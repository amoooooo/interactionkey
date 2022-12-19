package coffee.amo.interactionkey.client

import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.phys.BlockHitResult

interface IYeahIHaveNoIdea {
    fun useItemOn(player: LocalPlayer, hand: InteractionHand, result: BlockHitResult): InteractionResult
}