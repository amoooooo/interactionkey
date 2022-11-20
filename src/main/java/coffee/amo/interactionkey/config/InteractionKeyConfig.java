package coffee.amo.interactionkey.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class InteractionKeyConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;
    public static ForgeConfigSpec.BooleanValue enableVanillaInteract;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder){
        enableVanillaInteract = builder.comment("Enable vanilla interaction when Interaction Key is pressed.").translation("interaction_key.enablevanillainteract").define("enableVanillaInteract", true);
    }
}
