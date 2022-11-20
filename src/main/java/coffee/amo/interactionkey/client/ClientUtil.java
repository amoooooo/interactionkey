package coffee.amo.interactionkey.client;

import coffee.amo.interactionkey.api.InteractableBlockEntity;
import coffee.amo.interactionkey.registry.KeyRegistry;
import coffee.amo.interactionkey.util.ItemUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import crystalspider.harvestwithease.config.HarvestWithEaseConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;

public class ClientUtil {

    public static void useItemHack(){
        Minecraft.getInstance().startUseItem();
    }
    // separate from harvest with ease, allow right clicking with blocks but not placing them
    public static void drawHud(RenderGuiOverlayEvent.Post event) {
        float centerX = event.getWindow().getGuiScaledWidth() / 2f;
        float centerY = event.getWindow().getGuiScaledHeight() / 2f;
        PoseStack ps = event.getPoseStack();
        ps.pushPose();
        ps.translate(centerX, centerY, 0);
        BlockEntity ibe = getInteractableBlockEntity();
        ItemEntity ie = getItem(Minecraft.getInstance().player);
        if(ie != null) {
            String text = ie.getItem().getDisplayName().getString();
            int width = Minecraft.getInstance().font.width(text);
            int height = Minecraft.getInstance().font.lineHeight;
            Gui.drawCenteredString(ps, Minecraft.getInstance().font, text, (int) 8 + (width/2), -4, 0xFFFFFFFF);
        } else if (ibe != null) {
            String text = ibe.getBlockState().getBlock().getName().getString();
            float textWidth = Minecraft.getInstance().font.width(text);
            String draw = "[" + KeyRegistry.INTERACTION_KEY.getKey().getDisplayName().getString().toUpperCase() + "] " + text;
            Gui.drawCenteredString(ps, Minecraft.getInstance().font, draw, (int) (16 + (textWidth / 2)), 0, 0xFFFFFF);
        } else if (getInteractableBlockState() != null){
            String text = getInteractableBlockState().getBlock().getName().getString();
            float textWidth = Minecraft.getInstance().font.width(text);
            String draw = "[" + KeyRegistry.INTERACTION_KEY.getKey().getDisplayName().getString().toUpperCase() + "] " + text;
            Gui.drawCenteredString(ps, Minecraft.getInstance().font, draw, (int) (16 + (textWidth / 2)), 0, 0xFFFFFF);
        } else if (ItemUtil.getClosestItem(Minecraft.getInstance().player) != null) {
            String itemName = ItemUtil.getClosestItem(Minecraft.getInstance().player).getName().getString();
            String draw = "[" + KeyRegistry.INTERACTION_KEY.getKey().getDisplayName().getString().toUpperCase() + "] " + itemName;
            float textWidth = Minecraft.getInstance().font.width(draw);
            Gui.drawCenteredString(ps, Minecraft.getInstance().font, draw, (int) (16 + (textWidth / 2)), 0, 0xFFFFFF);
        }
        ps.popPose();
    }

    public static BlockEntity getInteractableBlockEntity() {
        BlockHitResult result = Minecraft.getInstance().hitResult.getType() == BlockHitResult.Type.BLOCK ? (BlockHitResult) Minecraft.getInstance().hitResult : null;
        if (result != null) {
            if(Minecraft.getInstance().level.getBlockEntity(result.getBlockPos()) != null) {
                return Minecraft.getInstance().level.getBlockEntity(result.getBlockPos());
            }
        }
        return null;
    }

    public static ItemEntity getLookingAtItem(){
        EntityHitResult result = Minecraft.getInstance().hitResult.getType() == EntityHitResult.Type.ENTITY ? (EntityHitResult) Minecraft.getInstance().hitResult : null;
        if (result != null) {
            if(result.getEntity() instanceof ItemEntity){
                return (ItemEntity) result.getEntity();
            }
        }
        return null;
    }

    public static BlockState getInteractableBlockState(){
        BlockHitResult result = Minecraft.getInstance().hitResult.getType() == BlockHitResult.Type.BLOCK ? (BlockHitResult) Minecraft.getInstance().hitResult : null;
        if (result != null) {
            BlockState state = Minecraft.getInstance().level.getBlockState(result.getBlockPos());
            if(isCrop(state.getBlock()))
                return state;
        }
        return null;
    }

    private static boolean isCrop(Block block) {
        return block instanceof CropBlock || block == Blocks.NETHER_WART || block == Blocks.COCOA || HarvestWithEaseConfig.getCrops().contains(ForgeRegistries.BLOCKS.getKey(block).toString());
    }

    public static HitResult getEntityItem(Player player, Vec3 position, Vec3 look){
        Vec3 include = look.subtract(position);
        List list = player.level.getEntities(player, player.getBoundingBox().expandTowards(include));

        for(int i = 0; i < list.size(); i++){
            Entity entity = (Entity) list.get(i);
            if(entity instanceof ItemEntity){
                AABB aabb = entity.getBoundingBox().inflate(0.2);
                Optional<Vec3> vec = aabb.clip(position, look);
                if(vec.isPresent()){
                    return new EntityHitResult(entity, vec.get());
                }
                if(aabb.contains(position)){
                    return new EntityHitResult(entity, position);
                }
            }
        }
        return null;
    }

    public static ItemEntity getItem(Player player){
        double distance = player.getAttributeValue(ForgeMod.REACH_DISTANCE.get());
        float partialTicks = Minecraft.getInstance().getDeltaFrameTime();
        Vec3 position = player.getEyePosition(partialTicks);
        Vec3 look = player.getViewVector(partialTicks);
        if(Minecraft.getInstance().hitResult != null && Minecraft.getInstance().hitResult.getType() != HitResult.Type.MISS){
            distance = Minecraft.getInstance().hitResult.getLocation().distanceTo(position);
        }
        HitResult result = getEntityItem(player, position, position.add(look.x * distance, look.y * distance, look.z * distance));
        if(result != null && result.getType() == HitResult.Type.ENTITY){
            return (ItemEntity)((EntityHitResult)result).getEntity();
        }
        return null;
    }

}
