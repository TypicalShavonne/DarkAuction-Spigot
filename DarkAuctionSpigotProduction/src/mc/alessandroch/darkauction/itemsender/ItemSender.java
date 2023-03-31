package mc.alessandroch.darkauction.itemsender;

import org.bukkit.Location;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;




public interface ItemSender {

	
	public void createItm(ItemStack item,Location loc);
	
	public void sendItem(Player player, ItemStack item,Location loc);
    

	public void removeItem(Player player, ItemStack currentlyItem, Location loc);
}
