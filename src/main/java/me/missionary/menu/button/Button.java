package me.missionary.menu.button;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 2/21/2018
 */
@Getter
public class Button {

    private boolean moveable;
    private ItemStack stack;
    private Consumer<InventoryClickEvent> consumer;

    public Button(boolean moveable, @NonNull ItemStack stack, Consumer<InventoryClickEvent> consumer) {
        this.moveable = moveable;
        this.stack = stack;
        this.consumer = consumer;
    }

    public Button(boolean moveable, ItemStack stack) {
        this(moveable, stack, null);
    }
}
