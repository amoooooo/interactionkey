package coffee.amo.interactionkey.events

import coffee.amo.interactionkey.InteractionKey
import coffee.amo.interactionkey.api.InteractableEntityHolder
import coffee.amo.interactionkey.api.event.InteractionKeyPressedEvent
import coffee.amo.interactionkey.client.ClientUtil
import coffee.amo.interactionkey.compat.ItemPhysicCompat
import coffee.amo.interactionkey.config.InteractionKeyConfig
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.BlockItem
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import net.minecraftforge.common.ForgeMod
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber(bus = EventBusSubscriber.Bus.FORGE, modid = InteractionKey.MODID)
object CommonEvents {
    @SubscribeEvent
    fun onInteractionKeyPressed(event: InteractionKeyPressedEvent) {
        if(event.entityID != InteractionKey.CHECK_INT){
            val entity = event.player!!.level.getEntity(event.entityID!!);
            if(entity is ItemEntity){
                InteractableEntityHolder(entity, null, null, 0).interact(event.player)
                return;
            }
            try {
                InteractableEntityHolder(entity!!, null, null, 0).interact(event.player)
                return;
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if(event.blockPos != InteractionKey.CHECK_POS){
            if(event.player!!.level.isClientSide) return
            event.player!!.level.getBlockState(event.blockPos!!).use(event.player.level, event.player, InteractionHand.MAIN_HAND, BlockHitResult(
                Vec3.atCenterOf(event.blockPos), Direction.UP, event.blockPos, false))
            return;
        }
        //        if(event.getEntity().level.isClientSide){
//            if(ModList.get().isLoaded("itemphysic")){
//                ItemPhysicCompat.playerPickupInteract(event.getEntity());
//                return;
//            }
//        }
    }

    fun init(){
        MinecraftForge.EVENT_BUS.register(this)
    }
}