package com.ulyup.tier_list.domain.preferences

interface LastDetailPreferences {
    fun save(tierListId: Int)
    fun load(): Int?
    fun clear()
}
