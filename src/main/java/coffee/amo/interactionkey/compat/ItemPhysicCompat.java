package coffee.amo.interactionkey.compat;

import coffee.amo.interactionkey.util.ItemUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import team.creative.itemphysic.ItemPhysic;
import team.creative.itemphysic.common.packet.PickupPacket;

public class ItemPhysicCompat {
    public static void playerPickupInteract(Player player){
        ItemEntity closestItem = ItemUtil.getClosestItem(player);
        if(closestItem != null && player.level.isClientSide){
            ItemPhysic.NETWORK.sendToServer(new PickupPacket(closestItem.getUUID(), true));
        }
    }

    public static void doPickup(ItemEntity itemEntity, Player player){
        if(player.level.isClientSide){
            ItemPhysic.NETWORK.sendToServer(new PickupPacket(itemEntity.getUUID(), true));
        }
    }
}
