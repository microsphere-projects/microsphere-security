# microsphere-security-core

> **版本线说明：** 本仓库目前不存在 `release` 或 `release-1.x` 分支。`current`
> 文档线由 `main` 分支生成（revision 为 `0.0.1-SNAPSHOT`，处于 1.0 之前，尚未发布任何版本）。
> javadoc 中的 `@since 1.0.0` 标签是对未来的承诺，并非已发布版本。
> 构件只能以快照形式从 Sonatype OSS 快照仓库获取
> （`https://s01.oss.sonatype.org/content/repositories/snapshots`），Maven Central 上没有。

## 概述

`microsphere-security-core` 是本项目的基础模块。它只包含一个公开类型，即
`io.microsphere.security.constants.SecurityConstants` 接口，其唯一职责是把配置属性名前缀
集中定义在**同一个地方**，供其余模块用作 `@ConfigurationProperties(prefix = ...)` 与
`@ConditionalOnProperty(prefix = ...)` 的取值。

该模块完全不依赖 Spring，是一个普通的 `jar`，任何模块或使用方引入它都不会牵入框架。
它的价值在于一致性：不必把字面量 `"microsphere.security."` 散落各处，共享前缀只需派生一次。

源文件：`microsphere-security-core/src/main/java/io/microsphere/security/constants/SecurityConstants.java`

## Maven 坐标

| 字段 | 值 |
|---|---|
| groupId | `io.github.microsphere-projects` |
| artifactId | `microsphere-security-core` |
| version | `${revision}`（由根 POM 解析为 `0.0.1-SNAPSHOT`） |
| packaging | `jar` |
| parent | `io.github.microsphere-projects:microsphere-security-parent:${revision}` |

```xml
<dependency>
    <groupId>io.github.microsphere-projects</groupId>
    <artifactId>microsphere-security-core</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

上面的 `<parent>` 描述的是该模块 POM 在 reactor 中的真实形态；在本仓库内版本一律写作
`${revision}` 而非字面量。

## API 说明

### 接口 `SecurityConstants`

```java
package io.microsphere.security.constants;

public interface SecurityConstants {

    String PROPERTY_NAME_PREFIX = MICROSPHERE_PROPERTY_NAME_PREFIX + "security" + DOT_CHAR;

    String WEB_SECURITY_PROPERTY_NAME_PREFIX = PROPERTY_NAME_PREFIX + "web";

    String WEB_SECURITY_FILTER_PROPERTY_NAME_PREFIX = WEB_SECURITY_PROPERTY_NAME_PREFIX + DOT_CHAR + "filter" + DOT_CHAR;

    String WEB_SECURITY_CSP_PROPERTY_NAME_PREFIX = WEB_SECURITY_PROPERTY_NAME_PREFIX + DOT_CHAR + "csp" + DOT_CHAR;

    String WEB_SECURITY_CORS_PROPERTY_NAME_PREFIX = WEB_SECURITY_PROPERTY_NAME_PREFIX + DOT_CHAR + "cors" + DOT_CHAR;
}
```

（真实文件中 `MICROSPHERE_PROPERTY_NAME_PREFIX` 与 `DOT_CHAR` 是静态导入的，为了可读性此处
省略了 import 行。）

| 常量 | 展开后的值 | 是否以 `.` 结尾 | 作为下列内容的前缀 |
|---|---|---|---|
| `PROPERTY_NAME_PREFIX` | `microsphere.security.` | 是 | `@ConditionalOnEnabledSecurity` → `microsphere.security.enabled` |
| `WEB_SECURITY_PROPERTY_NAME_PREFIX` | `microsphere.security.web` | **否** | `WebSecurityProperties` 与 `@ConditionalOnEnabledWebSecurity` |
| `WEB_SECURITY_FILTER_PROPERTY_NAME_PREFIX` | `microsphere.security.web.filter.` | 是 | 过滤器组合开关 `microsphere.security.web.filter.enabled` |
| `WEB_SECURITY_CSP_PROPERTY_NAME_PREFIX` | `microsphere.security.web.csp.` | 是 | CSP 配置 `microsphere.security.web.csp.*` |
| `WEB_SECURITY_CORS_PROPERTY_NAME_PREFIX` | `microsphere.security.web.cors.` | 是 | CORS 配置 `microsphere.security.web.cors.*` |

### 需要说明的怪异点：`WEB_SECURITY_PROPERTY_NAME_PREFIX` 结尾没有点

`PROPERTY_NAME_PREFIX` 以 `.` 结尾（`microsphere.security.`），但
`WEB_SECURITY_PROPERTY_NAME_PREFIX` 是由 `PROPERTY_NAME_PREFIX + "web"` 构造的，因此它止步于
`microsphere.security.web`。而由它派生的三个前缀自己又追加了 `DOT_CHAR`，所以它们确实以点结尾。

这一不对称是代码中有意为之，并且在实际行为上可观察：

- `@ConfigurationProperties(prefix = WEB_SECURITY_PROPERTY_NAME_PREFIX)` 绑定的属性根是
  `microsphere.security.web`。
- `@ConditionalOnProperty(prefix = WEB_SECURITY_PROPERTY_NAME_PREFIX, name = "enabled")`
  解析为 `microsphere.security.web.enabled`。
- 其它前缀已经以 `.` 结尾，因此 `name` 可直接拼接。

实际影响是：绝不要在拼接属性名时直接使用 `WEB_SECURITY_PROPERTY_NAME_PREFIX` 而不自行补上点。

## 配置

本模块不读取任何配置，也不提供任何运行期行为，因此这里没有需要配置的东西。它定义的是
[microsphere-security-spring-boot](microsphere-security-spring-boot.zh.md)
所读取属性的**名称**。如果你要找属性清单表，应该去读那一页。

## 依赖

声明于 `microsphere-security-core/pom.xml`，没有任何依赖写出版本字面量：

| 依赖 | 作用域 | 版本来源 |
|---|---|---|
| `io.github.microsphere-projects:microsphere-java-core` | compile（默认） | 由外部管理，经由 `microsphere-security-parent` 导入的 `microsphere-spring-cloud-dependencies` BOM |
| `org.junit.jupiter:junit-jupiter-api` | `test` | 由外部管理，同一条链 |

`microsphere-java-core` 并非可有可无：`SecurityConstants` 用到了它的两个类型，
`io.microsphere.constants.PropertyConstants.MICROSPHERE_PROPERTY_NAME_PREFIX`（提供 `microsphere.` 根）
与 `io.microsphere.constants.SymbolConstants.DOT_CHAR`（提供 `.`）。

## 代码示例

本模块自带的测试是对常量精确值最短而完整的演示，上面的表格正是以它为权威依据。路径：
`microsphere-security-core/src/test/java/io/microsphere/security/constants/SecurityConstantsTest.java`

```java
public class SecurityConstantsTest {

