# microsphere-security-dependencies

> **Version-line note:** This repository currently has no `release` or `release-1.x` branch. The `current` documentation line is generated from the `main` branch (revision `0.0.1-SNAPSHOT`, pre-1.0, nothing released yet). `@since 1.0.0` javadoc tags are forward promises, not released versions. Artifacts are available only as snapshots from the Sonatype OSS snapshot repository (`https://s01.oss.sonatype.org/content/repositories/snapshots`), not from Maven Central.

## Overview

`microsphere-security-dependencies` is the project's own bill of materials. It is a
`pom`-packaging module whose only content is a `<dependencyManagement>` block listing the
artifacts this project produces, so that consumers can add them without repeating a version
string in every `<dependency>`.

It is deliberately thin: two managed entries, no properties, no plugins, no build section.
For the third-party versions (Spring Boot, Servlet API, commons, JUnit) you have to look at the
external BOM that this module's parent imports — see
[microsphere-security-parent](microsphere-security-parent.md).

File: `microsphere-security-dependencies/pom.xml` (38 lines).

## Maven coordinates

| Field | Value |
|---|---|
| groupId | `io.github.microsphere-projects` |
| artifactId | `microsphere-security-dependencies` |
| version | `${revision}` (resolved to `0.0.1-SNAPSHOT`) |
| packaging | `pom` |
| parent | `io.github.microsphere-projects:microsphere-security-parent:${revision}` |
| name | `Microsphere :: Security :: Dependencies` |

## POM explained

### The managed coordinates

Exactly two entries, both versioned `${revision}`:

| Managed `artifactId` | `groupId` | Version expression | Resolved version | Built by this reactor? |
|---|---|---|---|---|
| `microsphere-security-core` | `io.github.microsphere-projects` | `${revision}` | `0.0.1-SNAPSHOT` | yes |
| `microsphere-security-spring-boot-starter` | `io.github.microsphere-projects` | `${revision}` | `0.0.1-SNAPSHOT` | **no** |

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

### The starter-name discrepancy (read this before using the BOM)

The BOM manages `microsphere-security-spring-boot-starter`. The module that actually builds the
Spring Boot integration is named `microsphere-security-spring-boot` (see
`microsphere-security-spring-boot/pom.xml`, whose `<artifactId>` has no `-starter` suffix), and
the root POM's `<modules>` list contains only those four modules.

So today:

- `microsphere-security-spring-boot-starter` is a **forward-looking placeholder** — an artifact
  name reserved in the BOM that this reactor does not produce yet. Asking Maven for it will fail
  to resolve.
- The BOM does **not** manage `microsphere-security-spring-boot`, the artifact you must actually
  depend on. Importing this BOM therefore does *not* spare you from writing a `<version>` for
  the Spring Boot module.
- Nothing in the repository indicates when or whether the starter module will appear. Do not
  plan around it; check `microsphere-security-dependencies/pom.xml` and the root `<modules>` list
  again on the version line you are using.

### Where this BOM sits in the POM hierarchy

This module inherits from `microsphere-security-parent`, the same parent as the jar modules.
That means when *it* is built, the parent's import of the external
`microsphere-spring-cloud-dependencies` BOM is applied to it. As a consumer, you should read the
two entries above as the content this BOM contributes; if you also need the third-party versions
managed along the parent chain, depend on the parent in your POM or import
`microsphere-spring-cloud-dependencies` yourself.

## Configuration

There is nothing to configure: a BOM has no properties, no code and no runtime. The properties
that control behaviour live in [microsphere-security-spring-boot](microsphere-security-spring-boot.md).

## Code examples

### Importing the BOM in a consumer build

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

The snapshot repository has to be reachable for `0.0.1-SNAPSHOT` to resolve:

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

### Declaring the Spring Boot module today

Because the BOM does not manage it, write the version explicitly:

```xml
<dependency>
    <groupId>io.github.microsphere-projects</groupId>
    <artifactId>microsphere-security-spring-boot</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

Inside the reactor itself, `microsphere-security-spring-boot/pom.xml` does the same thing but
keeps the placeholder: it declares `<version>${revision}</version>` on the
`microsphere-security-core` dependency rather than relying on the BOM.

## Version information

- Version: `${revision}` → `0.0.1-SNAPSHOT`. No release exists, and no `release` branch exists,
  so the BOM is only available as a snapshot.
- Both managed entries inherit the same single revision as the reactor, so the BOM and the jar
  modules always move together.
- The BOM pins **no** third-party version anywhere; every external version is decided by the
  `microsphere-spring-cloud-dependencies` BOM imported by the parent, and ultimately by the
  external `microsphere-build:0.0.21` parent.

## Compatibility

- A `pom` BOM is version-agnostic in itself: it declares coordinates, not bytecode, so it does
  not constrain the JDK or a Spring generation.
- The artifacts it manages come from this reactor, whose code targets the Spring Boot 2.x servlet
  stack (`javax.servlet.*` imports plus `META-INF/spring.factories` registration). Spring Boot
  3.x is not supported on this code line.
- The JDK baseline is inherited from the external parent `microsphere-build:0.0.21` and is not
  declared in this repository.

## Related documents

- [User Guide index](README.md) — back to the guide overview.
- [microsphere-security-core](microsphere-security-core.md) — the artifact the BOM really manages.
- [microsphere-security-spring-boot](microsphere-security-spring-boot.md) — the artifact you need
  to depend on, which the BOM does not manage.
- [microsphere-security-parent](microsphere-security-parent.md) — the parent that supplies all
  third-party versions.
- Developer Guide: [microsphere-security-dependencies](../developer-guide/microsphere-security-dependencies.md)

## 中文

[简体中文](microsphere-security-dependencies.zh.md)
