package coffee.amo.interactionkey.net;

import coffee.amo.interactionkey.api.event.InteractionKeyPressedEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundInteractionPacket {
    public ServerboundInteractionPacket() {
    }

    public static void encode(ServerboundInteractionPacket packet, FriendlyByteBuf buffer) {
    }

    public static ServerboundInteractionPacket decode(FriendlyByteBuf buffer) {
        return new ServerboundInteractionPacket();
    }

    public static void handle(ServerboundInteractionPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            MinecraftForge.EVENT_BUS.register(new InteractionKeyPressedEvent(ctx.get().getSender()));
        });
        ctx.get().setPacketHandled(true);
    }


}
