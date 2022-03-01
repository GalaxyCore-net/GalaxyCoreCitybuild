package net.galaxycore.citybuild.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackUtils {
    public static ItemStack glimmerIf(ItemStack source, boolean condition) {
        if(!condition) return source;

        ItemMeta meta = source.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);

        source.setItemMeta(meta);

        return source;
    }
}
