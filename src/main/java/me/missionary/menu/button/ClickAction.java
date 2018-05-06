package me.missionary.menu.button;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 3/2/2018
 */
@FunctionalInterface
public interface ClickAction {

    /**
     * Preforms an operation on the given argument(s)
     *
     * @param player                             The {@link Player} that has acted
     * @param buttonClickTypeButtonClickTypePair The {@link ButtonClickTypePair} that contains the {@link Button} clicked and the {@link ClickType}
     */
    void accept(Player player, ButtonClickTypePair buttonClickTypeButtonClickTypePair);

    @Getter
    @Setter
    @AllArgsConstructor
    class ButtonClickTypePair {
        private Button button;
        private ClickType clickType;
    }

}
