package coffee.amo.interactionkey.events;

import coffee.amo.interactionkey.InteractionKey;
import coffee.amo.interactionkey.api.event.InteractionKeyPressedEvent;
import coffee.amo.interactionkey.client.ClientUtil;
import coffee.amo.interactionkey.compat.ItemPhysicCompat;
import coffee.amo.interactionkey.config.InteractionKeyConfig;
import coffee.amo.interactionkey.net.PacketHandler;
import coffee.amo.interactionkey.net.ServerboundInteractionPacket;
import coffee.amo.interactionkey.registry.KeyRegistry;
import coffee.amo.interactionkey.util.ItemUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = InteractionKey.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onKeyInput(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.START) {
            if(KeyRegistry.INTERACTION_KEY.consumeClick()) {
                MinecraftForge.EVENT_BUS.post(new InteractionKeyPressedEvent(Minecraft.getInstance().player));
                PacketHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new ServerboundInteractionPacket());
            }
        }
    }

    @SubscribeEvent
    public static void hudElements(RenderGuiOverlayEvent.Post event) {
        ClientUtil.drawHud(event);
    }
}
