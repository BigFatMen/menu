package me.missionary.menu;

import me.missionary.menu.button.Button;
import me.missionary.menu.button.ClickAction;
import me.missionary.menu.type.BukkitInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 2/21/2018
 */
public class MenuHandler implements Listener {

    public static final Map<UUID, Menu> OPEN_MENUS = new HashMap<>();

    public MenuHandler(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getScheduler().runTaskTimer(plugin, new MenuUpdateTask(), 20L, 20L);
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

                        button.onClick((Player) event.getWhoClicked(), new ClickAction.InformationPair(button, event.getClick(), menu));

                        if (!button.isMovable()) {
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
                menu.getParent().ifPresent(parent -> parent.showMenu((Player) event.getPlayer()));
            }
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        OPEN_MENUS.remove(event.getPlayer().getUniqueId());
    }

    private class MenuUpdateTask implements Runnable {

        @Override
        public void run() {
            for (Map.Entry<UUID, Menu> entry : OPEN_MENUS.entrySet()) {
                UUID uuid = entry.getKey();
                Menu menu = entry.getValue();
                Reference<Player> player = new SoftReference<>(Bukkit.getPlayer(uuid));

                if (player.get() != null) {
                    if (menu.isAutoUpdate()) {
                        menu.showMenu(player.get(), true);
                    }
                    player.clear();
                }
            }
        }
    }
}
