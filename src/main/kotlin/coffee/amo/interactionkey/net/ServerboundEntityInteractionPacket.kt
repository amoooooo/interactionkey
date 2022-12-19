package coffee.amo.interactionkey.net

import coffee.amo.interactionkey.api.Interactable
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent
import java.util.*
import java.util.function.Supplier

class ServerboundEntityInteractionPacket(private val entityId: Int, private val playerId: UUID) {
    companion object {
        @JvmStatic
        fun encode(packet: ServerboundEntityInteractionPacket, buffer: FriendlyByteBuf) {
            buffer.writeInt(packet.entityId)
            buffer.writeUUID(packet.playerId)
        }

        @JvmStatic
        fun decode(buffer: FriendlyByteBuf): ServerboundEntityInteractionPacket {
            return ServerboundEntityInteractionPacket(buffer.readInt(), buffer.readUUID())
        }

        @JvmStatic
        fun handle(packet: ServerboundEntityInteractionPacket, ctx: Supplier<NetworkEvent.Context?>?) {
            ctx!!.get()!!.enqueueWork {
                val e = ctx.get()!!.sender!!.level.getEntity(packet.entityId)
                if (e != null) {
                    if (e is Interactable) {
                        e.interact(e.level.getPlayerByUUID(packet.playerId))
                    }
                }
            }
            ctx.get()!!.packetHandled = true
        }
    }
}