package coffee.amo.interactionkey.registry;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static coffee.amo.interactionkey.InteractionKey.MODID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MODID, value = Dist.CLIENT)
public class KeyRegistry {
    public static final KeyMapping INTERACTION_KEY = new KeyMapping("key.interaction_key.interaction_key", GLFW.GLFW_KEY_F, "key.categories.interaction_key");

    @SubscribeEvent
    public static void onKeyMappingEvent(RegisterKeyMappingsEvent event){
        event.register(INTERACTION_KEY);
    }
}
