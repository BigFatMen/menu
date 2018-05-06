# menu
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
    <version>1.1-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```
After you have added the dependency to your Maven pom.xml the last thing you need to do is register the MenuListener with your plugin!
```java
Bukkit.getServer().getPluginManager().registerEvents(MenuListener.getInstance(), instance of your plugin);
```

## Functionality
menu currently only provides support for [chest based inventories](https://github.com/missionarydev/menu/blob/master/src/main/java/me/missionary/menu/type/impl/ChestMenu.java) with more inventory types coming Soon™.

Here is an example of how to create a menu with menu.
```java
Menu menu = new ChestMenu("Title", 4);    
menu.setItem(12, new Button(true, new ItemBuilder(Material.STICK).setName(ChatColor.LIGHT_PURPLE + "Stick"), (player1, button) -> {
    player1.sendMessage("You have clicked the Stick.");
}));
menu.setItem(13, new Button(true, new ItemBuilder(Material.ACACIA_DOOR).setName("Door").toItemStack(), (player1, pair) -> {
    if (pair.getClickType().isLeftClick()) {
        player1.sendMessage("You have clicked the " + pair.getButton().getStack().getItemMeta().getDisplayName());
    }
}));
menu.setCloseHandler((player1, menu1) -> player1.sendMessage("Wow! You closed the inventory."));
menu.showMenu(player);
```
