package me.missionary.menu.button;

import lombok.Getter;
import lombok.NonNull;
import me.missionary.menu.util.ItemBuilder;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 2/21/2018
 */
@Getter
public class Button {

    private boolean moveable;
    private ItemBuilder stack;
    private ClickAction consumer;

    public Button(boolean moveable, @NonNull ItemBuilder stack, ClickAction consumer) {
        this.moveable = moveable;
        this.stack = stack;
        this.consumer = consumer;
    }

    public Button(boolean moveable, ItemBuilder stack) {
        this(moveable, stack, null);
    }
}
