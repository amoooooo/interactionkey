package coffee.amo.interactionkey;

import coffee.amo.interactionkey.config.InteractionKeyConfig;
import coffee.amo.interactionkey.net.PacketHandler;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(InteractionKey.MODID)
public class InteractionKey {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "interactionkey";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public InteractionKey() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, InteractionKeyConfig.GENERAL_SPEC, "interactionkey.toml");
        PacketHandler.registerMessages();
        MinecraftForge.EVENT_BUS.register(this);
    }
}
