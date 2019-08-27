package com.dilipsuthar.wallbox.data.model

data class PreviewPhoto(

    /**
    {
        id: "dGr6Cwp9qLs",
        urls: {
            raw: "https://images.unsplash.com/photo-1499195231111-6009d0b2f5c9?ixlib=rb-1.2.1",
            full: "https://images.unsplash.com/photo-1499195231111-6009d0b2f5c9?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb",
            regular: "https://images.unsplash.com/photo-1499195231111-6009d0b2f5c9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max",
            small: "https://images.unsplash.com/photo-1499195231111-6009d0b2f5c9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max",
            thumb: "https://images.unsplash.com/photo-1499195231111-6009d0b2f5c9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max"
        }
    },
     */

    val id: String,
    val urls: Urls

)