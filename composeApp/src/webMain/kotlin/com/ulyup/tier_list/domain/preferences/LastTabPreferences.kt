package com.ulyup.tier_list.domain.preferences

interface LastTabPreferences {
    fun save(graphName: String)
    fun load(): String?
    fun clear()
}
