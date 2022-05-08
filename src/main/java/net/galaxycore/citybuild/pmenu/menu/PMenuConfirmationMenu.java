package net.galaxycore.citybuild.pmenu.menu;

import lombok.Builder;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.galaxycorecore.spice.KMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class PMenuConfirmationMenu extends KMenu {
    private final Player player;
    private final Consumer<Boolean> callback;

    public PMenuConfirmationMenu(Player player, Consumer<Boolean> callback) {
        this.player = player;
        this.callback = callback;

        item(12, Material.LIME_STAINED_GLASS, PMenuI18N.CONTINUE_ANYWAY.get(player)).then((event) -> {
            player.closeInventory();
            callback.accept(true);
            return null;
        });
        item(15, Material.RED_STAINED_GLASS, PMenuI18N.CANCEL.get(player)).then((event) -> {
            player.closeInventory();
            callback.accept(false);
                    return null;
                });
    }

    @NotNull
    @Override
    public String getNameI18NKey() {
        return PMenuI18N.ACTION_CANNOT_BE_UNDONE.getKey();
    }

    @Override
    public void onclose(@NotNull InventoryCloseEvent inventoryCloseEvent) {
        callback.accept(false);
    }

    public void open() {
        open(player);
    }

    @Override
    public int getSize() {
        return 3*9;
    }
}
