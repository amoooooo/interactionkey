package coffee.amo.interactionkey.net

import coffee.amo.interactionkey.api.event.InteractionKeyPressedEvent
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Player
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class ServerboundInteractionPacket(val blockPos: BlockPos, val entityID: Int) {
    companion object{
        fun encode(packet: ServerboundInteractionPacket?, buffer: FriendlyByteBuf?) {
            buffer!!.writeBlockPos(packet!!.blockPos)
            buffer.writeVarInt(packet.entityID)
        }

        fun decode(buffer: FriendlyByteBuf?): ServerboundInteractionPacket {
            return ServerboundInteractionPacket(buffer!!.readBlockPos(), buffer.readVarInt())
        }

        fun handle(packet: ServerboundInteractionPacket?, ctx: Supplier<NetworkEvent.Context?>?) {
            ctx!!.get()!!.enqueueWork {
                val player = ctx.get()!!.sender as Player
                val blockPos = packet!!.blockPos
                val entityID = packet.entityID
                MinecraftForge.EVENT_BUS.post(InteractionKeyPressedEvent(player, blockPos, entityID))
                println("Received interaction packet from ${player.name} at $blockPos with entityID $entityID")
            }
            ctx.get()!!.packetHandled = true
        }
    }


}