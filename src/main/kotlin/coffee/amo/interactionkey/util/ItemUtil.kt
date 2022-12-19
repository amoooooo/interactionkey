package coffee.amo.interactionkey.util

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import java.util.*

object ItemUtil {
    @JvmStatic
    fun getClosestItem(player: Player): ItemEntity? {
        return player.level.getEntities(null, player.boundingBox.inflate(2.0)).stream().map { s: Entity? -> if (s is ItemEntity) s else null }.filter { obj: ItemEntity? -> Objects.nonNull(obj) }.min { s1: ItemEntity?, s2: ItemEntity? ->
            val d1 = s1?.let { player.distanceToSqr(it) }
            val d2 = s2?.let { player.distanceToSqr(it) }
            d1!!.compareTo(d2!!);
        }.orElse(null)
    }
}