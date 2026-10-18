# User Guide — microsphere-security

> **Version-line note:** This repository currently has no `release` or `release-1.x` branch. The `current` documentation line is generated from the `main` branch (revision `0.0.1-SNAPSHOT`, pre-1.0, nothing released yet). `@since 1.0.0` javadoc tags are forward promises, not released versions. Artifacts are available only as snapshots from the Sonatype OSS snapshot repository (`https://s01.oss.sonatype.org/content/repositories/snapshots`), not from Maven Central.

## What this library is

`microsphere-security` is a small, additive security layer for Spring Boot servlet web
applications. It does not replace Spring Security; instead it configures and composes
the HTTP response headers and browser-protection mechanisms that a web application
normally has to wire up by hand:

- `Strict-Transport-Security` (HSTS)
- `X-XSS-Protection`
- `X-Content-Type-Options: nosniff`
- `X-Frame-Options` (anti-clickjacking)
- `Content-Security-Policy` / `Content-Security-Policy-Report-Only`
- CORS, including preflight handling

Everything is driven by configuration properties under the `microsphere.security.`
prefix, and everything is **off unless you turn it on** — see
[microsphere-security-spring-boot](microsphere-security-spring-boot.md) for the exact
switch semantics.

The reactor is a Maven multi-module project whose root coordinates are
`io.github.microsphere-projects:microsphere-security:${revision}`, with `${revision}`
defined as `0.0.1-SNAPSHOT` in the root `pom.xml`.

## Module map

| Module | Packaging | What it gives you | Documentation |
|---|---|---|---|
| `microsphere-security-core` | `jar` | The `SecurityConstants` interface — the property-name prefixes shared by every other module. No Spring dependency. | [core](microsphere-security-core.md) |
| `microsphere-security-parent` | `pom` | Conventions parent used by all modules; imports the external `microsphere-spring-cloud-dependencies` BOM so third-party versions are inherited, not declared. | [parent](microsphere-security-parent.md) |
| `microsphere-security-dependencies` | `pom` (BOM) | `dependencyManagement` entries for this project's own artifacts, so consumers do not repeat versions. | [dependencies](microsphere-security-dependencies.md) |
| `microsphere-security-spring-boot` | `jar` | The user-facing module: auto-configuration, `WebSecurityProperties`, conditions, filters and the `WebSecurityProcessor` SPI. | [spring-boot](microsphere-security-spring-boot.md) |

## Quick start

### 1. Add the snapshot repository

No artifact is published to Maven Central yet, so the snapshot repository must be
reachable:

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

### 2. Declare the dependency

There is no starter artifact in this reactor yet — the module that ships the
auto-configuration is named `microsphere-security-spring-boot`. Use that exact
`artifactId`:

```xml
<dependency>
    <groupId>io.github.microsphere-projects</groupId>
    <artifactId>microsphere-security-spring-boot</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

Alternatively, import the BOM and drop the `<version>`:

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

Read [microsphere-security-dependencies](microsphere-security-dependencies.md) before
you rely on the BOM: it manages one artifact name that this reactor does not build yet.

### 3. Turn web security on

Registration happens through `META-INF/spring.factories`, so no `@Import` or
`@Enable*` annotation is needed. Two properties decide everything:

```properties
microsphere.security.enabled=true
microsphere.security.web.enabled=true
```

`microsphere.security.enabled` is already effective when absent (`matchIfMissing = true`);
`microsphere.security.web.enabled` is **not** — the web layer stays completely inert until
you set it to `true`. Each individual protection (HSTS, XSS, X-Frame-Options,
X-Content-Type-Options, CSP, CORS) then has its own `enabled` flag, which also defaults to
`false`.

### 4. Minimum useful configuration

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

## How to read these docs

- Every page ends with a link to its Chinese mirror (`*.zh.md`) sitting next to it; the
  Chinese page links back.
- Examples in these guides are taken from each module's own `src/test/java` sources. The
  repository has **no** `test-cases` directory, so there is no separate example project to
  check out.
- Where a fact is not verifiable in this repository (for example the JDK baseline or the
  Spring Boot version), the page says which external POM it is inherited from instead of
  guessing a number.
- The `current` line always mirrors the `main` branch; nothing here describes a released
  artifact.

## Where else to look

- [Developer Guide](../developer-guide/README.md) — build, module internals, conventions.
- [Docs home](../../README.md) — all documentation lines.
- [Repository README](../../../README.md) — project overview and license.

## 中文

[简体中文](README.zh.md)
