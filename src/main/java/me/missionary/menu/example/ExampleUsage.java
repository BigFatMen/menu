package me.missionary.menu.example;

import me.missionary.menu.Menu;
import me.missionary.menu.MenuHandler;
import me.missionary.menu.button.Button;
import me.missionary.menu.button.ClickAction;
import me.missionary.menu.mask.Mask2D;
import me.missionary.menu.type.impl.ChestMenu;
import me.missionary.menu.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 2/21/2018
 */
public class ExampleUsage extends JavaPlugin implements CommandExecutor {

    private static final Button MASK_FILLER = Button.placeholder(new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 8).setName(ChatColor.WHITE + "").toItemStack());

    @Override
    public void onEnable() {
        new MenuHandler(this);
        getServer().getPluginCommand("test").setExecutor(this);
    }

    @Override
    public void onDisable() {
        // Nothing to handle here.
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (ThreadLocalRandom.current().nextBoolean()) {
                Menu menu = new ChestMenu(player, ChatColor.YELLOW + "Example Menu", 1);
                menu.setItem(4, new ButtonImpl());
                menu.showMenu((Player) commandSender);
            } else {
                Menu maskedMenu = new ChestMenu(player, ChatColor.YELLOW + "Masked Menu Example", 3);
                new Mask2D()
                        .setButton('0', MASK_FILLER)
                        .setButton('1', new Button() {
                            @Override
                            public ItemStack getButton(Player player) {
                                return new ItemBuilder(Material.WOOL).setName("Mask Example").setDyeColor(DyeColor.RED).toItemStack();
                            }

                            @Override
                            public void onClick(Player player, ClickAction.InformationPair clickAction) {
                                if (clickAction.getClickType() == ClickType.LEFT) {
                                    player.sendMessage(ChatColor.GREEN + "You clicked the button!");
                                }
                            }
                        })
                        .setMaskPattern(
                                "000000000",
                                "000010000",
                                "000000000")
                        .applyTo(maskedMenu);
            }
        }
        return true;
    }

    public class ButtonImpl extends Button {

        @Override
        public ItemStack getButton(Player player) {
            ItemBuilder itemBuilder = new ItemBuilder(Material.WOOL);
            itemBuilder.setName(ChatColor.LIGHT_PURPLE + "Woah! Fancy " + ChatColor.YELLOW + System.currentTimeMillis());
            itemBuilder.setLore(Arrays.asList(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------",
                    ChatColor.YELLOW + "Test: " + ChatColor.GREEN + "Success!",
                    ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------"
            ));
            itemBuilder.setDyeColor(DyeColor.PINK);
            return itemBuilder.toItemStack();
        }

        @Override
        public void onClick(Player player, ClickAction.InformationPair informationPair) {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Worked!");
        }
    }

}
