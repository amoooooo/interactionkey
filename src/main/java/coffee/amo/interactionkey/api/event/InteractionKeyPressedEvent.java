package coffee.amo.interactionkey.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;

public class InteractionKeyPressedEvent extends PlayerEvent {

    public InteractionKeyPressedEvent(Player player) {
        super(player);
    }
}
