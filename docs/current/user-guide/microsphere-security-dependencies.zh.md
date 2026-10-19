# microsphere-security-dependencies

> **版本线说明：** 本仓库目前不存在 `release` 或 `release-1.x` 分支。`current`
> 文档线由 `main` 分支生成（revision 为 `0.0.1-SNAPSHOT`，处于 1.0 之前，尚未发布任何版本）。
> javadoc 中的 `@since 1.0.0` 标签是对未来的承诺，并非已发布版本。
> 构件只能以快照形式从 Sonatype OSS 快照仓库获取
> （`https://s01.oss.sonatype.org/content/repositories/snapshots`），Maven Central 上没有。

## 概述

`microsphere-security-dependencies` 是本项目自己的物料清单（BOM）。它是一个 `pom` 打包方式的
模块，全部内容就是一个 `<dependencyManagement>` 段，列出本项目产出的构件，使使用方在每条
`<dependency>` 中不必重复书写版本字符串。

它刻意保持精简：两条受管记录，没有属性，没有插件，没有构建段。至于三方版本（Spring Boot、
Servlet API、commons、JUnit），必须去看该模块的 parent 所导入的外部 BOM——见
[microsphere-security-parent](microsphere-security-parent.zh.md)。

文件：`microsphere-security-dependencies/pom.xml`（38 行）。

## Maven 坐标

| 字段 | 值 |
|---|---|
| groupId | `io.github.microsphere-projects` |
| artifactId | `microsphere-security-dependencies` |
| version | `${revision}`（解析为 `0.0.1-SNAPSHOT`） |
| packaging | `pom` |
| parent | `io.github.microsphere-projects:microsphere-security-parent:${revision}` |
| name | `Microsphere :: Security :: Dependencies` |

## POM 详解

### 被管理的坐标

恰好两条记录，版本都写作 `${revision}`：

| 受管 `artifactId` | `groupId` | 版本表达式 | 解析后的版本 | 是否由本 reactor 构建 |
|---|---|---|---|---|
| `microsphere-security-core` | `io.github.microsphere-projects` | `${revision}` | `0.0.1-SNAPSHOT` | 是 |
| `microsphere-security-spring-boot-starter` | `io.github.microsphere-projects` | `${revision}` | `0.0.1-SNAPSHOT` | **否** |

```xml
<dependencyManagement>
    <dependencies>

        <dependency>
            <groupId>io.github.microsphere-projects</groupId>
            <artifactId>microsphere-security-core</artifactId>
            <version>${revision}</version>
        </dependency>

        <dependency>
            <groupId>io.github.microsphere-projects</groupId>
            <artifactId>microsphere-security-spring-boot-starter</artifactId>
            <version>${revision}</version>
        </dependency>

    </dependencies>
</dependencyManagement>
```

### starter 命名不一致（使用 BOM 前必读）

BOM 管理的是 `microsphere-security-spring-boot-starter`。而真正构建 Spring Boot 集成能力的模块
名叫 `microsphere-security-spring-boot`（见 `microsphere-security-spring-boot/pom.xml`，其
`<artifactId>` 没有 `-starter` 后缀），并且根 POM 的 `<modules>` 只包含那四个模块。

因此就今天而言：

- `microsphere-security-spring-boot-starter` 是一个**面向未来的占位名**——在 BOM 中预留、但本
  reactor 尚未产出的构件名。向 Maven 请求它会解析失败。
- BOM **没有**管理 `microsphere-security-spring-boot`，也就是你实际必须依赖的那个构件。因此导入
  本 BOM 并**不能**让你在依赖 Spring Boot 模块时省掉 `<version>`。
- 仓库中没有任何信息表明 starter 模块何时、或是否会出现在。不要围绕它做规划；请在你所使用的
  文档线上重新核对 `microsphere-security-dependencies/pom.xml` 与根 `<modules>` 列表。

### 该 BOM 在 POM 层级中的位置

本模块继承自 `microsphere-security-parent`，与各 jar 模块同一个 parent。这意味着当*它*被构建时，
parent 对外部 `microsphere-spring-cloud-dependencies` BOM 的导入同样作用于它。作为使用方，你应当把
上文两条记录视为该 BOM 贡献的内容；如果你还需要 parent 链条上管理的三方版本，请在你的 POM 中依赖
该 parent，或者自行导入 `microsphere-spring-cloud-dependencies`。

