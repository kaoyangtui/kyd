package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysFile;
import com.pig4cloud.pigx.admin.api.entity.SysFileGroup;
import com.pig4cloud.pigx.admin.constants.FileGroupTypeEnum;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.file.FilePageRequest;
import com.pig4cloud.pigx.admin.dto.file.FileResponse;
import com.pig4cloud.pigx.admin.dto.file.FileUpdateRequest;
import com.pig4cloud.pigx.admin.entity.FileEntity;
import com.pig4cloud.pigx.admin.entity.IcLayoutEntity;
import com.pig4cloud.pigx.admin.mapper.FileMapper;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.service.SysFileGroupService;
import com.pig4cloud.pigx.admin.service.SysFileService;
import com.pig4cloud.pigx.common.core.util.WebUtils;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.file.core.FileProperties;
import com.pig4cloud.pigx.common.file.core.FileTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<FileMapper, FileEntity> implements FileService {

    private final SysFileService sysFileService;
    private final SysFileGroupService sysFileGroupService;
    private final FileTemplate fileTemplate;
    private final FileProperties properties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(FileCreateRequest request) {
        FileEntity entity = BeanUtil.copyProperties(request, FileEntity.class);
        return this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchCreate(List<FileCreateRequest> requestList) {
        if (CollUtil.isEmpty(requestList)) {
            return Boolean.TRUE;
        }

        // 提取所有 code 字段，非空去重
        List<String> codeList = requestList.stream()
                .map(FileCreateRequest::getCode)
                .filter(StrUtil::isNotBlank)
                .distinct()
                .toList();

        if (CollUtil.isNotEmpty(codeList)) {
            // 先删除已有的相同 code 的记录
            this.remove(Wrappers.<FileEntity>lambdaQuery().in(FileEntity::getCode, codeList));
        }

        // 执行批量保存
        List<FileEntity> entityList = BeanUtil.copyToList(requestList, FileEntity.class);
        return this.saveBatch(entityList);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(FileUpdateRequest request) {
        FileEntity entity = BeanUtil.copyProperties(request, FileEntity.class);
        return this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeByIds(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public IPage<FileResponse> pageResult(Page page, FilePageRequest request) {
        var wrapper = Wrappers.<FileEntity>lambdaQuery()
                .in(ObjectUtil.isNotEmpty(request.getIds()), FileEntity::getId, request.getIds())
                .like(StrUtil.isNotBlank(request.getCode()), FileEntity::getCode, request.getCode())
                .eq(StrUtil.isNotBlank(request.getApplyType()), FileEntity::getApplyType, request.getApplyType())
                .like(StrUtil.isNotBlank(request.getSubjectName()), FileEntity::getSubjectName, request.getSubjectName())
                .eq(StrUtil.isNotBlank(request.getBizType()), FileEntity::getBizType, request.getBizType())
                .like(StrUtil.isNotBlank(request.getFileName()), FileEntity::getFileName, request.getFileName())
                .eq(StrUtil.isNotBlank(request.getFileType()), FileEntity::getFileType, request.getFileType())
                .eq(StrUtil.isNotBlank(request.getCreateBy()), FileEntity::getCreateBy, request.getCreateBy())
                .eq(StrUtil.isNotBlank(request.getDeptId()), FileEntity::getDeptId, request.getDeptId())
                .ge(StrUtil.isNotBlank(request.getBeginTime()), FileEntity::getCreateTime, request.getBeginTime())
                .le(StrUtil.isNotBlank(request.getEndTime()), FileEntity::getCreateTime, request.getEndTime());

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (request.getIds() != null && !request.getIds().isEmpty()) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        Page<FileEntity> resPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());

        return resPage.convert(e -> BeanUtil.copyProperties(e, FileResponse.class));
    }

    @Override
    public FileCreateRequest getFileCreateRequest(String fileName,
                                                  String code,
                                                  String applyType,
                                                  String subjectName,
                                                  String bizType) {
        fileName = extractFileName(fileName);
        SysFile sysFile = sysFileService.lambdaQuery()
                .eq(SysFile::getFileName, fileName)
                .orderByDesc(SysFile::getCreateTime)
                .last("limit 1")
                .one();
        if (ObjectUtil.isNotNull(sysFile)) {
            FileCreateRequest fileCreateRequest = new FileCreateRequest();
            fileCreateRequest.setCode(code);
            fileCreateRequest.setApplyType(applyType);
            fileCreateRequest.setSubjectName(subjectName);
            fileCreateRequest.setBizType(bizType);
            fileCreateRequest.setFileName(sysFile.getFileName());
            fileCreateRequest.setFileType(sysFile.getType());
            fileCreateRequest.setDownloadName(sysFile.getOriginal());
            fileCreateRequest.setFileSize(sysFile.getFileSize());
            return fileCreateRequest;
        }
        return null;
    }

    public String extractFileName(String url) {
        if (StrUtil.isBlank(url)) {
            return null;
        }
        // 支持 fileName=xxx 或 fileName=xxx&xxx=yyy 这种
        String pattern = "(?:\\?|&)?fileName=([^&]+)";
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(pattern).matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    @Override
    public String uploadFileByUrl(String url, String dir, FileGroupTypeEnum type) {
        try {
            SysFileGroup sysFileGroup = sysFileGroupService.lambdaQuery()
                    .eq(SysFileGroup::getType, type.getValue())
                    .eq(SysFileGroup::getPid, -1L)
                    .eq(SysFileGroup::getName, "系统专利资源组")
                    .last("limit 1")
                    .one();
            if (sysFileGroup == null) {
                sysFileGroup = new SysFileGroup();
                sysFileGroup.setPid(-1L);
                sysFileGroup.setType(type.getValue());
                sysFileGroup.setName("系统专利资源组");
                sysFileGroupService.save(sysFileGroup);
            }
            Map<String, String> map = uploadFileByUrl(url, dir, sysFileGroup.getId(), String.valueOf(sysFileGroup.getType()));
            String mapUrl = StrUtil.EMPTY;
            if (null != map) {
                mapUrl = map.get("url");
            }
            return mapUrl;
        } catch (Exception e) {
            log.error("上传文件异常", e);
        }
        return null;
    }

    /**
     * 通过文件URL上传文件
     *
     * @param url     文件的下载地址
     * @param dir     文件夹
     * @param groupId 分组ID
     * @param type    类型
     * @return
     */
    public Map<String, String> uploadFileByUrl(String url, String dir, Long groupId, String type) {
        // 1. 解析文件名和后缀
        String path = URLUtil.getPath(url);                         // e.g. /dir/a.png
        String originFileName = FileUtil.getName(path);             // e.g. a.png
        String ext = FileUtil.extName(originFileName);              // e.g. png
        String fileName = IdUtil.simpleUUID() + (StrUtil.isBlank(ext) ? "" : StrUtil.DOT + ext);    // 拼接随机文件名


        Map<String, String> resultMap = new HashMap<>(4);
        resultMap.put("bucketName", properties.getBucketName());
        resultMap.put("fileName", fileName);
        resultMap.put("url", String.format("/admin/sys-file/oss/file?fileName=%s", fileName));

        try (InputStream inputStream = URLUtil.getStream(new URL(url))) {
            String contentType = FileUtil.getMimeType(fileName);
            fileTemplate.putObject(properties.getBucketName(), dir, fileName, inputStream, contentType);

            // 获取文件大小
            URLConnection connection = new URL(url).openConnection();
            long fileSize = connection.getContentLengthLong();

            fileLog(dir, fileName, groupId, type, originFileName, fileSize);
        } catch (Exception e) {
            log.error("URL上传失败", e);
            return null;
        }
        return resultMap;
    }

    // 用于 URL 上传（不需要 MultipartFile）
    private void fileLog(String dir, String fileName, Long groupId, String type, String originFileName, long fileSize) {
        SysFile sysFile = new SysFile();
        sysFile.setFileName(fileName);
        sysFile.setDir(dir);
        sysFile.setOriginal(originFileName);
        sysFile.setFileSize(fileSize);
        sysFile.setBucketName(properties.getBucketName());
        sysFile.setType(type);
        sysFile.setGroupId(groupId);
        // 这里没有 hash，可选
        sysFileService.save(sysFile);
    }
}