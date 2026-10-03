package io.github.moriskakitsu.hoowo.data.repository

import androidx.paging.PagingSource
import io.github.moriskakitsu.hoowo.data.db.dao.GenMediaDAO
import io.github.moriskakitsu.hoowo.data.db.entity.GenMediaEntity

class GenMediaRepository(private val dao: GenMediaDAO) {
    fun getAllMedia(): PagingSource<Int, GenMediaEntity> = dao.getAll()

    suspend fun insertMedia(media: GenMediaEntity) = dao.insert(media)

    suspend fun deleteMedia(id: Int) = dao.delete(id)
}