## 配置

没有可配置的东西：BOM 没有属性、没有代码、也没有运行期。控制行为的属性位于
[microsphere-security-spring-boot](microsphere-security-spring-boot.zh.md)。

## 代码示例

### 在使用方构建中导入 BOM

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

<dependencies>
    <!-- version supplied by the imported BOM -->
    <dependency>
        <groupId>io.github.microsphere-projects</groupId>
        <artifactId>microsphere-security-core</artifactId>
    </dependency>
</dependencies>
```

要能解析 `0.0.1-SNAPSHOT`，快照仓库必须可达：

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

### 目前如何声明 Spring Boot 模块

由于 BOM 并未管理它，请显式写出版本：

```xml
<dependency>
    <groupId>io.github.microsphere-projects</groupId>
    <artifactId>microsphere-security-spring-boot</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

在 reactor 内部，`microsphere-security-spring-boot/pom.xml` 做的是同样的事但保留了占位表达式：
它在 `microsphere-security-core` 依赖上声明 `<version>${revision}</version>`，而不是依赖该 BOM。

## 版本信息

- 版本：`${revision}` → `0.0.1-SNAPSHOT`。不存在发布版本，也不存在 `release` 分支，因此该 BOM
  只有快照可用。
- 两条受管记录都继承 reactor 的同一个 revision，因此 BOM 与各 jar 模块总是同步移动。
- 该 BOM 在任何地方都**没有**钉住三方版本；所有外部版本由 parent 导入的
  `microsphere-spring-cloud-dependencies` BOM 决定，并最终取决于外部父 POM `microsphere-build:0.0.21`。

- **发布日期：** 无 —— 截至版本 `0.0.1-SNAPSHOT`，本模块从未正式发布（无 git 标签、无 `release`
  分支、未进入 Maven Central）。
- **变更日志摘要：** 仓库中没有 `CHANGELOG` 或发布说明文件；`main` 分支的提交历史是唯一的变更记录。

## 兼容性

- `pom` 形式的 BOM 本身与版本无关：它声明的是坐标而非字节码，因此既不约束 JDK，也不约束 Spring
  代际。
- 它所管理的构件来自本 reactor，而本 reactor 的代码针对 Spring Boot 2.x servlet 栈
  （`javax.servlet.*` 导入加上 `META-INF/spring.factories` 注册）。该代码线不支持 Spring Boot 3.x。
- JDK 基线继承自外部父 POM `microsphere-build:0.0.21`，本仓库中未声明。

### Java 版本矩阵

BOM 不含字节码；适用的是它所管理的构件（均由本 reactor 构建）的基线：

| Java 版本 | 是否支持 | 说明 |
| --- | --- | --- |
| 8 | 支持 | CI 已验证（`.github/workflows/maven.yml`，Temurin `java: [ '8' ]`）；权威的 source/target 级别继承自外部父 POM `microsphere-build:0.0.21`，本仓库未声明。 |
| 11 | 未验证 | CI 未覆盖；被管理的模块只使用 Java 8 时代的 API。 |
| 17 | 未验证 | CI 未覆盖。 |

### 受管坐标兼容性

| 受管坐标 | 是否由本 reactor 产出 | 使用注意 |
| --- | --- | --- |
| `io.github.microsphere-projects:microsphere-security-core` | 是 | 不写版本的依赖可经此 BOM 解析 |
| `io.github.microsphere-projects:microsphere-security-spring-boot-starter` | **否** —— 实际构建的构件是 `microsphere-security-spring-boot` | 前瞻占位条目；请显式依赖真实的 artifactId |

## 相关文档

- [用户指南索引](README.md)——返回指南总览。
- [microsphere-security-core](microsphere-security-core.zh.md)——BOM 真正管理的构件。
- [microsphere-security-spring-boot](microsphere-security-spring-boot.zh.md)——你必须依赖、但 BOM 未管理的构件。
- [microsphere-security-parent](microsphere-security-parent.zh.md)——提供全部三方版本的 parent。
- 开发者指南：[microsphere-security-dependencies](../developer-guide/microsphere-security-dependencies.zh.md)

## English

[English](microsphere-security-dependencies.md)
