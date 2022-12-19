package coffee.amo.interactionkey.registry

import coffee.amo.interactionkey.InteractionKey
import net.minecraft.client.KeyMapping
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import org.lwjgl.glfw.GLFW

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = InteractionKey.MODID, value = [Dist.CLIENT])
object KeyRegistry {
    @JvmField
    val INTERACTION_KEY = KeyMapping("key.interaction_key.interaction_key", GLFW.GLFW_KEY_F, "key.categories.interaction_key")
    val SCROLL_UP_KEY = KeyMapping("key.interaction_key.scroll_up_key", GLFW.GLFW_KEY_UP, "key.categories.interaction_key")
    val SCROLL_DOWN_KEY = KeyMapping("key.interaction_key.scroll_down_key", GLFW.GLFW_KEY_DOWN, "key.categories.interaction_key")
    @SubscribeEvent
    fun onKeyMappingEvent(event: RegisterKeyMappingsEvent) {
        event.register(INTERACTION_KEY)
        event.register(SCROLL_UP_KEY)
        event.register(SCROLL_DOWN_KEY)
    }
}