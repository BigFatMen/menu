package me.missionary.menu.example;

import me.missionary.menu.Menu;
import me.missionary.menu.MenuListener;
import me.missionary.menu.button.Button;
import me.missionary.menu.mask.Mask;
import me.missionary.menu.mask.Mask2D;
import me.missionary.menu.type.impl.ChestMenu;
import me.missionary.menu.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 2/21/2018
 */
public class ExampleUsage extends JavaPlugin implements CommandExecutor {

    private static final ItemBuilder MASK_FILLER = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 8).setName(ChatColor.WHITE + "");

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
            new MenuImpl().doSomething((Player) commandSender);
        }
        return true;
    }


    public class MenuImpl {

        public void doSomething(Player player) {
            Menu menu = new ChestMenu("Menu", 4);
            menu.setItem(12, new Button(true, new ItemBuilder(Material.STICK).setName(ChatColor.LIGHT_PURPLE + "Stick"), (player1, button) -> {
                player1.sendMessage("You have clicked the Stick."); // Java 8 Functional Style
            }));
            menu.setItem(13, new Button(true, new ItemBuilder(Material.EMERALD).setName(ChatColor.GREEN + "Emerald"), (player1, button) -> {
                player1.sendMessage("Wow! You have clicked the Emerald.");
            }));
            menu.setCloseHandler((player1, menu1) -> player1.sendMessage("Wow! You closed the inventory."));
            menu.showMenu(player);
        }
    }
}
