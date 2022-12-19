@file:Suppress("INACCESSIBLE_TYPE")

package coffee.amo.interactionkey.net

import coffee.amo.interactionkey.net.ServerboundEntityInteractionPacket.Companion.decode
import coffee.amo.interactionkey.net.ServerboundEntityInteractionPacket.Companion.encode
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.network.NetworkEvent
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.simple.SimpleChannel
import java.util.function.Supplier

object PacketHandler {
    private const val PROTOCOL_VERSION = "1"
    object Instance {
        val INSTANCE = NetworkRegistry.newSimpleChannel(
                ResourceLocation("interactionkey", "main"),
                { PROTOCOL_VERSION }, { anObject: String? -> PROTOCOL_VERSION.equals(anObject) }) { anObject: String? -> PROTOCOL_VERSION.equals(anObject) }
    }
    @JvmField
    val INSTANCE: SimpleChannel = NetworkRegistry.newSimpleChannel(
            ResourceLocation("interactionkey", "main"),
            { PROTOCOL_VERSION }, { anObject: String? -> PROTOCOL_VERSION.equals(anObject) }) { anObject: String? -> PROTOCOL_VERSION.equals(anObject) }
    private var id = 0
    fun registerMessages() {
        INSTANCE.registerMessage(id++, ServerboundInteractionPacket::class.java, ServerboundInteractionPacket::encode, ServerboundInteractionPacket::decode, ServerboundInteractionPacket::handle)
        INSTANCE.registerMessage(id++, ServerboundEntityInteractionPacket::class.java, { p: ServerboundEntityInteractionPacket?, b: FriendlyByteBuf? -> encode(p!!, b!!) }, { b: FriendlyByteBuf? -> decode(b!!) }) { p: ServerboundEntityInteractionPacket?, c: Supplier<NetworkEvent.Context?>? -> ServerboundEntityInteractionPacket.handle(p!!, c) }
    }
}