    @Test
    public void testPropertyNamePrefix() {
        assertEquals("microsphere.security.", SecurityConstants.PROPERTY_NAME_PREFIX);
    }

    @Test
    public void testWebSecurityPropertyNamePrefix() {
        assertEquals("microsphere.security.web", SecurityConstants.WEB_SECURITY_PROPERTY_NAME_PREFIX);
    }

    @Test
    public void testWebSecurityFilterPropertyNamePrefix() {
        assertEquals("microsphere.security.web.filter.", SecurityConstants.WEB_SECURITY_FILTER_PROPERTY_NAME_PREFIX);
    }

    @Test
    public void testWebSecurityCspPropertyNamePrefix() {
        assertEquals("microsphere.security.web.csp.", SecurityConstants.WEB_SECURITY_CSP_PROPERTY_NAME_PREFIX);
    }

    @Test
    public void testWebSecurityCorsPropertyNamePrefix() {
        assertEquals("microsphere.security.web.cors.", SecurityConstants.WEB_SECURITY_CORS_PROPERTY_NAME_PREFIX);
    }
}
```

值得注意的是，测试本身就断言了 `WEB_SECURITY_PROPERTY_NAME_PREFIX` 结尾没有点，因此这个怪异点
是被构建锁定的行为，而非疏漏。

## 版本信息

- 当前 revision：`0.0.1-SNAPSHOT`（根 `pom.xml` 中的 `revision` 属性）。
- 本模块没有任何已发布版本，也没有 `release` 分支；该构件仅以快照形式存在于 Sonatype OSS 快照仓库。
- `SecurityConstants` 上的 `@since 1.0.0` 标签是对未来版本的承诺，并不是 `1.0.0` 已存在的证据。
- 由于这些值是编译期 `String` 常量，它们会被**内联**进所有引用它们的类。若后续版本改动前缀，
  已编译的使用方不重新编译是不会生效的。

- **发布日期：** 无 —— 截至版本 `0.0.1-SNAPSHOT`，本模块从未正式发布（无 git 标签、无 `release`
  分支、未进入 Maven Central）。
- **变更日志摘要：** 仓库中没有 `CHANGELOG` 或发布说明文件；`main` 分支的提交历史是唯一的变更记录。

## 兼容性

- 不依赖 Spring、Servlet 或日志框架的纯 Java 库，因此在 JDK 允许的范围内处处可用。
- Java 语言级别**继承自外部父 POM `microsphere-build:0.0.21`**，本仓库中没有任何地方声明它；
  仓库的 CI 工作流（`.github/workflows/maven.yml`）使用 Temurin JDK 8 构建，这只是关于 CI 的事实，
  并非本仓库声明的基线。
- 你实际拿到的 `microsphere-java-core` 版本由外部导入的 `microsphere-spring-cloud-dependencies`
  BOM 决定，因此取决于你的构建解析到了该 BOM 的哪一个快照。

### Java 版本矩阵

| Java 版本 | 是否支持 | 说明 |
| --- | --- | --- |
| 8 | 支持 | CI 已验证：`.github/workflows/maven.yml` 使用 Temurin `java: [ '8' ]` 构建。权威的 source/target 级别继承自外部父 POM `microsphere-build:0.0.21`，本仓库未声明。 |
| 11 | 未验证 | 本仓库 CI 未覆盖；源码没有使用 Java 8 之后的 API，预期可在 11 上运行，但此处未测试。 |
| 17 | 未验证 | 同 11 —— 本模块是不依赖任何框架的纯 Java 库。 |

### 依赖兼容性

| 依赖 | 作用域 | 本版本线的版本范围 | 来源 |
| --- | --- | --- | --- |
| `io.github.microsphere-projects:microsphere-java-core` | compile | 本 POM 中不写版本；由外部依赖链解析决定 | 外部 `microsphere-spring-cloud-dependencies` BOM |
| `org.junit.jupiter:junit-jupiter-api` | test | 本 POM 中不写版本 | 同上外部 BOM 链 |

## 相关文档

- [用户指南索引](README.md)——返回指南总览。
- [microsphere-security-spring-boot](microsphere-security-spring-boot.zh.md)——这些前缀的实际消费方。
- [microsphere-security-parent](microsphere-security-parent.zh.md)——约定父 POM。
- [microsphere-security-dependencies](microsphere-security-dependencies.zh.md)——管理本构件的 BOM。
- 开发者指南：[microsphere-security-core](../developer-guide/microsphere-security-core.zh.md)

## English

[English](microsphere-security-core.md)
