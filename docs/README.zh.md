# Microsphere Security — 文档

**Microsphere Security** 项目的文档，提供两种语言：

| 语言 | 入口 |
| --- | --- |
| English（默认语言） | [README.md](README.md) |
| 简体中文 | 您在这里。 |

> **版本线说明：** 本仓库目前**没有 `release` 或 `release-1.x` 分支**（只有 `main`，也没有任何
> git 标签）。因此 `current/` 文档线基于 `main` 分支生成，版本号为 `0.0.1-SNAPSHOT` ——
> 处于 1.0 之前，尚未发布到 Maven Central，仅有快照构件
> （`https://s01.oss.sonatype.org/content/repositories/snapshots`）。javadoc 中的
> `@since 1.0.0` 是前瞻承诺而非已发布版本。`1.x/` 目录并不存在，只有在创建 `release-1.x`
> 分支后才会出现。

## 版本线

| 文档线 | 源码分支 | 版本 | 用户指南 | 开发者指南 |
| --- | --- | --- | --- | --- |
| `current/` | `main`（尚无 `release` 分支） | `0.0.1-SNAPSHOT` | [current/user-guide/README.md](current/user-guide/README.md) · [中文](current/user-guide/README.zh.md) | [current/developer-guide/README.md](current/developer-guide/README.md) · [中文](current/developer-guide/README.zh.md) |
| `1.x/` | `release-1.x` | — | *未发布 —— 分支不存在* | *未发布 —— 分支不存在* |

## 该读哪份指南

- **[用户指南](current/user-guide/README.zh.md)** —— 各 Maven 模块的能力、Maven 坐标、
  `microsphere.security.web.*` 完整配置属性参考、取自项目自身测试的可运行代码示例、
  版本信息与兼容范围（Tomcat 上的 Spring Boot 2.x servlet 技术栈）。
- **[开发者指南](current/developer-guide/README.zh.md)** —— 架构设计与模块依赖关系图、
  逐类源码分析、所应用的设计模式（组合、职责链、策略/SPI、元注解条件装配），
  以及自动配置条件门控与请求处理流程的 mermaid 流程图/时序图。

## 已覆盖的 Maven 模块（两份指南均按模块各一页）

| 模块 | 打包方式 | 用户指南 | 开发者指南 |
| --- | --- | --- | --- |
| `microsphere-security-parent` | pom | [文档](current/user-guide/microsphere-security-parent.zh.md) · [English](current/user-guide/microsphere-security-parent.md) | [文档](current/developer-guide/microsphere-security-parent.zh.md) · [English](current/developer-guide/microsphere-security-parent.md) |
| `microsphere-security-dependencies` | pom（BOM） | [文档](current/user-guide/microsphere-security-dependencies.zh.md) · [English](current/user-guide/microsphere-security-dependencies.md) | [文档](current/developer-guide/microsphere-security-dependencies.zh.md) · [English](current/developer-guide/microsphere-security-dependencies.md) |
| `microsphere-security-core` | jar | [文档](current/user-guide/microsphere-security-core.zh.md) · [English](current/user-guide/microsphere-security-core.md) | [文档](current/developer-guide/microsphere-security-core.zh.md) · [English](current/developer-guide/microsphere-security-core.md) |
| `microsphere-security-spring-boot` | jar | [文档](current/user-guide/microsphere-security-spring-boot.zh.md) · [English](current/user-guide/microsphere-security-spring-boot.md) | [文档](current/developer-guide/microsphere-security-spring-boot.zh.md) · [English](current/developer-guide/microsphere-security-spring-boot.md) |

开发者指南附加页面：[architecture.zh.md](current/developer-guide/architecture.zh.md) ·
[English](current/developer-guide/architecture.md)。

## 仓库链接

- 项目根目录：[../README.md](../README.md)
- `current/` 文档的源码依据：`main` 分支，版本 `0.0.1-SNAPSHOT`（生成时已核实）。

## English

[English entry point](README.md)
