package com.ulyup.tierlist

object Routes {

    private const val API = "/api"

    object Auth {
        private const val BASE = "$API/auth"
        const val REGISTER = "$BASE/register"
        const val LOGIN = "$BASE/login"
        const val LOGOUT = "$BASE/logout"
        const val ME = "$BASE/me"
    }

    object Tierlists {
        const val ROOT = "$API/tierlists"
        const val MINE = "$API/users/me/tierlists"

        fun detail(id: Int): String = "$ROOT/$id"
        fun visibility(id: Int): String = "$ROOT/$id/visibility"
    }

    object Items {
        fun root(tierlistId: Int): String = "$API/tierlists/$tierlistId/items"
        fun byId(tierlistId: Int, itemId: Int): String = "${root(tierlistId)}/$itemId"
        fun move(tierlistId: Int, itemId: Int): String = "${byId(tierlistId, itemId)}/move"
    }

    object Users {
        const val ME = "$API/users/me"
        const val UPGRADE_PREMIUM = "$ME/upgrade-premium"
    }
}