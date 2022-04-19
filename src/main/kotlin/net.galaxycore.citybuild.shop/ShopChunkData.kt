package net.galaxycore.citybuild.shop

import lombok.Getter
import org.bukkit.Chunk
import java.io.*

@Getter
class ShopChunkData(private val file: File, private val chunk: Chunk) {
    var shopsInThisChunk: List<Shop> = ArrayList()
    fun load() {
        try {
            val fileIn = FileInputStream(file)
            val inStream = ObjectInputStream(fileIn)
            @Suppress("UNCHECKED_CAST")
            shopsInThisChunk = inStream.readObject() as List<Shop>
            inStream.close()
            fileIn.close()
        } catch (i: IOException) {
            i.printStackTrace()
        } catch (i: ClassNotFoundException) {
            i.printStackTrace()
        }
    }

    fun save() {
        if (file.exists()) {
            file.delete()
        }
        try {
            file.createNewFile()
            val fileOut = FileOutputStream(file)
            val out = ObjectOutputStream(fileOut)
            out.writeObject(shopsInThisChunk)
            out.close()
            fileOut.close()
        } catch (i: IOException) {
            i.printStackTrace()
        }
    }
}