package net.galaxycore.citybuild.bb

import net.galaxycore.citybuild.shop.Shop

class ShopDeserializer {
    companion object {
        @JvmStatic
        fun disect(any: Any): String {
            val byteArray = any as ByteArray
            return Shop.disect(byteArray).toString()
        }
    }
}