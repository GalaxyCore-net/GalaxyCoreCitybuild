package net.galaxycore.citybuild.shop

import org.bukkit.inventory.ItemStack
import java.io.Serializable

data class Shop(var player: Int, var price: Long, var itemStack: ItemStack, var len: Int, var cx: Int, var cy: Int, var cz: Int) : Serializable {
    private fun writeInt(byteArray: ByteArray, offset: Int, value: Int) {
        byteArray[offset] = (value shr 24).toByte()
        byteArray[offset + 1] = (value shr 16).toByte()
        byteArray[offset + 2] = (value shr 8).toByte()
        byteArray[offset + 3] = value.toByte()
    }

    private fun writeLong(byteArray: ByteArray, offset: Int, value: Long) {
        writeInt(byteArray, offset, (value shr 32).toInt())
        writeInt(byteArray, offset + 4, value.toInt())
    }


    fun compact(): ByteArray {
        val itemStackSerialized = itemStack.serializeAsBytes()
        val byteArray = ByteArray(itemStackSerialized.size + 4 + 8 + 4 + 4 + 4 + 4)

        writeInt(byteArray, 0, player)// Write the player (4 Bytes)
        writeLong(byteArray, 4, price)// Write the price (8 Bytes)
        writeInt(byteArray, 12, len)// Write the len (4 Bytes)
        writeInt(byteArray, 16, cx) // Write the cx (4 Bytes)
        writeInt(byteArray, 20, cy)// Write the cy (4 Bytes)
        writeInt(byteArray, 24, cz)// Write the cz (4 Bytes)

        // Write the itemStack (? Bytes, depends on the itemStack, is the rest of the data)
        System.arraycopy(itemStackSerialized, 0, byteArray, 28, itemStackSerialized.size)

        return byteArray
    }

    companion object {
        fun disect(byteArray: ByteArray): Shop {
            val player = readInt(byteArray, 0)
            val price = (readInt(byteArray, 4).toLong() shl 32) + readInt(byteArray, 8).toLong()
            val len = readInt(byteArray, 12)
            val cx = readInt(byteArray, 16)
            val cy = readInt(byteArray, 20)
            val cz = readInt(byteArray, 24)

            val itemStackByteArray = ByteArray(byteArray.size - 28)
            System.arraycopy(byteArray, 28, itemStackByteArray, 0, itemStackByteArray.size)

            val itemStack = ItemStack.deserializeBytes(itemStackByteArray)
            return Shop(player, price, itemStack, len, cx, cy, cz)
        }

        private fun readInt(byteArray: ByteArray, offset: Int): Int {
            return (byteArray[offset].toInt() shl 24) + (byteArray[offset + 1].toInt() shl 16) + (byteArray[offset + 2].toInt() shl 8) + byteArray[offset + 3].toInt()
        }
    }
}
