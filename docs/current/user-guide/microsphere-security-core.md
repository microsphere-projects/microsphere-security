# microsphere-security-core

> **Version-line note:** This repository currently has no `release` or `release-1.x` branch. The `current` documentation line is generated from the `main` branch (revision `0.0.1-SNAPSHOT`, pre-1.0, nothing released yet). `@since 1.0.0` javadoc tags are forward promises, not released versions. Artifacts are available only as snapshots from the Sonatype OSS snapshot repository (`https://s01.oss.sonatype.org/content/repositories/snapshots`), not from Maven Central.

## Overview

`microsphere-security-core` is the foundation module of the project. It contains exactly
one public type, the `io.microsphere.security.constants.SecurityConstants` interface, whose
only job is to define — in one place — the configuration property-name prefixes that the
rest of the modules use as `@ConfigurationProperties(prefix = ...)` and
`@ConditionalOnProperty(prefix = ...)` values.

The module has no Spring dependency at all. It is a plain `jar` that any module or consumer
can add without pulling in a framework. Its value is consistency: instead of scattering the
literal string `"microsphere.security."` across annotations, the constants are derived once
from the shared Microsphere prefix.

Source file: `microsphere-security-core/src/main/java/io/microsphere/security/constants/SecurityConstants.java`

## Maven coordinates

| Field | Value |
|---|---|
| groupId | `io.github.microsphere-projects` |
| artifactId | `microsphere-security-core` |
| version | `${revision}` (resolved to `0.0.1-SNAPSHOT` by the root POM) |
| packaging | `jar` |
| parent | `io.github.microsphere-projects:microsphere-security-parent:${revision}` |

```xml
<dependency>
    <groupId>io.github.microsphere-projects</groupId>
    <artifactId>microsphere-security-core</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

The `<parent>` block above is the module POM as it exists in the reactor; inside this
repository the version is always written as `${revision}` rather than a literal.

## API reference

### Interface `SecurityConstants`

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

(The real file imports `MICROSPHERE_PROPERTY_NAME_PREFIX` and `DOT_CHAR` statically; the
import lines are omitted here for readability.)

| Constant | Expanded value | Trailing `.`? | Used as the prefix for |
|---|---|---|---|
| `PROPERTY_NAME_PREFIX` | `microsphere.security.` | yes | `@ConditionalOnEnabledSecurity` → `microsphere.security.enabled` |
| `WEB_SECURITY_PROPERTY_NAME_PREFIX` | `microsphere.security.web` | **no** | `WebSecurityProperties` and `@ConditionalOnEnabledWebSecurity` |
| `WEB_SECURITY_FILTER_PROPERTY_NAME_PREFIX` | `microsphere.security.web.filter.` | yes | the filter-composite switch, `microsphere.security.web.filter.enabled` |
| `WEB_SECURITY_CSP_PROPERTY_NAME_PREFIX` | `microsphere.security.web.csp.` | yes | CSP settings, `microsphere.security.web.csp.*` |
| `WEB_SECURITY_CORS_PROPERTY_NAME_PREFIX` | `microsphere.security.web.cors.` | yes | CORS settings, `microsphere.security.web.cors.*` |

### Documented quirk: `WEB_SECURITY_PROPERTY_NAME_PREFIX` has no trailing dot

`PROPERTY_NAME_PREFIX` ends with `.` (`microsphere.security.`), but
`WEB_SECURITY_PROPERTY_NAME_PREFIX` is built as `PROPERTY_NAME_PREFIX + "web"` — so it
stops at `microsphere.security.web`. The three derived prefixes then append `DOT_CHAR`
themselves, which is why they do end with a dot.

This asymmetry is intentional in the code and observable in practice:

- `@ConfigurationProperties(prefix = WEB_SECURITY_PROPERTY_NAME_PREFIX)` binds the property
  root `microsphere.security.web`.
- `@ConditionalOnProperty(prefix = WEB_SECURITY_PROPERTY_NAME_PREFIX, name = "enabled")`
  resolves to `microsphere.security.web.enabled`.
- The other prefixes already end with `.`, so `name` is appended directly.

The practical consequence is that `WEB_SECURITY_PROPERTY_NAME_PREFIX` must never be
concatenated with a property name without inserting the dot yourself.

## Configuration

This module reads no configuration and provides no runtime behaviour, so there is nothing
to configure here. What it defines are the *names* of the properties that
[microsphere-security-spring-boot](microsphere-security-spring-boot.md) reads. If you are
looking for the property reference table, that page is the one to read.

## Dependencies

Declared in `microsphere-security-core/pom.xml`, none of them with a version literal:

| Dependency | Scope | Version source |
|---|---|---|
| `io.github.microsphere-projects:microsphere-java-core` | compile (default) | managed externally, through the `microsphere-spring-cloud-dependencies` BOM imported by `microsphere-security-parent` |
| `org.junit.jupiter:junit-jupiter-api` | `test` | managed externally, same chain |

`microsphere-java-core` is not incidental: `SecurityConstants` uses two of its types,
`io.microsphere.constants.PropertyConstants.MICROSPHERE_PROPERTY_NAME_PREFIX` (which supplies
the `microsphere.` root) and `io.microsphere.constants.SymbolConstants.DOT_CHAR`
(which supplies `.`).

## Code example

The module's own test is the shortest complete demonstration of the exact constant values.
It is the authority the table above is derived from — path:
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

Note that the test itself asserts the missing trailing dot on
`WEB_SECURITY_PROPERTY_NAME_PREFIX`, so the quirk is locked in by the build rather than
being an accident.

## Version information

- Current revision: `0.0.1-SNAPSHOT` (property `revision` in the root `pom.xml`).
- There are no released versions of this module and no `release` branch; the artifact only
  exists as a snapshot in the Sonatype OSS snapshot repository.
- The `@since 1.0.0` tag on `SecurityConstants` is a forward-looking promise about a future
  release, not evidence that `1.0.0` exists.
- Because the values are compile-time `String` constants, they are **inlined** into any class
  that references them. Changing a prefix in a later release will not take effect in already
  compiled consumers without recompilation.

## Compatibility

- Pure Java library with no Spring, servlet or logging dependency of its own, so it is
  compatible with anything the JDK allows.
- The Java language level is **inherited from the external parent `microsphere-build:0.0.21`**
  and is not declared anywhere in this repository; the repository's CI workflow
  (`.github/workflows/maven.yml`) builds with a Temurin JDK 8, which is evidence about CI,
  not a baseline declared here.
- The version of `microsphere-java-core` you actually get is decided by the externally
  imported `microsphere-spring-cloud-dependencies` BOM, so it depends on which snapshot of
  that BOM your build resolves.

## Related documents

- [User Guide index](README.md) — back to the guide overview.
- [microsphere-security-spring-boot](microsphere-security-spring-boot.md) — where these
  prefixes are consumed.
- [microsphere-security-parent](microsphere-security-parent.md) — the conventions parent.
- [microsphere-security-dependencies](microsphere-security-dependencies.md) — the BOM that
  manages this artifact.
- Developer Guide: [microsphere-security-core](../developer-guide/microsphere-security-core.md)

## 中文

[简体中文](microsphere-security-core.zh.md)
