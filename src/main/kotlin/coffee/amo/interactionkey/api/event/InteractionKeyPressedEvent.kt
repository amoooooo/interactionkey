package coffee.amo.interactionkey.api.event

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.entity.player.PlayerEvent

class InteractionKeyPressedEvent(val player: Player?, val blockPos: BlockPos?, val entityID: Int?) : PlayerEvent(player)