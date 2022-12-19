package coffee.amo.interactionkey.mixin.client;

import coffee.amo.interactionkey.client.IWhyDoesThisExist;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = Minecraft.class, priority = 999)
public class MinecraftMixin implements IWhyDoesThisExist {
    private static BlockHitResult overrideBHR = null;
    private static EntityHitResult overrideEHR = null;
    @Override
    public void startUseItem(int test) {
        if(test == 0) return;
        Minecraft current = (Minecraft) (Object) this;
        if (!current.gameMode.isDestroying()) {
            current.rightClickDelay = 4;
            if (!current.player.isHandsBusy()) {
                if (current.hitResult == null) {
                }

                for(InteractionHand interactionhand : InteractionHand.values()) {
                    var inputEvent = net.minecraftforge.client.ForgeHooksClient.onClickInput(1, current.options.keyUse, interactionhand);
                    if (inputEvent.isCanceled()) {
                        if (inputEvent.shouldSwingHand()) current.player.swing(interactionhand);
                        return;
                    }
                    ItemStack itemstack = ItemStack.EMPTY;
                    if (current.hitResult != null) {
                        switch (current.hitResult.getType()) {
                            case ENTITY:
                                EntityHitResult entityhitresult = (EntityHitResult)current.hitResult;
                                Entity entity = entityhitresult.getEntity();
                                if (!current.level.getWorldBorder().isWithinBounds(entity.blockPosition())) {
                                    return;
                                }

                                EntityHitResult result = overrideEHR == null ? entityhitresult : overrideEHR;
                                if(!current.player.canInteractWith(result.getEntity(), 0)) break; //Forge: Entity may be traced via attack range, but the player may not have sufficient reach.  No padding in client code.
                                InteractionResult interactionresult = current.gameMode.interactAt(current.player, entity, result, interactionhand);
                                if (!interactionresult.consumesAction()) {
                                    interactionresult = current.gameMode.interact(current.player, entity, interactionhand);
                                }

                                if (interactionresult.consumesAction()) {
                                    if (interactionresult.shouldSwing() && inputEvent.shouldSwingHand()) {
                                        current.player.swing(interactionhand);
                                    }

                                    return;
                                }
                                break;
                            case BLOCK:
                                BlockHitResult blockhitresult = (BlockHitResult)current.hitResult;
                                BlockHitResult result1 = overrideBHR == null ? blockhitresult : overrideBHR;
                                int i = itemstack.getCount();
                                InteractionResult interactionresult1 = current.gameMode.useItemOn(current.player, interactionhand, result1);
                                if (interactionresult1.consumesAction()) {
                                    if (interactionresult1.shouldSwing() && inputEvent.shouldSwingHand()) {
                                        current.player.swing(interactionhand);
                                        if (!itemstack.isEmpty() && (itemstack.getCount() != i || current.gameMode.hasInfiniteItems())) {
                                            current.gameRenderer.itemInHandRenderer.itemUsed(interactionhand);
                                        }
                                    }

                                    return;
                                }

                                if (interactionresult1 == InteractionResult.FAIL) {
                                    return;
                                }
                        }
                    }

                    if (itemstack.isEmpty() && (current.hitResult == null || current.hitResult.getType() == HitResult.Type.MISS))
                        net.minecraftforge.common.ForgeHooks.onEmptyClick(current.player, interactionhand);

                    if (!itemstack.isEmpty()) {
                        InteractionResult interactionresult2 = current.gameMode.useItem(current.player, interactionhand);
                        if (interactionresult2.consumesAction()) {
                            if (interactionresult2.shouldSwing()) {
                                current.player.swing(interactionhand);
                            }

                            current.gameRenderer.itemInHandRenderer.itemUsed(interactionhand);
                            return;
                        }
                    }
                }

            }
        }
    }

    @Override
    public void setOverrideBlockHitResult(@Nullable BlockHitResult pResult) {
        overrideBHR = pResult;
    }

    @Override
    public void setOverrideEntityHitResult(@Nullable EntityHitResult pResult) {
        overrideEHR = pResult;
    }
}
