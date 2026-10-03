package io.github.moriskakitsu.hoowo.data.favorite

import io.github.moriskakitsu.hoowo.data.db.entity.FavoriteEntity
import io.github.moriskakitsu.hoowo.data.model.FavoriteType

interface FavoriteAdapter<T> {
    val type: FavoriteType

    fun buildRefKey(target: T): String

    fun buildFavoriteEntity(
        target: T,
        existing: FavoriteEntity? = null,
        now: Long = System.currentTimeMillis()
    ): FavoriteEntity
}
