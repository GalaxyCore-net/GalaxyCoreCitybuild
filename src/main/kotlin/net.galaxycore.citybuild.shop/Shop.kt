package net.galaxycore.citybuild.shop

import java.io.Serializable

data class Shop(var player: Int, var price: Long, var itemStack: Map<String, Any>, var len: Int, var cx: Int, var cy: Int, var cz: Int) : Serializable
