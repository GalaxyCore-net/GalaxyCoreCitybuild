package net.galaxycore.citybuild.shop

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N
import org.bukkit.entity.Player

class ShopI18N {
    companion object {
        private inline fun <reified T> default(key: String, en: String, de: String, isChat: Boolean = false){
            I18N.setDefaultByLang("de_DE", "citybuild.shop.${T::class.simpleName}.$key", de, isChat)
            I18N.setDefaultByLang("en_GB", "citybuild.shop.${T::class.simpleName}.$key", en, isChat)
        }

        inline fun <reified T> get(player: Player, key: String): String {
            return I18N.getS(player, "citybuild.shop.${T::class.simpleName}.$key")
        }

        fun registerDefaults() {
            default<ShopListener>( "shopalreadyinarea", "§cThere is already a shop in this area!", "§cEs gibt bereits einen Shop in diesem Bereich!", true)
            default<ShopListener>( "needtoholdanitem", "§cYou need to hold an item to create a shop!", "§cDu musst ein Item halten um einen Shop zu erstellen!", true)
            default<ShopListener>( "sneakandinteracttocreateashop", "§aSneak and interact with a chest to create a shop!", "§aSchleiche und interagiere mit einer Kiste um einen Shop zu erstellen!", false)
            default<ShopCreateGUI>( "title", "§eCreate a shop", "§eErstelle einen Shop",false)
            default<ShopCreateGUI>( "create", "§aCreate shop", "§aErstelle den Shop",false)
            default<ShopCreateGUI>( "cancel", "§cCancel", "§cAbbrechen",false)
            default<ShopCreateGUI>( "buy", "§aBuy", "§aKaufen",false)
            default<ShopCreateGUI>( "sell", "§aSell", "§aVerkaufen",false)
            default<ShopCreateGUI>( "buyandsell", "§aBuy and sell", "§aKaufen und verkaufen",false)
            default<ShopCreateGUI>( "price", "§ePrice: %d Coind", "§ePreis: %d Coins",false)
            default<ShopEditGUI>( "buy", "§aBuy", "§aKaufen",false)
            default<ShopEditGUI>( "sell", "§aSell", "§aVerkaufen",false)
            default<ShopEditGUI>( "buyandsell", "§aBuy and sell", "§aKaufen und verkaufen",false)
            default<ShopEditGUI>( "refill", "§aRefill", "§aAuffüllen",false)
             default<ShopEditGUI>( "changeprice", "§aChange price", "§aPreis ändern",false)
             default<ShopEditGUI>( "makeadminshop", "§aMake an admin shop", "§aErstelle einen Admin Shop",false)
             default<ShopEditGUI>( "title", "§eEdit Shop", "§eBearbeite den Shop",false)
            default<ShopGUI>( "title", "Buy %s", "Kaufe %s",false)
            default<ShopGUI>( "sell", "§eSell", "§eVerkaufen",false)
            default<ShopGUI>( "buy", "§eBuy", "§eKaufen",false)
            default<ShopGUI>( "settings", "§eSettings", "§eEinstellungen",false)
        }
    }
}