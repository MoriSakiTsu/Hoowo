package io.github.moriskakitsu.highlight.languages

import io.github.moriskakitsu.highlight.core.Language
import io.github.moriskakitsu.highlight.languages.bash.bash
import io.github.moriskakitsu.highlight.languages.c.c
import io.github.moriskakitsu.highlight.languages.cmake.cmake
import io.github.moriskakitsu.highlight.languages.cpp.cpp
import io.github.moriskakitsu.highlight.languages.csharp.csharp
import io.github.moriskakitsu.highlight.languages.css.css
import io.github.moriskakitsu.highlight.languages.dart.dart
import io.github.moriskakitsu.highlight.languages.diff.diff
import io.github.moriskakitsu.highlight.languages.dockerfile.dockerfile
import io.github.moriskakitsu.highlight.languages.go.go
import io.github.moriskakitsu.highlight.languages.glsl.glsl
import io.github.moriskakitsu.highlight.languages.ini.ini
import io.github.moriskakitsu.highlight.languages.java.java
import io.github.moriskakitsu.highlight.languages.javascript.javascript
import io.github.moriskakitsu.highlight.languages.json.json
import io.github.moriskakitsu.highlight.languages.kotlin.kotlin
import io.github.moriskakitsu.highlight.languages.latex.latex
import io.github.moriskakitsu.highlight.languages.lua.lua
import io.github.moriskakitsu.highlight.languages.markdown.markdown
import io.github.moriskakitsu.highlight.languages.php.php
import io.github.moriskakitsu.highlight.languages.powershell.powershell
import io.github.moriskakitsu.highlight.languages.properties.properties
import io.github.moriskakitsu.highlight.languages.python.python
import io.github.moriskakitsu.highlight.languages.rust.rust
import io.github.moriskakitsu.highlight.languages.ruby.ruby
import io.github.moriskakitsu.highlight.languages.sql.sql
import io.github.moriskakitsu.highlight.languages.swift.swift
import io.github.moriskakitsu.highlight.languages.typescript.typescript
import io.github.moriskakitsu.highlight.languages.xml.xml
import io.github.moriskakitsu.highlight.languages.yaml.yaml

/**
 * Every grammar bundled with the highlighter.
 *
 * Each entry builds a fresh mode tree: compilation mutates modes in place, mirroring `highlight.js`.
 */
internal fun builtinLanguages(): List<Language> = listOf(
    json(),
    ini(),
    cmake(),
    go(),
    glsl(),
    yaml(),
    bash(),
    dockerfile(),
    javascript(),
    typescript(),
    xml(),
    css(),
    dart(),
    java(),
    kotlin(),
    latex(),
    lua(),
    powershell(),
    properties(),
    python(),
    c(),
    cpp(),
    csharp(),
    sql(),
    diff(),
    markdown(),
    rust(),
    ruby(),
    php(),
    swift(),
)
