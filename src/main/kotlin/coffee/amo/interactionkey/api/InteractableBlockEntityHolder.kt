package coffee.amo.interactionkey.api

import coffee.amo.interactionkey.client.IWhyDoesThisExist
import net.minecraft.client.Minecraft
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.phys.BlockHitResult

class InteractableBlockEntityHolder(
    val entity: BlockEntity,
    override val interactableName: String?,
    override val description: String?,
    override val priority: Int
) : Interactable {
    override fun interact(player: Player?) {
        entity.blockState.block.use(entity.blockState, entity.level, entity.blockPos, player, InteractionHand.MAIN_HAND, BlockHitResult(false, player!!.position(), Direction.UP, entity.blockPos, false))
    }
}