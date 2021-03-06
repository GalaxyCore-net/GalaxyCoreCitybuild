package net.galaxycore.citybuild.bb

import net.galaxycore.citybuild.Essential
import net.galaxycore.galaxycorecore.spice.KBlockData
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.lang.reflect.Method
import java.util.BitSet

class BBCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // Require three int and two str args
        if (args.size == 2 || args.size == 3) {
            // Get the player
            val player = Bukkit.getPlayer(args[0].substring(1))!!
            val pdc: PersistentDataContainer = player.persistentDataContainer

            // Get the key
            val key = NamespacedKey(Essential.getInstance(), args[1])

            val value = pdc.get(key, PersistentDataType.INTEGER)

            player.sendMessage("§eValue: §7$value")

            if (args.size == 3) {
                val newValue = args[2].toInt()
                pdc.set(key, PersistentDataType.INTEGER, newValue)
                player.sendMessage("§eNew value: §7$newValue")
            }

            return true;
        }

        if (args.size != 5 && args.size != 6 && args.size != 7 && args.size != 9) {
            sender.sendMessage("§cUsage: /bb <x> <y> <z> <data> <datatype> [deserializer] [mod|clear] [and|or] [value] or /bb @<Player> <Key> [Int to set]")
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

        if (args.size == 7) {
            if (args[6] == "clear") {
                pdc.remove(NamespacedKey(Essential.getInstance(), data))
                sender.sendMessage("§aCleared data")
            }
        }

        if (args.size == 9) {
            if (args[6] == "mod") {
                when (args[7]) {
                    "and" -> {
                        if (datatype != PersistentDataType.BYTE_ARRAY) {
                            sender.sendMessage("§cModifying data is only supported for byte arrays")
                            return true
                        }
                        val rawValue = rawData as ByteArray
                        val toEdit = args[8].split("shr")
                        val editablelong = toEdit[0].toLong()
                        val editable = ByteArray(Long.SIZE_BYTES)
                        for (i in 0 until Long.SIZE_BYTES) {
                            editable[i] = editablelong.toByte()
                        }

                        val editableByteArray = ByteArray(rawValue.size)

                        if (toEdit.size == 2) {
                            val shift = toEdit[1].toInt()
                            System.arraycopy(editable, 0, editableByteArray, shift, editable.size)
                        }
                        val a: BitSet = BitSet.valueOf(rawValue)
                        val b: BitSet = BitSet.valueOf(editable)
                        a.and(b)
                        pdc[NamespacedKey(Essential.getInstance(), data), PersistentDataType.BYTE_ARRAY] = a.toByteArray()
                    }
                    "or" -> {
                        if (datatype != PersistentDataType.BYTE_ARRAY) {
                            sender.sendMessage("§cModifying data is only supported for byte arrays")
                            return true
                        }
                        val rawValue = rawData as ByteArray
                        val toEdit = args[8].split("shr")
                        val editablelong = toEdit[0].toLong()
                        val editable = ByteArray(Long.SIZE_BYTES)
                        for (i in 0 until Long.SIZE_BYTES) {
                            editable[i] = editablelong.toByte()
                        }

                        val editableByteArray = ByteArray(rawValue.size)

                        if (toEdit.size == 2) {
                            val shift = toEdit[1].toInt()
                            System.arraycopy(editable, 0, editableByteArray, shift, editable.size)
                        }
                        val a: BitSet = BitSet.valueOf(rawValue)
                        val b: BitSet = BitSet.valueOf(editable)
                        a.or(b)
                        pdc[NamespacedKey(Essential.getInstance(), data), PersistentDataType.BYTE_ARRAY] = a.toByteArray()
                    }
                    "set" -> {
                        when (stringDatatype) {
                            "byte" -> {
                                pdc[NamespacedKey(Essential.getInstance(), data), PersistentDataType.BYTE] = getInt(args[8]).toByte()
                            }
                            "short" -> {
                                pdc[NamespacedKey(Essential.getInstance(), data), PersistentDataType.SHORT] = getInt(args[8]).toShort()
                            }
                            "int" -> {
                                pdc[NamespacedKey(Essential.getInstance(), data), PersistentDataType.INTEGER] = getInt(args[8])

                            }
                            "long" -> {
                                pdc[NamespacedKey(Essential.getInstance(), data), PersistentDataType.LONG] = getInt(args[8]).toLong()

                            }
                            "float" -> {
                                pdc[NamespacedKey(Essential.getInstance(), data), PersistentDataType.FLOAT] = args[8].toFloat()
                            }
                            "double" -> {
                                pdc[NamespacedKey(Essential.getInstance(), data), PersistentDataType.DOUBLE] = getInt(args[8]).toDouble()
                            }
                            "string" -> {
                                pdc[NamespacedKey(Essential.getInstance(), data), PersistentDataType.STRING] = args[8]
                            }
                            "bytearray" -> {
                                pdc[NamespacedKey(Essential.getInstance(), data), PersistentDataType.BYTE_ARRAY] = args[8].split(",").map { it.toInt().toByte() }.toTypedArray().toByteArray()
                            }
                            else -> {
                                sender.sendMessage("§cCan't set data of type $stringDatatype")
                                return true
                            }
                        }
                    }
                    else -> {
                        sender.sendMessage("§cInvalid operator: ${args[8]}")
                        return true
                    }
                }


                sender.sendMessage("§aSet!")
            }
        }

        try {
            val result = method.invoke(null, rawData)
            sender.sendMessage("§aResult: $result")
        } catch (e: Exception) {
            if (e.message == "null") {
                sender.sendMessage("§aResult: Data is null")
            } else {
                sender.sendMessage("§cFailed to deserialize: ${e.message}")
            }
        }
        return true
    }

    private fun getInt(string: String): Int {
        return if (string.startsWith("0x")) {
            Integer.parseInt(string.substring(2), 16)
        } else if (string.startsWith("0b")) {
            Integer.parseInt(string.substring(2), 2)
        } else if (string.startsWith("0o")) {
            Integer.parseInt(string.substring(2), 8)
        } else {
            Integer.parseInt(string)
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        // Complete the first arg to the x-coordinate the sender is looking at
        val player = sender as Player

        if (args.isNotEmpty() && args[0].startsWith("@")) {
            if (args.size == 1) {
                // Any player
                return Bukkit.getOnlinePlayers().map { it.name }.filter { it.startsWith(args[0]) }.toMutableList()
            }

            if (args.size == 2) {
                return mutableListOf("shops")
            }

            return mutableListOf()
        }

        if (args.size == 1) {
            return mutableListOf(player.getTargetBlock(12)?.x.toString()).filter { it.startsWith(args[0]) }.toMutableList()
        }

        if (args.size == 2) {
            return mutableListOf(player.getTargetBlock(12)?.y.toString()).filter { it.startsWith(args[1]) }.toMutableList()
        }

        if (args.size == 3) {
            return mutableListOf(player.getTargetBlock(12)?.z.toString()).filter { it.startsWith(args[2]) }.toMutableList()
        }

        if (args.size == 4) {
            val possibilities = mutableListOf(
                "shop_player",
                "shop_price",
                "shop_itemstack",
                "shop_len",
                "shop_cx",
                "shop_cy",
                "shop_cz",
                "shop_state"
            )
            return possibilities.filter { it.startsWith(args[3]) }.toMutableList()
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
                "net.galaxycore.citybuild.bb.BasicDeserializer"
            )
            return possibilities.filter { it.startsWith(args[5]) }.toMutableList()
        }

        if (args.size == 7) {
            val possibilities = mutableListOf(
                "clear",
                "mod"
            )
            return possibilities.filter { it.startsWith(args[6]) }.toMutableList()
        }

        if (args.size == 8) {
            val possibilities = mutableListOf(
                "and",
                "or",
                "set"
            )
            return possibilities.filter { it.startsWith(args[7]) }.toMutableList()
        }

        return mutableListOf()
    }


}