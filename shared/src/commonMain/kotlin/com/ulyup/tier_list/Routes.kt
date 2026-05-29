package com.ulyup.tier_list

object Routes {

    private const val API = "/api"

    object Auth {
        private const val BASE = "$API/auth"
        const val REGISTER = "$BASE/register"
        const val LOGIN = "$BASE/login"
        const val LOGOUT = "$BASE/logout"
    }

    object TierLists {
        const val ROOT = "$API/tier_lists"
        const val MINE = "$API/users/me/tier_lists"

        const val ID_PARAM = "tierListId"
        const val BY_ID = "$ROOT/{$ID_PARAM}"
        const val VISIBILITY = "$BY_ID/visibility"

        fun detail(id: Int): String = "$ROOT/$id"
        fun visibility(id: Int): String = "$ROOT/$id/visibility"
    }

    object Items {
        const val TIER_LIST_ID_PARAM = "tierListId"
        const val ITEM_ID_PARAM = "itemId"

        const val ROOT = "$API/tier_lists/{$TIER_LIST_ID_PARAM}/items"
        const val BATCH = "$ROOT/batch"
        const val BY_ID = "$ROOT/{$ITEM_ID_PARAM}"
        const val MOVE = "$BY_ID/move"

        fun root(tierListId: Int): String = "$API/tier_lists/$tierListId/items"
        fun batch(tierListId: Int): String = "${root(tierListId)}/batch"
        fun byId(tierListId: Int, itemId: Int): String = "${root(tierListId)}/$itemId"
        fun move(tierListId: Int, itemId: Int): String = "${byId(tierListId, itemId)}/move"
    }

    object Users {
        const val ME = "$API/users/me"
        const val UPGRADE_PREMIUM = "$ME/upgrade-premium"
    }
}
