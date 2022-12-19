package coffee.amo.interactionkey.client

import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult

interface IWhyDoesThisExist {
    fun startUseItem(test: Int)
    fun setOverrideBlockHitResult(pResult: BlockHitResult?)
    fun setOverrideEntityHitResult(pResult: EntityHitResult?)
}