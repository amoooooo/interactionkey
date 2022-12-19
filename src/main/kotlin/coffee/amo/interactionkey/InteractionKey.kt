package coffee.amo.interactionkey

import coffee.amo.interactionkey.InteractionKey
import coffee.amo.interactionkey.config.InteractionKeyConfig
import coffee.amo.interactionkey.events.ClientEvents
import coffee.amo.interactionkey.events.CommonEvents
import coffee.amo.interactionkey.net.PacketHandler
import coffee.amo.interactionkey.registry.EntityRegistry
import com.mojang.logging.LogUtils
import net.minecraft.core.BlockPos
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runForDist

// The value here should match an entry in the META-INF/mods.toml file
@Mod(InteractionKey.MODID)
class InteractionKey {
    companion object {
        // Define mod id in a common place for everything to reference
        const val MODID = "interactionkey"
        // Directly reference a slf4j logger
        var LOGGER: Logger = LogUtils.getLogger()
        val CHECK_POS = BlockPos(1290845,12394,105023)
        const val CHECK_INT: Int = 239853798
    }
    init {
        val modEventBus = MOD_BUS
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, InteractionKeyConfig.GENERAL_SPEC, "interactionkey.toml")
        PacketHandler.registerMessages()
        EntityRegistry.ENTITY_TYPES.register(modEventBus)
        MinecraftForge.EVENT_BUS.register(this)
        val obj = runForDist(
            clientTarget = {
                InteractionKeyClient
            },
            serverTarget = {

            }
        )
        FORGE_BUS.apply{
            register(CommonEvents)
        }
    }
}