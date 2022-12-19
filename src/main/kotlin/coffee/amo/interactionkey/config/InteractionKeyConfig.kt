package coffee.amo.interactionkey.config

import net.minecraftforge.common.ForgeConfigSpec

object InteractionKeyConfig {
    var GENERAL_SPEC: ForgeConfigSpec? = null
    var enableVanillaInteract: ForgeConfigSpec.BooleanValue? = null

    init {
        val configBuilder = ForgeConfigSpec.Builder()
        setupConfig(configBuilder)
        GENERAL_SPEC = configBuilder.build()
    }

    private fun setupConfig(builder: ForgeConfigSpec.Builder) {
        enableVanillaInteract = builder.comment("Enable vanilla interaction when Interaction Key is pressed.").translation("interaction_key.enablevanillainteract").define("enableVanillaInteract", true)
    }
}