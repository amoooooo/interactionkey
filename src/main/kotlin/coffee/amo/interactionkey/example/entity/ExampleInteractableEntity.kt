package coffee.amo.interactionkey.example.entity

import coffee.amo.interactionkey.api.Interactable
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraftforge.network.NetworkHooks

class ExampleInteractableEntity(pEntityType: EntityType<*>?, pLevel: Level?,
                                override val interactableName: String?,
                                override val description: String?,
                                override val priority: Int
) : Entity(pEntityType!!, pLevel!!), Interactable {

    override fun interact(player: Player?) {
        player!!.sendSystemMessage(Component.literal("You interacted with the example interactable entity!"))
        val ie = ItemEntity(level, this.x, this.y, this.z, Items.GLOW_ITEM_FRAME.defaultInstance)
        ie.setPos(this.x, this.y, this.z)
        ie.deltaMovement = ie.deltaMovement.add(player.level.random.nextFloat() - 0.5, player.level.random.nextFloat().toDouble(), player.level.random.nextFloat() - 0.5)
        player.level.addFreshEntity(ie)
    }

    override fun defineSynchedData() {}
    override fun readAdditionalSaveData(pCompound: CompoundTag) {}
    override fun addAdditionalSaveData(pCompound: CompoundTag) {}
    override fun isPickable(): Boolean {
        return true
    }

    override fun canCollideWith(pEntity: Entity): Boolean {
        return pEntity !is ItemEntity
    }

    override fun makeBoundingBox(): AABB {
        return AABB.ofSize(position(), 0.5, 1.0, 0.5)
    }

    override fun interact(pPlayer: Player, pHand: InteractionHand): InteractionResult {
        if (level.isClientSide && pHand == InteractionHand.MAIN_HAND) pPlayer.sendSystemMessage(Component.literal("This is a rightclick"))
        return super.interact(pPlayer, pHand)
    }

    override fun getAddEntityPacket(): Packet<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }
}