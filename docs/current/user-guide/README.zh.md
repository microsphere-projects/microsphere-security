# 用户指南 —— microsphere-security

> **版本线说明：** 本仓库目前不存在 `release` 或 `release-1.x` 分支。`current`
> 文档线由 `main` 分支生成（revision 为 `0.0.1-SNAPSHOT`，处于 1.0 之前，尚未发布任何版本）。
> javadoc 中的 `@since 1.0.0` 标签是对未来的承诺，并非已发布版本。
> 构件只能以快照形式从 Sonatype OSS 快照仓库获取
> （`https://s01.oss.sonatype.org/content/repositories/snapshots`），Maven Central 上没有。

## 这个库是什么

`microsphere-security` 是 Spring Boot Servlet Web 应用的一个轻量、增量式安全层。
它并不取代 Spring Security，而是把 Web 应用通常需要手工装配的 HTTP 响应头与浏览器保护机制
统一由配置来驱动：

- `Strict-Transport-Security`（HSTS）
- `X-XSS-Protection`
- `X-Content-Type-Options: nosniff`
- `X-Frame-Options`（防点击劫持）
- `Content-Security-Policy` / `Content-Security-Policy-Report-Only`
- CORS，包含预检（preflight）处理

所有行为都由 `microsphere.security.` 前缀下的配置属性决定，并且所有能力默认都是**关闭**的——
必须显式打开。精确的开关语义见
[microsphere-security-spring-boot](microsphere-security-spring-boot.zh.md)。

该仓库是 Maven 多模块工程，根坐标为
`io.github.microsphere-projects:microsphere-security:${revision}`，其中 `${revision}`
在根 `pom.xml` 中被定义为 `0.0.1-SNAPSHOT`。

## 模块地图

| 模块 | 打包方式 | 提供的能力 | 文档 |
|---|---|---|---|
| `microsphere-security-core` | `jar` | `SecurityConstants` 接口——被其它所有模块共用的属性名前缀。不依赖 Spring。 | [core](microsphere-security-core.zh.md) |
| `microsphere-security-parent` | `pom` | 所有模块共同使用的约定父 POM；导入外部 `microsphere-spring-cloud-dependencies` BOM，因此三方版本是继承而来而非在本仓库声明。 | [parent](microsphere-security-parent.zh.md) |
| `microsphere-security-dependencies` | `pom`（BOM） | 为本项目自有构件提供 `dependencyManagement` 坐标，使用者无需重复写版本。 | [dependencies](microsphere-security-dependencies.zh.md) |
| `microsphere-security-spring-boot` | `jar` | 面向使用者的核心模块：自动配置、`WebSecurityProperties`、条件注解、过滤器与 `WebSecurityProcessor` SPI。 | [spring-boot](microsphere-security-spring-boot.zh.md) |

## 快速开始

### 1. 添加快照仓库

目前没有任何构件发布到 Maven Central，因此必须让快照仓库可被解析：

```xml
<repositories>
    <repository>
        <id>snapshot</id>
        <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
        <releases>
            <enabled>false</enabled>
        </releases>
    </repository>
</repositories>
```

### 2. 声明依赖

本 reactor 中还没有 starter 构件——真正提供自动配置的模块名就是
`microsphere-security-spring-boot`，请使用这个精确的 `artifactId`：

```xml
<dependency>
    <groupId>io.github.microsphere-projects</groupId>
    <artifactId>microsphere-security-spring-boot</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

也可以导入 BOM，从而省略 `<version>`：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.microsphere-projects</groupId>
            <artifactId>microsphere-security-dependencies</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

在依赖该 BOM 之前请先阅读
[microsphere-security-dependencies](microsphere-security-dependencies.zh.md)：
它管理了一个本 reactor 尚未产出的构件名。

### 3. 开启 Web 安全

注册通过 `META-INF/spring.factories` 完成，因此不需要 `@Import` 或任何 `@Enable*` 注解。
一切由两个属性决定：

```properties
microsphere.security.enabled=true
microsphere.security.web.enabled=true
```

`microsphere.security.enabled` 在不配置时即生效（`matchIfMissing = true`）；
`microsphere.security.web.enabled` **不是**如此——在你显式设置为 `true` 之前，
Web 层完全不会介入。随后每一项保护能力（HSTS、XSS、X-Frame-Options、
X-Content-Type-Options、CSP、CORS）都有自己的 `enabled` 开关，它们同样默认是
`false`。

### 4. 最小可用配置

```yaml
microsphere:
  security:
    web:
      enabled: true
      x-frame:
        enabled: true
        option: SAMEORIGIN
      x-content-type:
        enabled: true
      hsts:
        enabled: true
        max-age-in-seconds: 31536000
```

## 如何阅读这些文档

- 每个页面结尾都有指向同目录下中文镜像（`*.zh.md`）的链接，中文页面结尾则链回英文。
- 本指南中的示例全部取自各模块自己的 `src/test/java` 源码。仓库中**没有**
  `test-cases` 目录，因此不存在可单独检出的示例工程。
- 凡是无法在本仓库中核实的事实（例如 JDK 基线或 Spring Boot 版本），页面只会说明它
  继承自哪个外部 POM，而不会去猜测具体数字。
- `current` 线始终对应 `main` 分支；这里没有任何内容描述已发布构件。

## 延伸阅读

- [开发者指南](../developer-guide/README.zh.md)——构建、模块内部结构、代码约定。
- [文档首页](../../README.md)——全部文档版本线。
- [仓库 README](../../../README.md)——项目概述与许可证。

## English

[English](README.md)
