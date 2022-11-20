package coffee.amo.interactionkey.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

// Also does nothing unless you call it inside the interact event
public class InteractableEntity extends Entity implements Interactable {
    public Entity entity;

    public InteractableEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public String getInteractableName() {
        return null;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return null;
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
