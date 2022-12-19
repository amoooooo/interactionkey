package coffee.amo.interactionkey

import coffee.amo.interactionkey.api.Interactable
import coffee.amo.interactionkey.client.InteractionStack
import coffee.amo.interactionkey.events.ClientEvents
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

object InteractionKeyClient {
    init {
        FORGE_BUS.apply {
            register(ClientEvents)
        }
    }
}