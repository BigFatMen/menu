package me.missionary.menu;

import me.missionary.menu.button.Button;
import me.missionary.menu.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 2/21/2018
 */
public class ExampleUsage extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
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

        private final ItemStack FILLER_MATERIAL = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 5).toItemStack();

        public void doSomething(Player player) {
            Menu menu = new Menu("Example Menu", 4);
            menu.setItem(0, new Button(true, new ItemBuilder(Material.STICK).setName(ChatColor.LIGHT_PURPLE + "Stick").toItemStack(), (player1, menu1) -> {
               player1.sendMessage("Hey! You clicked the stick.");
            }));
            menu.fill(new Button(false, FILLER_MATERIAL)); // Fills the rest of the menu w/ the FILLER_MATERIAL, important that this gets called after population.
            menu.setStatic(true); // Make this menu never unregister, allows for re-use. default is false
            menu.setCloseHandler((player1, menu1) -> player1.sendMessage("Wow! You closed the inventory named " + menu1.getTitle() + " w/ a size of " + menu1.getSize()));
            menu.show(player);
        }
    }
}
