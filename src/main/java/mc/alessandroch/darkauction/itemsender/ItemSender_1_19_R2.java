package mc.alessandroch.darkauction.itemsender;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;


public class ItemSender_1_19_R2 implements ItemSender {

    //EntityItem idd = null;
    Item item = null;

    @Override
    public void createItm(ItemStack item, Location loc) {
    }
    @Override
    public void sendItem(Player player, ItemStack item2, Location loc) {
        if (item == null) {
            item = player.getWorld().dropItem(loc, item2);
            item.setVelocity(new Vector(0, 0, 0));
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setCustomName("DA-Item");
            item.setCustomNameVisible(false);
        }
    }

    @Override
    public void removeItem(Player player, ItemStack currentlyItem, Location loc) {
        if (item != null) {
            item.remove();
            item = null;
        }
    }
}
