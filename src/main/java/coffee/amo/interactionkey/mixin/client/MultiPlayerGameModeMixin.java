package coffee.amo.interactionkey.mixin.client;

import coffee.amo.interactionkey.client.IYeahIHaveNoIdea;
import coffee.amo.interactionkey.net.InteractionServerboundUseItemOnPacket;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin implements IYeahIHaveNoIdea {
    @Override
    public InteractionResult useItemOn(LocalPlayer pPlayer, InteractionHand pHand, BlockHitResult pResult) {
        MultiPlayerGameMode current = (MultiPlayerGameMode) (Object) this;
        current.ensureHasSentCarriedItem();
        if (!current.minecraft.level.getWorldBorder().isWithinBounds(pResult.getBlockPos())) {
            return InteractionResult.FAIL;
        } else {
            MutableObject<InteractionResult> mutableobject = new MutableObject<>();
            current.startPrediction(current.minecraft.level, (p_233745_) -> {
                mutableobject.setValue(current.performUseItemOn(pPlayer, pHand, pResult));
                return new InteractionServerboundUseItemOnPacket(pHand, pResult, p_233745_);
            });
            return mutableobject.getValue();
        }
    }
}
