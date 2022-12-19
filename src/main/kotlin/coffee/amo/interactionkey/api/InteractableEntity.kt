package coffee.amo.interactionkey.api

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

// Also does nothing unless you call it inside the interact event
class InteractableEntity(pEntityType: EntityType<*>, pLevel: Level) : Entity(pEntityType, pLevel), Interactable {
    var entity: Entity? = null
    override fun defineSynchedData() {}
    override fun readAdditionalSaveData(pCompound: CompoundTag) {}
    override fun addAdditionalSaveData(pCompound: CompoundTag) {}
    override val interactableName: String?
        get() = null

    override fun getAddEntityPacket(): Packet<*>? {
        return null
    }

    override val description: String?
        get() = null
    override val priority: Int
        get() = 0

    override fun interact(player: Player?) {}
}