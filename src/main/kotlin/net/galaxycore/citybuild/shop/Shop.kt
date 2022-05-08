package net.galaxycore.citybuild.shop

import net.galaxycore.citybuild.Essential
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.io.Serializable

data class Shop(var player: Int, var price: Long, var itemStack: ItemStack, var len: Int, var cx: Int, var cy: Int, var cz: Int, var state: Int) : Serializable {
    fun compact(pdc: PersistentDataContainer){
        val itemStackSerialized = itemStack.serializeAsBytes()

        pdc[KEY_PLAYER,    PersistentDataType.INTEGER   ] = player
        pdc[KEY_PRICE,     PersistentDataType.LONG      ] = price
        pdc[KEY_ITEMSTACK, PersistentDataType.BYTE_ARRAY] = itemStackSerialized
        pdc[KEY_LEN,       PersistentDataType.INTEGER   ] = len
        pdc[KEY_CX,        PersistentDataType.INTEGER   ] = cx
        pdc[KEY_CY,        PersistentDataType.INTEGER   ] = cy
        pdc[KEY_CZ,        PersistentDataType.INTEGER   ] = cz
        pdc[KEY_STATE,     PersistentDataType.INTEGER   ] = state
    }

    companion object {
        const val STATE_SELL = 1
        const val STATE_BUY = 2
        const val STATE_BTH = STATE_SELL or STATE_BUY

        val KEY_PLAYER = NamespacedKey(Essential.getInstance(), "shop_player")
        val KEY_PRICE = NamespacedKey(Essential.getInstance(), "shop_price")
        val KEY_ITEMSTACK = NamespacedKey(Essential.getInstance(), "shop_itemstack")
        val KEY_LEN = NamespacedKey(Essential.getInstance(), "shop_len")
        val KEY_CX = NamespacedKey(Essential.getInstance(), "shop_cx")
        val KEY_CY = NamespacedKey(Essential.getInstance(), "shop_cy")
        val KEY_CZ = NamespacedKey(Essential.getInstance(), "shop_cz")
        val KEY_STATE = NamespacedKey(Essential.getInstance(), "shop_state")

        fun disect(pdc: PersistentDataContainer): Shop {

            val player              = pdc[KEY_PLAYER,    PersistentDataType.INTEGER   ]!!
            val price               = pdc[KEY_PRICE,     PersistentDataType.LONG      ]!!
            val itemStackSerialized = pdc[KEY_ITEMSTACK, PersistentDataType.BYTE_ARRAY]!!
            val len                 = pdc[KEY_LEN,       PersistentDataType.INTEGER   ]!!
            val cx                  = pdc[KEY_CX,        PersistentDataType.INTEGER   ]!!
            val cy                  = pdc[KEY_CY,        PersistentDataType.INTEGER   ]!!
            val cz                  = pdc[KEY_CZ,        PersistentDataType.INTEGER   ]!!
            val state               = pdc[KEY_STATE,     PersistentDataType.INTEGER   ]!!

            val itemStack = ItemStack.deserializeBytes(itemStackSerialized)
            return Shop(player, price, itemStack, len, cx, cy, cz, state)
        }
    }
}
