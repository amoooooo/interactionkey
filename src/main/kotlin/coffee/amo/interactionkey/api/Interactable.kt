package coffee.amo.interactionkey.api

import net.minecraft.world.entity.player.Player

interface Interactable {
    val interactableName: String?
    val description: String?
    val priority: Int
    fun interact(player: Player?)
}