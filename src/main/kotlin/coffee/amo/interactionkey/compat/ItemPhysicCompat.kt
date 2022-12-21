package coffee.amo.interactionkey.compat

import coffee.amo.interactionkey.util.ItemUtil.getClosestItem
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import team.creative.itemphysic.ItemPhysic
import team.creative.itemphysic.common.packet.PickupPacket
import javax.swing.text.html.parser.Entity

object ItemPhysicCompat {
    fun playerPickupInteract(player: Player) {
        val closestItem = getClosestItem(player)
        if (closestItem != null && player.level.isClientSide) {
            ItemPhysic.NETWORK.sendToServer(PickupPacket(closestItem.uuid, true))
        }
    }

    fun doPickup(itemEntity: ItemEntity, player: Player) {
        if (player.level.isClientSide) {
            ItemPhysic.NETWORK.sendToServer(PickupPacket(itemEntity.uuid, true))
        }
    }

}