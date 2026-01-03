# Repository Guidelines

## 项目结构与模块组织
- `src/main/java/com/pig4cloud/pigx` 存放 Spring Boot 业务代码，常见分层为 `controller`、`service`、`service/impl`、`mapper`、`entity`、`dto`、`config`、`utils`。
- `src/main/resources` 存放配置与运行期资源，包括 `application-*.yml`、`logback-spring.xml`，以及 `mapper/` 下的 MyBatis XML。
- `sql/` 保存数据库初始化和维护脚本。
- `src/test` 目前仅包含数据/SQL 文件，暂无自动化测试源码。

## 构建、测试与本地开发命令
- `mvn clean package` 构建可运行 jar（`target/kyd.jar`）。
- `mvn spring-boot:run` 以默认 `dev` 配置启动服务。
- `mvn -Ptest spring-boot:run` 或 `mvn -Pprod spring-boot:run` 分别加载 `application-test.yml` 或 `application-prod.yml`。
- `mvn -DskipTests package` 打包时跳过测试。
- 如需自定义 Maven 仓库，使用 `mvn -s settings.xml ...`（见 `settings.xml`）。

## 代码风格与命名约定
- 以邻近文件为准保持一致，不做全局格式化。
- Java 命名：类 `PascalCase`，方法/字段 `lowerCamelCase`，常量 `UPPER_SNAKE_CASE`，包名小写。
- DTO 与实体分别放在 `dto`、`entity` 包内，对齐 mapper XML 的命名习惯。

## 测试规范
- 当前模块暂无单元/集成测试。
- 若新增测试，请放在 `src/test/java` 并保持包路径一致，使用 `mvn test` 运行（继承父工程配置）。

## 提交与合并请求规范
- Git 记录以简短中文主题为主（例如“修复 ...”），请保持同样风格。
- PR 需说明业务背景、影响模块（如 `controller`、`service`、`mapper`），并关联涉及的 SQL 或配置改动。

## 配置与运行提示
- 通过 Maven profiles（`dev`/`test`/`prod`）与 `profiles.active` 控制环境。
- `application-*.yml` 中避免提交密钥，优先使用环境变量或外部配置。
- 本地运行日志默认写入 `logs/`。
