package coffee.amo.interactionkey.events;

import coffee.amo.interactionkey.InteractionKey;
import coffee.amo.interactionkey.api.event.InteractionKeyPressedEvent;
import coffee.amo.interactionkey.client.ClientUtil;
import coffee.amo.interactionkey.compat.ItemPhysicCompat;
import coffee.amo.interactionkey.config.InteractionKeyConfig;
import coffee.amo.interactionkey.net.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import team.creative.itemphysic.ItemPhysic;
import team.creative.itemphysic.common.packet.PickupPacket;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = InteractionKey.MODID)
public class CommonEvents {

    @SubscribeEvent
    public static void onInteractionKeyPressed(InteractionKeyPressedEvent event) {
        if(event.getEntity().getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof BlockItem) return;
        HitResult hitResult = event.getEntity().pick(event.getEntity().getAttributeValue(ForgeMod.REACH_DISTANCE.get()), 0, false);
        if(hitResult.getType() == HitResult.Type.ENTITY){
            if(((EntityHitResult)hitResult).getEntity() instanceof ItemEntity){
                if(event.getEntity().level.isClientSide && ModList.get().isLoaded("itemphysic")){
                    ItemPhysicCompat.doPickup((ItemEntity) ((EntityHitResult)hitResult).getEntity(), event.getEntity());
                    return;
                }
            }
            event.getEntity().interactOn(event.getEntity(), InteractionHand.MAIN_HAND);
            return;
        }
        if(InteractionKeyConfig.enableVanillaInteract.get()){
            // get the block the player is looking at on the server
            if(hitResult.getType() == HitResult.Type.BLOCK){
                BlockHitResult blockHit = (BlockHitResult) hitResult;
                // if the player is looking at a block, interact with it
                if(event.getEntity().level.isClientSide) ClientUtil.useItemHack();
                return;
           }
        }
        if(event.getEntity().level.isClientSide){
            if(ModList.get().isLoaded("itemphysic")){
                ItemPhysicCompat.playerPickupInteract(event.getEntity());
                return;
            }
        }
    }
}
