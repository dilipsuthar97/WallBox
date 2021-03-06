package com.dilipsuthar.wallbox.data_source.model

data class ProfileImage (

/**
    profile_image: {
        small: "https://images.unsplash.com/profile-1532031424191-d9fffab22d78?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32",
        medium: "https://images.unsplash.com/profile-1532031424191-d9fffab22d78?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64",
        large: "https://images.unsplash.com/profile-1532031424191-d9fffab22d78?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128"
    }
 */

    val small: String,
    val medium: String,
    val large: String

) {
    constructor() : this("", "", "")
}