package me.missionary.menu;

import me.missionary.menu.button.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 2/21/2018
 */
public class MenuListener implements Listener {

    private static MenuListener MENU_LISTENER;
    private final List<Menu> menuListeners = new ArrayList<>();

    public MenuListener() {
    }

    public static MenuListener getInstance() { // Singleton pattern for this ensuring that there is only 1 instance.
        if (MENU_LISTENER == null) {
            MENU_LISTENER = new MenuListener();
        }
        return MENU_LISTENER;
    }

    void registerMenuListener(Menu menu) {
        menuListeners.add(menu);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        final Inventory inventory = event.getInventory();

        Menu menu = null;
        for (Menu menuListener : menuListeners) {
            if (menuListener.getCraftBukkitInventory().equals(inventory)) {
                // This is our menu.
                menu = menuListener;
                break;
            }
        }

        if (menu != null) {
            final ItemStack stack = event.getCurrentItem();
            if ((stack == null || stack.getType() == Material.AIR)) {
                return;
            }

            int slot = event.getSlot();
            if (slot >= 0 && slot <= (menu.getSize() * 9)) {

                Button button = menu.getButtonByIndex(slot);

                if (button == null) {
                    return;
                }

                if (button.getConsumer() == null) { // Allows for Buttons to not have an action.
                    return;
                }

                button.getConsumer().accept((Player) event.getWhoClicked(), button);

                if (!button.isMoveable()) {
                    event.setResult(Event.Result.DENY);
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;

        final Inventory inventory = event.getInventory();
        Menu menu = null;
        for (Menu menuListener : menuListeners) {
            if (menuListener.getCraftBukkitInventory().equals(inventory)) {
                // This is our menu.
                menu = menuListener;
                break;
            }
        }

        if (menu != null) {
            if (menu.getCloseHandler() != null) {
                menu.getCloseHandler().accept((Player) event.getPlayer(), menu);
            }

            if (!menu.isStatic()) {
                menuListeners.remove(menu);
            }
        }
    }
}
