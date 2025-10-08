package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ArrayListMultimap;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.entity.SysUserRole;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.jsonflow.*;
import com.pig4cloud.pigx.admin.service.SysDeptService;
import com.pig4cloud.pigx.admin.service.SysRoleService;
import com.pig4cloud.pigx.admin.service.SysUserRoleService;
import com.pig4cloud.pigx.admin.service.SysUserService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.datascope.DataScopeTypeEnum;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.entity.DefFlow;
import com.pig4cloud.pigx.jsonflow.api.vo.ToDoneJobVO;
import com.pig4cloud.pigx.jsonflow.service.DefFlowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 审批人计算入口（基于角色与数据权限）
 *
 * 设计要点：
 * 1）所有与库交互尽量做“批量拉取”，避免循环查库（N+1）。
 * 2）用内存结构做一次性计算：userId->roleIds，多角色取“最小 dsType”为最大权限。
 * 3）部门为树结构：构建父子邻接表，BFS 收集“本级及子级”。
 * 4）对空参、角色不存在、无匹配用户等场景统一安全返回空列表。
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/jf")
@Tag(name = "流程引擎")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class JFController {

    private final SysUserService sysUserService;
    private final SysUserRoleService sysUserRoleService;
    private final SysRoleService sysRoleService;
    private final SysDeptService sysDeptService;
    private final JfRunFlowService jfRunFlowService;
    private final DefFlowService defFlowService;
    /**
     * 获取流程节点审批人
     * 入参：
     *   roleCode  目标角色编码（如：DEPT_LEADER）
     *   userId    工单发起人ID
     *   deptId    工单发起人部门ID
     * 出参：
     *   满足数据权限的审批人 userId 列表
     */
    @PostMapping("/node/approver")
    @Operation(summary = "获取流程节点审批人")
    public R<List<Long>> nodeApprover(@RequestBody Map<String, Object> request) {
        log.info("$$$$$获取流程节点审批人,{}", request);
        String roleCode = MapUtil.getStr(request, "roleCode");
        Long createUserId = MapUtil.getLong(request, "createUser");

        if (StrUtil.isBlank(roleCode) || createUserId == null) {
            return R.ok(Collections.emptyList());
        }
        return R.ok(getApproverUserIds(roleCode, createUserId));
    }


    @GetMapping("/options")
    @Operation(summary = "流程定义下拉选项")
    public R<List<DefFlowOptionResponse>> listOptions() {
        List<DefFlow> list = defFlowService.list(
                Wrappers.<DefFlow>lambdaQuery()
                        .eq(DefFlow::getStatus, "1")   // 已发布
                        .eq(DefFlow::getDelFlag, "0") // 未删除
                        .orderByAsc(DefFlow::getSort)
                        .orderByDesc(DefFlow::getCreateTime)
        );
        return R.ok(list.stream()
                .map(item -> BeanUtil.copyProperties(item, DefFlowOptionResponse.class))
                .collect(Collectors.toList()));
    }


    @Operation(summary = "我的申请")
    @GetMapping("/page")
    public R<Page<MyFlowResponse>> page(
            @ParameterObject PageRequest pageRequest,
            @ParameterObject MyFlowRequest req) {
        req.setCreateUser(SecurityUtils.getUser().getId());
        return R.ok(jfRunFlowService.pageFlowList(PageUtil.toPage(pageRequest), req));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "我的申请导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                MyFlowResponse.BIZ_CODE,
                MyFlowResponse.class
        );
        return R.ok(fields);
    }


    @PostMapping("/export")
    @Operation(summary = "我的申请导出")
    //@PreAuthorize("@pms.hasPermission('result_export')")
    public void export(@RequestBody MyFlowExportWrapperRequest request) throws IOException {
        // 1. 拿到 ServletRequestAttributes
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }

        // 2. 再拿到 Jakarta HttpServletResponse
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }

        // 3. 查询数据
        IPage<MyFlowResponse> pageData = jfRunFlowService.pageFlowList(
                new Page<>(), request.getQuery()
        );

        // 4. 调用通用导出工具
        ExcelExportUtil.exportByBean(
                response,
                // 文件名（不带 .xlsx）
                "我的申请导出",
                // Sheet 名称
                "数据",
                // DTO 列表
                pageData.getRecords(),
                // 要导出的字段 keys
                request.getExport().getFieldKeys(),
                // DTO 类型
                MyFlowResponse.class
        );
    }

    /**
     * 查询任务数量
     * @param toDoneJobVO 任务记录
     * @return
     */
    @Operation(summary = "查询任务数量", description = "查询任务数量")
    @GetMapping("/to-done/count")
    public R<Long> getToDoneCount( @ModelAttribute ToDoneJobVO toDoneJobVO) {
        return R.ok(jfRunFlowService.getToDoneCount(toDoneJobVO));
    }

    /**
     * 计算审批人集合（核心算法）
     * 步骤：
     *   1. 取目标角色
     *   2. 取拥有该角色的候选用户（候选集）
     *   3. 批量取候选用户信息
     *   4. 批量取候选用户的“全部角色”并计算每个用户的最大数据权限（最小 dsType）
     *   5. 构建部门父子邻接表，做 OWN_CHILD_LEVEL 的子树匹配
     *   6. 按 dsType 规则过滤，得到最终可审批用户
     */
    public List<Long> getApproverUserIds(String roleCode, Long createUserId) {
        Long createDeptId = sysUserService.lambdaQuery()
                .eq(SysUser::getUserId, createUserId)
                .one().getDeptId();
        SysRole targetRole = sysRoleService.lambdaQuery()
                .eq(SysRole::getRoleCode, roleCode)
                .one();
        if (targetRole == null) {
            return Collections.emptyList();
        }

        List<SysUserRole> userRolesOfTarget = sysUserRoleService.list(
                Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getRoleId, targetRole.getRoleId())
        );
        if (CollUtil.isEmpty(userRolesOfTarget)) {
            return Collections.emptyList();
        }

        List<Long> candidateUserIds = userRolesOfTarget.stream()
                .map(SysUserRole::getUserId).distinct().toList();

        List<SysUser> candidateUsers = sysUserService.listByIds(candidateUserIds);
        if (CollUtil.isEmpty(candidateUsers)) {
            return Collections.emptyList();
        }

        List<SysUserRole> allRolesOfCandidates = sysUserRoleService.list(
                Wrappers.<SysUserRole>lambdaQuery().in(SysUserRole::getUserId, candidateUserIds)
        );
        if (CollUtil.isEmpty(allRolesOfCandidates)) {
            return Collections.emptyList();
        }

        Set<Long> allRoleIds = allRolesOfCandidates.stream()
                .map(SysUserRole::getRoleId).collect(Collectors.toSet());
        List<SysRole> allRoles = sysRoleService.listByIds(allRoleIds);
        Map<Long, SysRole> roleMap = allRoles.stream()
                .collect(Collectors.toMap(SysRole::getRoleId, r -> r, (a, b) -> a));

        ArrayListMultimap<Long, Long> userIdToRoleIds = ArrayListMultimap.create();
        allRolesOfCandidates.forEach(ur -> userIdToRoleIds.put(ur.getUserId(), ur.getRoleId()));

        Map<Long, List<Long>> parentToChildren = buildDeptAdjacency();

        List<Long> result = new ArrayList<>(candidateUsers.size());
        for (SysUser u : candidateUsers) {
            Integer dsType = resolveUserMaxDataScope(u.getUserId(), userIdToRoleIds, roleMap);
            if (dsType == null) {
                continue;
            }
            if (allowByDataScope(u, dsType, createUserId, createDeptId, parentToChildren)) {
                result.add(u.getUserId());
            }
        }
        return result;
    }

    /**
     * 计算用户在其“所有角色”维度上的最大数据权限
     * 约定：dsType 数值越小，权限越大（如 ALL 最小）
     * 取最小 dsType 作为用户最大权限
     */
    private Integer resolveUserMaxDataScope(Long userId,
                                            ArrayListMultimap<Long, Long> userIdToRoleIds,
                                            Map<Long, SysRole> roleMap) {
        Collection<Long> rids = userIdToRoleIds.get(userId);
        if (CollUtil.isEmpty(rids)) {
            return null;
        }
        Integer min = null;
        for (Long rid : rids) {
            SysRole role = roleMap.get(rid);
            if (role == null || role.getDsType() == null) {
                continue;
            }
            if (min == null || role.getDsType() < min) {
                min = role.getDsType();
            }
        }
        return min;
    }

    /**
     * 数据权限判定
     * 规则：
     *   ALL              直接通过
     *   SELF_LEVEL       仅本人
     *   OWN_LEVEL        本部门
     *   OWN_CHILD_LEVEL  本部门及其所有子部门（需树遍历）
     *   CUSTOM           自定义部门集合（dsScope 逗号分隔）
     */
    private boolean allowByDataScope(SysUser sysUser,
                                     Integer dsType,
                                     Long createUserId,
                                     Long createDeptId,
                                     Map<Long, List<Long>> parentToChildren) {
        if (dsType == null) {
            return false;
        }

        if (Objects.equals(dsType, DataScopeTypeEnum.ALL.getType())) {
            return true;
        }

        if (Objects.equals(dsType, DataScopeTypeEnum.SELF_LEVEL.getType())) {
            return Objects.equals(sysUser.getUserId(), createUserId);
        }

        if (Objects.equals(dsType, DataScopeTypeEnum.OWN_LEVEL.getType())) {
            Long selfDept = sysUser.getDeptId();
            return selfDept != null && Objects.equals(selfDept, createDeptId);
        }

        if (Objects.equals(dsType, DataScopeTypeEnum.OWN_CHILD_LEVEL.getType())) {
            Long selfDept = sysUser.getDeptId();
            if (selfDept == null) {
                return false;
            }
            Set<Long> cover = collectDeptWithChildren(selfDept, parentToChildren);
            return cover.contains(createDeptId);
        }

        if (Objects.equals(dsType, DataScopeTypeEnum.CUSTOM.getType())) {
            SysRole maxRole = findUserMaxRole(sysUser.getUserId());
            if (maxRole == null || StrUtil.isBlank(maxRole.getDsScope())) {
                return false;
            }
            Set<Long> allowed = StrUtil.split(maxRole.getDsScope(), StrUtil.COMMA)
                    .stream().filter(StrUtil::isNotBlank)
                    .map(Long::parseLong).collect(Collectors.toSet());
            return allowed.contains(createDeptId);
        }

        return false;
    }

    /**
     * 辅助：再次精确获取“最大权限角色”（用于 CUSTOM 拿 dsScope）
     * 若你能在 resolveUserMaxDataScope 时顺便返回“对应角色”，可以避免再次查库。
     */
    private SysRole findUserMaxRole(Long userId) {
        List<SysUserRole> urs = sysUserRoleService.list(
                Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId)
        );
        if (CollUtil.isEmpty(urs)) {
            return null;
        }
        Set<Long> roleIds = urs.stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());
        List<SysRole> roles = sysRoleService.listByIds(roleIds);
        return roles.stream()
                .filter(r -> r.getDsType() != null)
                .min(Comparator.comparingInt(SysRole::getDsType))
                .orElse(null);
    }

    /**
     * 构建部门父子邻接表
     * 仅选 deptId、parentId 字段，减少传输与内存
     * O(N) 构建，其中 N 为部门数
     */
    private Map<Long, List<Long>> buildDeptAdjacency() {
        List<SysDept> depts = sysDeptService.list(
                Wrappers.<SysDept>lambdaQuery().select(SysDept::getDeptId, SysDept::getParentId)
        );
        Map<Long, List<Long>> parentToChildren = new HashMap<>(Math.max(16, depts.size()));
        for (SysDept d : depts) {
            parentToChildren.computeIfAbsent(d.getParentId(), k -> new ArrayList<>())
                    .add(d.getDeptId());
        }
        return parentToChildren;
    }

    /**
     * 收集“本部门 + 所有子部门”
     * BFS/DFS 皆可；此处用 BFS
     * 平均复杂度 O(K)，K 为该子树规模
     */
    private Set<Long> collectDeptWithChildren(Long rootDeptId, Map<Long, List<Long>> parentToChildren) {
        Set<Long> all = new HashSet<>();
        Deque<Long> q = new ArrayDeque<>();
        q.add(rootDeptId);
        all.add(rootDeptId);

        while (!q.isEmpty()) {
            Long cur = q.poll();
            List<Long> children = parentToChildren.get(cur);
            if (CollUtil.isEmpty(children)) {
                continue;
            }
            for (Long ch : children) {
                if (all.add(ch)) {
                    q.add(ch);
                }
            }
        }
        return all;
    }
}
