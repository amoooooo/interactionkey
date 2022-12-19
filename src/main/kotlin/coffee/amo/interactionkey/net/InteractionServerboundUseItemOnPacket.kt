package coffee.amo.interactionkey.net

import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket
import net.minecraft.world.InteractionHand
import net.minecraft.world.phys.BlockHitResult

class InteractionServerboundUseItemOnPacket(pHand: InteractionHand, pBlockHit: BlockHitResult, pSequence: Int) : ServerboundUseItemOnPacket(pHand, pBlockHit, pSequence)