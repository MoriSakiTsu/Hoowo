package io.github.moriskakitsu.hoowo.web.routes

import kotlin.uuid.Uuid
import io.github.moriskakitsu.hoowo.web.BadRequestException

internal fun String?.toUuid(name: String = "id"): Uuid {
    if (this == null) throw BadRequestException("Missing $name")
    return runCatching { Uuid.parse(this) }.getOrNull()
        ?: throw BadRequestException("Invalid $name")
}
