package com.dilipsuthar.wallbox.items

data class Setting(
    var type: Type,
    var title: String = "",
    var subTitle: String = "",
    var content: String = "",
    var footer: String = "",
    var checkState: Int = -1
) {
    enum class Type {
        LANGUAGE,
        THEME,
        CLEAR_CACHE,
        WALLPAPER_PREVIEW_QUALITY,
        WALLPAPER_SAVE_LOCATION,
        RESET_TUTORIAL,
        UNSPLASH_SITE,
        FOOTER
    }
}
