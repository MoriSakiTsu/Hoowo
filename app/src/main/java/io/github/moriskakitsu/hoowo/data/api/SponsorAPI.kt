package io.github.moriskakitsu.hoowo.data.api

import io.github.moriskakitsu.hoowo.data.model.Sponsor
import okhttp3.OkHttpClient

// 赞助列表已改为本地空实现，不再请求原作者的赞助服务。
// 若将来要接入自己的赞助后端，把 create() 改回 Retrofit 实现即可。
interface SponsorAPI {
    suspend fun getSponsors(): List<Sponsor>

    companion object {
        fun create(httpClient: OkHttpClient): SponsorAPI {
            return object : SponsorAPI {
                override suspend fun getSponsors(): List<Sponsor> = emptyList()
            }
        }
    }
}
