package coffee.amo.interactionkey.api;

import net.minecraft.world.entity.player.Player;

public interface Interactable {
    String getInteractableName();
    String getDescription();
    int getPriority();
    void interact(Player player);
}
