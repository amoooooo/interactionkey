package coffee.amo.interactionkey.api

import coffee.amo.interactionkey.compat.ItemPhysicCompat
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraftforge.fml.ModList

class InteractableEntityHolder(
    val entity: Entity,
    override val interactableName: String?,
    override val description: String?,
    override val priority: Int
) : Interactable {
    override fun interact(player: Player?) {
        if(entity is ItemEntity){
            if(!entity.level.isClientSide) return;
            if(ModList.get().isLoaded("itemphysic")){
                ItemPhysicCompat.doPickup(entity, player!!)
                return
            }
        }
        if(entity.level.isClientSide) return;
        if(entity is Interactable){
            entity.interact(player)
            return
        }
        entity.interact(player!!, InteractionHand.MAIN_HAND)
    }
}