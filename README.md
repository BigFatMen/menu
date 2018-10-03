# menu [![](https://jitpack.io/v/missionarydev/menu.svg)](https://jitpack.io/#missionarydev/menu)
A clean and functional inventory management api for use with Bukkit/Spigot plugins. The objective of menu is to allow for easy creation 
of inventory systems without the hassle of the included api's in Bukkit.

## Usage
menu can be easily added to your project with the use of Apache Maven.

#### Requirements of menu
* [Java 8+](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (usage of Java 8 specific features is heavy)
* [Git SCM](https://git-scm.com/downloads)
* [Apache Maven 3](http://maven.apache.org/download.html)

After you have all of the requirements installed & configured you can use the following series of commands to download and install
menu to your local maven repo (.m2)
```
git clone https://github.com/missionarydev/menu.git
cd menu
mvn clean install
```
menu will now have been added to your local maven repo (.m2) and you can begin using it by adding the following into your Maven pom.xml
```xml
<dependency>
    <groupId>me.missionary</groupId>
    <artifactId>menu</artifactId>
    <version>2.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```
After you have added the dependency to your Maven pom.xml the last thing you need to do is register the MenuHandler with your plugin!
```java
new MenuHandler(this);
```

## Functionality
menu currently only provides support for [chest based inventories](https://github.com/missionarydev/menu/blob/master/src/main/java/me/missionary/menu/type/impl/ChestMenu.java) with more inventory types coming Soon™.

Here is an [example](https://github.com/missionarydev/menu/blob/master/src/main/java/me/missionary/menu/example/ExampleUsage.java) of how to create a menu with menu.
```java
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

```
