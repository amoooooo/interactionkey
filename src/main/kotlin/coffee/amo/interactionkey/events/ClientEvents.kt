package coffee.amo.interactionkey.events

import coffee.amo.interactionkey.InteractionKey
import coffee.amo.interactionkey.api.InteractableBlockEntityHolder
import coffee.amo.interactionkey.api.InteractableEntityHolder
import coffee.amo.interactionkey.api.event.InteractionKeyPressedEvent
import coffee.amo.interactionkey.client.ClientUtil
import coffee.amo.interactionkey.client.IWhyDoesThisExist
import coffee.amo.interactionkey.client.InteractionStack
import coffee.amo.interactionkey.net.PacketHandler
import coffee.amo.interactionkey.net.ServerboundInteractionPacket
import coffee.amo.interactionkey.registry.KeyRegistry
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.client.event.RenderGuiOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ClientTickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.network.PacketDistributor

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = InteractionKey.MODID, value = [Dist.CLIENT])
object ClientEvents {
    @SubscribeEvent
    fun onKeyInput(event: ClientTickEvent) {
        if (event.phase == TickEvent.Phase.START) {
            val player = Minecraft.getInstance().player
            val itemEntities = ClientUtil.getEntities(player, player?.position(), 5.0)
            val blockEntities = ClientUtil.getBlockEntities(player, player?.position(), 5.0)
            ClientUtil.fillInteractionStack(itemEntities, blockEntities)
            if(KeyRegistry.SCROLL_UP_KEY.consumeClick()) {
                InteractionStack.selectPrevious()
            }
            if(KeyRegistry.SCROLL_DOWN_KEY.consumeClick()) {
                InteractionStack.selectNext()
            }
            val current = if(InteractionStack.getStack().isNotEmpty()) InteractionStack.getSelected() else null
            var pos: BlockPos = InteractionKey.CHECK_POS
            var entityID: Int = InteractionKey.CHECK_INT
            if(current is InteractableBlockEntityHolder){
                val blockEntity = current.entity
                val blockPos = blockEntity.blockPos
                val hitResult =
                    Minecraft.getInstance().player?.position()?.let { BlockHitResult(it, Direction.UP, blockPos, false) }
                (Minecraft.getInstance() as IWhyDoesThisExist).setOverrideBlockHitResult(hitResult)
                pos = blockPos
            }
            if(current is InteractableEntityHolder){
                val entity = current.entity
                val hitResult =
                    Minecraft.getInstance().player?.position()?.let { EntityHitResult(entity) }
                (Minecraft.getInstance() as IWhyDoesThisExist).setOverrideEntityHitResult(hitResult)
                entityID = entity.id
            }
            if (KeyRegistry.INTERACTION_KEY.consumeClick()) {
                //ClientUtil.useItemHack()
                MinecraftForge.EVENT_BUS.post(InteractionKeyPressedEvent(Minecraft.getInstance().player as Player, pos, entityID))
                PacketHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), ServerboundInteractionPacket(pos, entityID))
            }
        }
    }

    @SubscribeEvent
    fun onMouseScroll(event: InputEvent.MouseScrollingEvent){
        if(!Minecraft.getInstance().player!!.isCrouching) return
        if(InteractionStack.getStack().isEmpty()) return
        if(InteractionStack.getStack().size == 1) return
        if(event.scrollDelta > 0){
            InteractionStack.selectPrevious()
        }
        if(event.scrollDelta < 0){
            InteractionStack.selectNext()
        }
        event.isCanceled = true
    }

    @SubscribeEvent
    fun hudElements(event: RenderGuiOverlayEvent.Post) {
        ClientUtil.drawHud(event)
    }

    fun init(){
        MinecraftForge.EVENT_BUS.register(this)
    }
}