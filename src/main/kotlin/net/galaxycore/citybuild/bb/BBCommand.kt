package net.galaxycore.citybuild.bb

import net.galaxycore.citybuild.Essential
import net.galaxycore.galaxycorecore.spice.KBlockData
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.lang.reflect.Method

class BBCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // Require three int and two str args
        if (args.size != 5 && args.size != 6) {
            sender.sendMessage("§cUsage: /bb <x> <y> <z> <data> <datatype> [deserializer]")
            return true
        }

        // Parse the args
        val x = args[0].toInt()
        val y = args[1].toInt()
        val z = args[2].toInt()
        val data = args[3]
        val stringDatatype = args[4]
        val deserializer = if (args.size == 6) args[5] else "net.galaxycore.citybuild.bb.BasicDeserializer"

        // Parse the datatype
        val datatype = when (stringDatatype) {
            "byte" -> PersistentDataType.BYTE
            "short" -> PersistentDataType.SHORT
            "int" -> PersistentDataType.INTEGER
            "long" -> PersistentDataType.LONG
            "float" -> PersistentDataType.FLOAT
            "double" -> PersistentDataType.DOUBLE
            "string" -> PersistentDataType.STRING
            "bytearray" -> PersistentDataType.BYTE_ARRAY
            else -> {
                sender.sendMessage("§cInvalid datatype: $stringDatatype")
                return true
            }
        }

        // Get the block
        val block = (sender as Player).world.getBlockAt(x, y, z)

        // Check if the block is in the city
        val pdc = KBlockData(block, Essential.getInstance())
        val rawData = pdc[NamespacedKey(Essential.getInstance(), data), datatype]

        // Parse the deserializer
        val deserializerClass = Class.forName(deserializer)
        val method: Method
        try {
            method = deserializerClass.getMethod("disect", Any::class.java)
        } catch (e: NoSuchMethodException) {
            sender.sendMessage("§cInvalid deserializer: $deserializer")
            return true
        }

        try {
            val result = method.invoke(null, rawData)
            sender.sendMessage("§aResult: $result")
        } catch (e: Exception) {
            sender.sendMessage("§cFailed to deserialize: ${e.message}")
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        // Complete the first arg to the x-coordinate the sender is looking at
        val player = sender as Player
        if (args.size == 1) {
            return mutableListOf(player.getTargetBlock(12)?.x.toString()).filter { it.startsWith(args[0]) }.toMutableList()
        }

        if (args.size == 2) {
            return mutableListOf(player.getTargetBlock(12)?.y.toString()).filter { it.startsWith(args[1]) }.toMutableList()
        }

        if (args.size == 3) {
            return mutableListOf(player.getTargetBlock(12)?.z.toString()).filter { it.startsWith(args[2]) }.toMutableList()
        }


        if (args.size == 5) {
            val possibilities = mutableListOf(
                "byte",
                "short",
                "int",
                "long",
                "float",
                "double",
                "string",
                "bytearray"
            )
            return possibilities.filter { it.startsWith(args[4]) }.toMutableList()
        }

        if (args.size == 6) {
            val possibilities = mutableListOf(
                "net.galaxycore.citybuild.bb.BasicDeserializer",
                "net.galaxycore.citybuild.bb.ShopDeserializer"
            )
            return possibilities.filter { it.startsWith(args[5]) }.toMutableList()
        }
        return mutableListOf()
    }


}