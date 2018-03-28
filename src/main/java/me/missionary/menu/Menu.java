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

import java.util.Optional;
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
    private Button[] contents;
    @Getter
    private BiConsumer<Player, Menu> closeHandler;
    @Getter
    @Setter
    private boolean isStatic; // Does the inventory stay.
    @Getter
    private Inventory craftBukkitInventory;


    public Menu(String title, int size) { // No need for @NonNull as the other constructor will handle it.
        this(title, size, null);
    }

    public Menu(@NonNull String title, int size, Button[] contents) {
        this.title = title;
        if (size <= 0 || size > 6) {
            throw new IndexOutOfBoundsException("A menu can only have between 1 & 6 for a size");
        }
        this.size = size;
        this.contents = contents == null ? new Button[size * 9] : contents;
        MenuListener.getInstance().registerMenuListener(this);
    }

    /**
     * Add's a {@link Button} to the first empty slot in the inventory
     *
     * @param button The {@link Button} to use
     */
    public void addItem(Button button) { // #setItem will enforce the non null policy.
        setItem(getFirstEmptySlot(), button);
    }

    /**
     * Set's an {@link Button} at the specified index.
     *
     * @param index  The index in the array at which to use
     * @param button A not null {@link Button} which is set at the specified index.
     */
    public void setItem(int index, @NonNull Button button) {
        checkBounds(index);
        contents[index] = button;
    }

    /**
     * Fill's the entirety of the Menu.
     *
     * @param button The specified {@link Button} to use in the procedure
     */
    public void fill(Button button) {
        fillRange(0, size, button);
    }

    /**
     * Fill's a selected range with the specified {@link Button}
     *
     * @param startingIndex The index to start the procedure
     * @param endIndex      The index at which the procedure shall terminate
     * @param button        The specified {@link Button} that the procedure will use.
     */
    public void fillRange(int startingIndex, int endIndex, @NonNull Button button) {
        IntStream.range(startingIndex, endIndex).forEach(i -> setItem(i, button));
    }

    /**
     * Sets an action handled by a {@link BiConsumer} that will execute upon the closing of the menu
     *
     * @param closeHandler The {@link BiConsumer} that handles the action.
     */
    public void setCloseHandler(BiConsumer<Player, Menu> closeHandler) {
        this.closeHandler = closeHandler;
    }

    /**
     * Gets the first empty slot in the inventory
     *
     * @return an applicable slot or -1 if no slot can be found
     */
    private int getFirstEmptySlot() {
        for (int i = 0; i < contents.length; i++) {
            Button button = contents[i];
            if (button == null) {
                return i;
            }
        }
        return -1; // Will throw when #checkBounds is called.
    }

    /**
     * Helper method to see if the index is valid.
     *
     * @param slot the index to check
     */
    private void checkBounds(int slot) {
        if (slot < 0 || slot > (size * 9)) {
            throw new IndexOutOfBoundsException(String.format("setItem(); %s is out of bounds!", slot));
        }
    }

    private boolean hasBeenBuilt() {
        return craftBukkitInventory != null;
    }

    /**
     * Build the CraftBukkit {@link Inventory}
     *
     * @param initBuild Has the inventory already been created?
     */
    private void buildCBInventory(boolean initBuild) {
        if (initBuild) {
            this.craftBukkitInventory = Bukkit.createInventory(null, size * 9, title);
        }

        for (int i = 0; i < contents.length; i++) {
            Button cbIs = contents[i];
            ItemStack stack;
            if (cbIs == null) {
                stack = new ItemStack(Material.AIR);
            } else {
                stack = cbIs.getStack().toItemStack();
            }
            craftBukkitInventory.setItem(i, stack);
        }
    }

    /**
     * Gets a {@link Button} by the index in the Button array
     *
     * @param index The position of the Button
     * @return An {@link Optional} that may or may not contain a viable {@link Button}
     */
    public Optional<Button> getButtonByIndex(int index) {
        return Optional.ofNullable(contents[index]);
    }

    /**
     * Rebuild and update the inventory for the player
     *
     * @param player The {@link Player} to refresh the menu for
     */
    public void refresh(Player player) {
        buildCBInventory(hasBeenBuilt());
        player.updateInventory();
    }

    /**
     * Open the {@link Inventory} for this menu
     *
     * @param player The {@link Player} to open the menu for
     */
    public void show(Player player) {
        if (!hasBeenBuilt()) {
            buildCBInventory(true);
        }

        player.openInventory(craftBukkitInventory);
    }

    /**
     * Internal method to actuate the close processes.
     *
     * @param player The {@link Player} to handle the process for.
     */
    protected void handleClose(Player player) {
        if (closeHandler != null) {
            closeHandler.accept(player, this);
        }
        if (!isStatic) {
            MenuListener.getInstance().removeMenuListener(this);
        }
    }

    /**
     * Manually close the inventory
     *
     * @param player The {@link Player} to close the inventory for
     */
    public void close(Player player) {
        player.closeInventory();
        handleClose(player);
    }
}