package me.missionary.menu.type.impl;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.missionary.menu.Menu;
import me.missionary.menu.MenuHandler;
import me.missionary.menu.button.Button;
import me.missionary.menu.type.BukkitInventoryHolder;
import me.missionary.menu.util.ArrayIterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 3/28/2018
 */
public class ChestMenu implements Menu {

    @Getter
    private final String title;
    private final MenuDimension dimension;
    private final Player player;
    @Setter
    private Menu parent;
    private BukkitInventoryHolder holder;
    private Button[] contents;
    private CloseHandler closeHandler;
    private boolean autoUpdate;

    public ChestMenu(Player player, @NonNull String title, int size) {
        this.player = player;
        this.title = title.length() > 32 ? title.substring(0, 32) : title;
        if (size <= 0 || size > 6) {
            throw new IndexOutOfBoundsException("A menu can only have between 1 & 6 for a size");
        }
        this.dimension = new MenuDimension(size, 9);
        this.contents = new Button[size * 9];
        this.autoUpdate = true;
    }

    public ChestMenu(Player player, @NonNull String title, int size, boolean autoUpdate) {
        this(player, title, size);
        this.autoUpdate = autoUpdate;
    }

    @Override
    public MenuDimension getMenuDimension() {
        return dimension;
    }

    @Override
    public void addItem(Button button) {
        setItem(getFirstEmptySlot(), button);
    }

    @Override
    public void setItem(int index, Button button) {
        checkBounds(index);
        contents[index] = button;
    }

    @Override
    public void fill(Button button) {
        fillRange(0, dimension.getSize(), button);
    }

    @Override
    public void fillRange(int startingIndex, int endingIndex, Button button) {
        IntStream.range(startingIndex, endingIndex).forEach(i -> setItem(i, button));
    }

    @Override
    public int getFirstEmptySlot() {
        for (int i = 0; i < contents.length; i++) {
            Button button = contents[i];
            if (button == null) {
                return i;
            }
        }
        return -1; // Will throw when #checkBounds is called.
    }

    @Override
    public void checkBounds(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > (dimension.getSize())) {
            throw new IndexOutOfBoundsException(String.format("setItem(); %s is out of bounds!", index));
        }
    }

    @Override
    public Optional<Button> getButtonByIndex(int index) {
        return Optional.ofNullable(contents[index]);
    }

    @Override
    public void buildInventory(boolean initial) {
        if (initial) {
            this.holder = new BukkitInventoryHolder(this);
            holder.setInventory(Bukkit.createInventory(holder, dimension.getSize(), title));
        } else {
            holder.getInventory().clear();
        }
        for (int i = 0; i < contents.length; i++) {
            Button button = contents[i];
            if (button != null) {
                holder.getInventory().setItem(i, button.getButton(player));
            }
        }
    }

    @Override
    public void showMenu(Player player, boolean update) {
        if (holder == null) {
            buildInventory(true);
        } else {
            buildInventory(false);
        }
        if (!MenuHandler.OPEN_MENUS.containsKey(player.getUniqueId())) {
            MenuHandler.OPEN_MENUS.put(player.getUniqueId(), this);
        }
        if (update) {
            player.updateInventory();
        } else {
            player.openInventory(holder.getInventory());
        }
    }

    @Override
    public void close(Player player) {
        player.closeInventory();
        handleClose(player);
    }

    @Override
    public void setCloseHandler(CloseHandler handler) {
        this.closeHandler = handler;
    }

    @Override
    public void handleClose(Player player) {
        if (closeHandler != null) {
            closeHandler.accept(player, this);
        }
        MenuHandler.OPEN_MENUS.remove(player.getUniqueId());
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @Override
    public Optional<Menu> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public Iterator<Button> iterator() {
        return new ArrayIterator<>(contents);
    }
}
