# Microsphere Security — Documentation

Documentation for the **Microsphere Security** project, available in two languages:

| Language | Entry point |
| --- | --- |
| English (default) | You are here. |
| 简体中文 | [README.zh.md](README.zh.md) |

> **Version-line note:** This repository currently has **no `release` or `release-1.x` branch**
> (only `main`, and no git tags). The `current/` documentation line is therefore generated from
> the `main` branch at revision `0.0.1-SNAPSHOT` — pre-1.0, nothing released to Maven Central;
> snapshots only (`https://s01.oss.sonatype.org/content/repositories/snapshots`).
> `@since 1.0.0` javadoc tags are forward promises, not released versions. A `1.x/` directory
> does not exist and will appear only when a `release-1.x` branch is created.

## Version lines

| Line | Source branch | Revision | User Guide | Developer Guide |
| --- | --- | --- | --- | --- |
| `current/` | `main` (no `release` branch yet) | `0.0.1-SNAPSHOT` | [current/user-guide/README.md](current/user-guide/README.md) · [中文](current/user-guide/README.zh.md) | [current/developer-guide/README.md](current/developer-guide/README.md) · [中文](current/developer-guide/README.zh.md) |
| `1.x/` | `release-1.x` | — | *not published — branch does not exist* | *not published — branch does not exist* |

## Which guide to read

- **[User Guide](current/user-guide/README.md)** — what each Maven module offers, Maven
  coordinates, the full `microsphere.security.web.*` configuration-property reference,
  runnable code examples taken from the project's own tests, version information and the
  compatibility range (Spring Boot 2.x servlet stack on Tomcat).
- **[Developer Guide](current/developer-guide/README.md)** — architecture design and the module
  dependency diagram, class-by-class source analysis, applied design patterns
  (Composite, Chain of Responsibility, Strategy/SPI, meta-annotation conditions), and mermaid
  flow/sequence diagrams of the auto-configuration gating and request-processing workflows.

## Maven modules documented (one page per module, both guides)

| Module | Packaging | User Guide | Developer Guide |
| --- | --- | --- | --- |
| `microsphere-security-parent` | pom | [doc](current/user-guide/microsphere-security-parent.md) · [中文](current/user-guide/microsphere-security-parent.zh.md) | [doc](current/developer-guide/microsphere-security-parent.md) · [中文](current/developer-guide/microsphere-security-parent.zh.md) |
| `microsphere-security-dependencies` | pom (BOM) | [doc](current/user-guide/microsphere-security-dependencies.md) · [中文](current/user-guide/microsphere-security-dependencies.zh.md) | [doc](current/developer-guide/microsphere-security-dependencies.md) · [中文](current/developer-guide/microsphere-security-dependencies.zh.md) |
| `microsphere-security-core` | jar | [doc](current/user-guide/microsphere-security-core.md) · [中文](current/user-guide/microsphere-security-core.zh.md) | [doc](current/developer-guide/microsphere-security-core.md) · [中文](current/developer-guide/microsphere-security-core.zh.md) |
| `microsphere-security-spring-boot` | jar | [doc](current/user-guide/microsphere-security-spring-boot.md) · [中文](current/user-guide/microsphere-security-spring-boot.zh.md) | [doc](current/developer-guide/microsphere-security-spring-boot.md) · [中文](current/developer-guide/microsphere-security-spring-boot.zh.md) |

Additional developer-guide pages: [architecture.md](current/developer-guide/architecture.md) ·
[中文](current/developer-guide/architecture.zh.md).

## Repository links

- Project root: [../README.md](../README.md)
- Source of `current/` docs: the `main` branch, revision `0.0.1-SNAPSHOT` (verified at generation time).

## 中文

[简体中文入口](README.zh.md)
