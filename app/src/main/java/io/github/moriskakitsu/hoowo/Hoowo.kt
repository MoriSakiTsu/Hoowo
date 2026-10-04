package io.github.moriskakitsu.hoowo

/**
 * Hoowo 自身的产品信息, 与上游作者无关。
 *
 * 需要改联系方式、仓库地址或更新源时, 只改这里即可,
 * 更新检查、关于页等位置都从这里取值, 避免多处硬编码。
 */
object Hoowo {
    /** GitHub 仓库, 同时作为更新检查的数据源 */
    const val REPO_OWNER = "MoriSakiTsu"
    const val REPO_NAME = "Hoowo"
    const val REPO_URL = "https://github.com/$REPO_OWNER/$REPO_NAME"

    /** 协议文件地址 */
    const val LICENSE_URL = "$REPO_URL/blob/master/LICENSE"

    /** 开发者联系方式 */
    const val DEVELOPER_QQ = "2928332588"
    const val DEVELOPER_EMAIL = "morisakitsu@163.com"
}
