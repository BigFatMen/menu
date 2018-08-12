package me.missionary.menu.example;

import me.missionary.menu.Menu;
import me.missionary.menu.MenuListener;
import me.missionary.menu.button.Button;
import me.missionary.menu.mask.Mask2D;
import me.missionary.menu.type.impl.ChestMenu;
import me.missionary.menu.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 2/21/2018
 */
public class ExampleUsage extends JavaPlugin implements CommandExecutor {

    private static final ItemStack MASK_FILLER = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 8).setName(ChatColor.WHITE + "").toItemStack();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(MenuListener.getInstance(), this); // No need for 'new' keyword because MenuListener follows Singleton
        getServer().getPluginCommand("test").setExecutor(this);
    }

    @Override
    public void onDisable() {
        // Nothing to handle here.
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                new MenuImpl().createMenu((Player) commandSender);
            } else {
                new MenuImpl().createMaskedMenu((Player) commandSender);
            }
        }
        return true;
    }


    public class MenuImpl {

        public void createMenu(Player player) {
            Menu menu = new ChestMenu("Menu", 4);
            menu.setItem(12, new Button(true, new ItemBuilder(Material.STICK).setName(ChatColor.LIGHT_PURPLE + "Stick").toItemStack(), (player1, pair) -> {
                player1.sendMessage("You have clicked the Stick."); // Java 8 Functional Style
            }));
            menu.setItem(13, new Button(true, new ItemBuilder(Material.ACACIA_DOOR).setName("Door").toItemStack(), (player1, pair) -> {
                if (pair.getClickType().isLeftClick()) {
                    player1.sendMessage("You have clicked the " + pair.getButton().getStack().getItemMeta().getDisplayName());
                }
            }));
            menu.setCloseHandler((player1, menu1) -> player1.sendMessage("Wow! You closed the inventory."));
            menu.showMenu(player);
        }

        public void createMaskedMenu(Player player) {
            Menu menu = new ChestMenu("Masked Menu", 3);
            new Mask2D()
                    .setButton('0', new Button(false, MASK_FILLER))
                    .setButton('1', new Button(false, new ItemBuilder(Material.IRON_DOOR).toItemStack(), (player1, buttonClickTypeButtonClickTypePair) -> {
                        player1.sendMessage("WOW! You have clicked an Iron Door!!");
                    }))
                    .setMaskPattern(
                            "000000000",
                            "000010000",
                            "000000000")
                    .applyTo(menu);
            menu.showMenu(player);
        }

        public void createRelationshipMenu(Player player) {
            Menu parent = new ChestMenu("Parent Menu", 2);
            parent.setItem(3, new Button(false, new ItemBuilder(Material.GLASS).toItemStack()));
            Menu child = new ChestMenu("Child", 1);
            child.setItem(3, new Button(false, new ItemBuilder(Material.GLASS_BOTTLE).toItemStack()));
            ((ChestMenu) child).setParent(parent);
        }
    }
}
