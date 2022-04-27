package net.galaxycore.citybuild.shop

import net.galaxycore.galaxycorecore.coins.CoinDAO

class CoinFakeDAO : CoinDAO(null, null) {
    override fun get() = Long.MAX_VALUE
}