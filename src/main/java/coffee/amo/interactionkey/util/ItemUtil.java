package coffee.amo.interactionkey.util;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class ItemUtil {
    public static ItemEntity getClosestItem(Player player) {
        return player.level.getEntities(null, player.getBoundingBox().inflate(2)).stream().map(s -> s instanceof ItemEntity? (ItemEntity) s : null).filter(Objects::nonNull).min((s1, s2) -> {
            double d1 = player.distanceToSqr(s1);
            double d2 = player.distanceToSqr(s2);
            return Double.compare(d1, d2);
        }).orElse(null);
    }
}
