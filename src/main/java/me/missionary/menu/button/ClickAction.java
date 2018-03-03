package me.missionary.menu.button;

import org.bukkit.entity.Player;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 3/2/2018
 */
@FunctionalInterface
public interface ClickAction {

    /**
     * Preforms an operation on the given argument(s)
     *
     * @param player The {@link Player} that has acted
     * @param button The {@link Button} that has been acted upon
     */
    void accept(Player player, Button button);
}
