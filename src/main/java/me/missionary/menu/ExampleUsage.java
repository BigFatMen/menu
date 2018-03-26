package me.missionary.menu;

import me.missionary.menu.button.Button;
import me.missionary.menu.mask.IMask;
import me.missionary.menu.mask.Mask2D;
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
            Menu menu = new Menu("Example Menu", 4);
            IMask mask = new Mask2D.Builder(menu.getSize(), 9).apply("111111111") // Fills all the spaces with 1's with something which is applied later
                    .nextRow().apply("100000001")
                    .nextRow().apply("100000001")
                    .nextRow().apply("111111111").build();
            mask.forEach(integer -> menu.setItem(integer, new Button(false, MASK_FILLER))); // Fills all of the 1's with the MASK_FILLER
            menu.setItem(12, new Button(true, new ItemBuilder(Material.STICK).setName(ChatColor.LIGHT_PURPLE + "Stick"), (player1, button) -> {
                player1.sendMessage("You have clicked the Stick."); // Java 8 Functional Style
            }));
            menu.setItem(13, new Button(true, new ItemBuilder(Material.EMERALD).setName(ChatColor.GREEN + "Emerald"), (player1, button) -> {
                player1.sendMessage("Wow! You have clicked the Emerald.");
            }));
            menu.setStatic(true); // Make this menu never unregister, allows for re-use. default is false
            menu.setCloseHandler((player1, menu1) -> player1.sendMessage("Wow! You closed the inventory named " + menu1.getTitle() + " w/ a size of " + menu1.getSize()));
            menu.show(player);
        }
    }
}
