package coffee.amo.interactionkey.registry

import coffee.amo.interactionkey.example.entity.ExampleInteractableEntity
import coffee.amo.interactionkey.example.entity.ExampleInteractableEntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.Level
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object EntityRegistry {
    val ENTITY_TYPES: DeferredRegister<EntityType<*>> = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "interactionkey")
    val EXAMPLE_ENTITY: RegistryObject<EntityType<ExampleInteractableEntity>> = ENTITY_TYPES.register("example_entity") { EntityType.Builder.of({ pEntityType: EntityType<ExampleInteractableEntity>, pLevel: Level -> ExampleInteractableEntity(pEntityType, pLevel, null, null, 0) }, MobCategory.MISC).sized(0.5f, 0.5f).build("example_entity") }

    @EventBusSubscriber(value = [Dist.CLIENT], bus = EventBusSubscriber.Bus.MOD)
    object Client {
        @SubscribeEvent
        fun registerEntityRenderers(event: RegisterRenderers) {
            event.registerEntityRenderer(EXAMPLE_ENTITY.get()) { k: EntityRendererProvider.Context -> ExampleInteractableEntityRenderer(k) }
        }
    }
}