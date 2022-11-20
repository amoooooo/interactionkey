package coffee.amo.interactionkey.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

// Does nothing until i figure out what to do with it.
public class InteractableBlockEntity extends BlockEntity implements Interactable {
    public BlockEntity blockEntity;

    public InteractableBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public void interact() {

    }

    @Override
    public String getInteractableName() {
        return this.blockEntity.getLevel().getBlockState(this.blockEntity.getBlockPos()).getBlock().getName().getString();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void interact(Player player) {

    }
}
