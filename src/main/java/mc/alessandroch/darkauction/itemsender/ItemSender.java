package mc.alessandroch.darkauction.itemsender;

import org.bukkit.Location;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;




public interface ItemSender {

	
	void createItm(ItemStack item,Location loc);
	
	void sendItem(Player player, ItemStack item,Location loc);
    

	void removeItem(Player player, ItemStack currentlyItem, Location loc);
}
