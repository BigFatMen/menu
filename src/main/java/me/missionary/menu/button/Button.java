package me.missionary.menu.button;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 10/3/2018
 */
public abstract class Button {

    /**
     * This will make the item a 'placeholder' item with no mobility or click action.
     *
     * @param stack The {@link ItemStack} to be used
     * @return The 'placeholder' {@link Button}
     */
    public static Button placeholder(ItemStack stack) {
        return new Button() {
            @Override
            public ItemStack getButton(Player player) {
                return stack;
            }
        };
    }

    public abstract ItemStack getButton(Player player);

    public void onClick(Player player, ClickAction.InformationPair clickAction) {
    }

    public boolean isMovable() {
        return false;
    }
}
