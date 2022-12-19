package coffee.amo.interactionkey.api

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

// Does nothing until i figure out what to do with it.
class InteractableBlockEntity(pType: BlockEntityType<*>?, pPos: BlockPos?, pBlockState: BlockState?) : BlockEntity(pType, pPos, pBlockState), Interactable {
    var blockEntity: BlockEntity? = null
    fun interact() {}
    override val interactableName: String
        get() = blockEntity!!.level!!.getBlockState(blockEntity!!.blockPos).block.name.string
    override val description: String?
        get() = null
    override val priority: Int
        get() = 0

    override fun interact(player: Player?) {}
}