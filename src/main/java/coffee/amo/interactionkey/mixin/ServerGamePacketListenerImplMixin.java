package coffee.amo.interactionkey.mixin;

import coffee.amo.interactionkey.InteractionKey;
import coffee.amo.interactionkey.net.InteractionServerboundUseItemOnPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.minecraft.server.network.ServerGamePacketListenerImpl.wasBlockPlacementAttempt;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @Unique private boolean isEmpty;

    @Inject(method = "handleUseItemOn", at = @At("HEAD"), cancellable = true)
    public void handleUseItemOn(ServerboundUseItemOnPacket pPacket, CallbackInfo ci) {
        if(pPacket instanceof InteractionServerboundUseItemOnPacket) {
            ServerGamePacketListenerImpl instance = (ServerGamePacketListenerImpl) (Object) this;
            PacketUtils.ensureRunningOnSameThread(pPacket, instance, instance.player.getLevel());
            instance.player.connection.ackBlockChangesUpTo(pPacket.getSequence());
            ServerLevel serverlevel = instance.player.getLevel();
            InteractionHand interactionhand = pPacket.getHand();
            ItemStack itemstack = isEmpty ? ItemStack.EMPTY : instance.player.getItemInHand(interactionhand);
            BlockHitResult blockhitresult = pPacket.getHitResult();
            Vec3 vec3 = blockhitresult.getLocation();
            BlockPos blockpos = blockhitresult.getBlockPos();
            Vec3 vec31 = Vec3.atCenterOf(blockpos);
            if (instance.player.canInteractWith(blockpos, 3000000)) {
                Vec3 vec32 = vec3.subtract(vec31);
                double d0 = 1.0000001D;
                if (Math.abs(vec32.x()) < 5D && Math.abs(vec32.y()) < 5D && Math.abs(vec32.z()) < 5D) {
                    Direction direction = blockhitresult.getDirection();
                    instance.player.resetLastActionTime();
                    int i = instance.player.level.getMaxBuildHeight();
                    if (blockpos.getY() < i) {
                        if (instance.awaitingPositionFromClient == null && serverlevel.mayInteract(instance.player, blockpos)) {
                            InteractionResult interactionresult = instance.player.gameMode.useItemOn(instance.player, serverlevel, itemstack, interactionhand, blockhitresult);
                            if (direction == Direction.UP && !interactionresult.consumesAction() && blockpos.getY() >= i - 1 && wasBlockPlacementAttempt(instance.player, itemstack)) {
                                Component component = Component.translatable("build.tooHigh", i - 1).withStyle(ChatFormatting.RED);
                                instance.player.sendSystemMessage(component, true);
                            } else if (interactionresult.shouldSwing()) {
                                instance.player.swing(interactionhand, true);
                            }
                        }
                    } else {
                        Component component1 = Component.translatable("build.tooHigh", i - 1).withStyle(ChatFormatting.RED);
                        instance.player.sendSystemMessage(component1, true);
                    }

                    instance.player.connection.send(new ClientboundBlockUpdatePacket(serverlevel, blockpos));
                    instance.player.connection.send(new ClientboundBlockUpdatePacket(serverlevel, blockpos.relative(direction)));
                } else {
                    System.out.println("Too far");
                }
            }
            ci.cancel();
        }
    }
}
