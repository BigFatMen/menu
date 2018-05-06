package me.missionary.menu;

import me.missionary.menu.button.Button;
import me.missionary.menu.button.ClickAction;
import me.missionary.menu.type.BukkitInventoryHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 2/21/2018
 */
public class MenuListener implements Listener {

    private static MenuListener MENU_LISTENER;

    private MenuListener() {
    }

    public static MenuListener getInstance() { // Singleton pattern for this ensuring that there is only 1 instance.
        if (MENU_LISTENER == null) {
            MENU_LISTENER = new MenuListener();
        }
        return MENU_LISTENER;
    }


    @EventHandler(priority = EventPriority.LOWEST)
    private void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        final InventoryView inventoryView = event.getView();
        final Inventory inventory = inventoryView.getTopInventory();

        if (inventory.getHolder() instanceof BukkitInventoryHolder) {
            Menu menu = ((BukkitInventoryHolder) inventory.getHolder()).getMenu();

            if (menu != null) {
                final ItemStack stack = event.getCurrentItem();
                if ((stack == null || stack.getType() == Material.AIR)) {
                    return;
                }

                int slot = event.getSlot();
                if (slot >= 0 && slot <= menu.getMenuDimension().getSize()) {

                    Optional<Button> buttonOptional = menu.getButtonByIndex(slot);

                    buttonOptional.ifPresent(button -> {

                        if (button.getConsumer() == null) { // Allows for Buttons to not have an action.
                            return;
                        }

                        button.getConsumer().accept((Player) event.getWhoClicked(), new ClickAction.ButtonClickTypePair(button, event.getClick()));

                        if (!button.isMoveable()) {
                            event.setResult(Event.Result.DENY);
                            event.setCancelled(true);
                        }
                    });
                }
            }
        }
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;

        final InventoryView inventoryView = event.getView();
        final Inventory inventory = inventoryView.getTopInventory();

        if (inventory.getHolder() instanceof BukkitInventoryHolder) {
            Menu menu = ((BukkitInventoryHolder) inventory.getHolder()).getMenu();

            if (menu != null) {
                menu.handleClose((Player) event.getPlayer());
            }
        }
    }
}
