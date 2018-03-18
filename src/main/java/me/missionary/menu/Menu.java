package me.missionary.menu;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.missionary.menu.button.Button;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 2/21/2018
 */
public class Menu {

    @Getter
    private final String title;
    @Getter
    private final int size;
    private List<Button> contents;
    @Getter
    private BiConsumer<Player, Menu> closeHandler;
    @Getter
    @Setter
    private boolean isStatic; // Does the inventory stay.
    @Getter
    private Inventory craftBukkitInventory;

    public Menu(String title, int size) { // No need for @NonNull as the other constructor will handle it.
        this(title, size, new ArrayList<>());
    }

    public Menu(@NonNull String title, int size, List<Button> contents) {
        this.title = title;
        if (size <= 0 || size > 6) {
            throw new IndexOutOfBoundsException("A menu can only have between 1 & 6 for a size");
        }
        this.size = size;
        this.contents = contents;
        MenuListener.getInstance().registerMenuListener(this);
    }

    public void addItem(Button button) { // #setItem will enforce the non null policy.
        setItem(getFirstEmptySlot(), button);
    }

    public void setItem(int index, @NonNull Button button) {
        checkBounds(index, "setItem(); Index is out of range!");
        contents.add(index, button);
    }

    public void fill(Button button){
        IntStream.range(0, size * 9).filter(value -> contents.get(value) == null).forEach(value -> setItem(value, button));
    }

    public void fillRange(int startingIndex, int endIndex, @NonNull Button button) {
        IntStream.range(startingIndex, endIndex).forEach(i -> setItem(i, button));
    }

    public void setCloseHandler(BiConsumer<Player, Menu> closeHandler) {
        this.closeHandler = closeHandler;
    }

    /**
     * Gets the first empty slot in the inventory
     *
     * @return an applicable slot or -1 if no slot can be found
     */
    private int getFirstEmptySlot() {
        for (int i = 0; i < contents.size(); i++) {
            Button is = contents.get(i);
            if (is == null || is.getStack().getType() == Material.AIR) {
                return i;
            }
        }
        return -1; // Will throw when #checkBounds is called.
    }

    /**
     * Helper method to see if the index is valid.
     *
     * @param slot             the index to check
     * @param exceptionMessage What to say if the index check fails
     */
    private void checkBounds(int slot, String exceptionMessage) {
        if (slot < 0 || slot > (size * 9)) {
            throw new IndexOutOfBoundsException(exceptionMessage);
        }
    }

    private boolean hasBeenBuilt() {
        return craftBukkitInventory != null;
    }

    private void buildCBInventory() {
        this.craftBukkitInventory = Bukkit.createInventory(null, size * 9, title);

        for (int i = 0; i < contents.size(); i++) {
            ItemStack cbIs = contents.get(i).getStack();
            if (cbIs == null) {
                cbIs = new ItemStack(Material.AIR);
            }
            craftBukkitInventory.setItem(i, cbIs);
        }
    }

    public Button getButtonByIndex(int index) {
        return contents.get(index);
    }

    public void show(Player player) {
        if (!hasBeenBuilt()) {
            buildCBInventory();
        }

        player.openInventory(craftBukkitInventory);
    }

    public List<Button> getContents() {
        return Collections.unmodifiableList(contents); // The contents should'nt be mutated out of this class.
    }
}
