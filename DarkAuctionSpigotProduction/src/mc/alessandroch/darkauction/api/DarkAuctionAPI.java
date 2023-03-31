package mc.alessandroch.darkauction.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mc.alessandroch.darkauction.DarkAuction;
import mc.alessandroch.darkauction.HologramAPI;
import mc.alessandroch.darkauction.Offer;
import mc.alessandroch.darkauction.gui.GuiManager;


public class DarkAuctionAPI {

	
	
	/*@Deprecated
	public static ItemSender getItemSender() {
		return DarkAuction.get().itemSender;
	 	
	}*/
	public static int getTimeSeconds() {
		return DarkAuction.get().time;
	 	
	}
	public static Offer getPlayerOffer(Player player) {
		return DarkAuction.get().getOffer(player);
	 	
	}
	public static void startAuction() {
		DarkAuction.get().startAuction();
	 	
	}
	public static void setCustomHologramAPI(HologramAPI api) {
		HologramAPI.setAPI(api);
	}
	public static void setCustomHologramAPIwhileRunning(HologramAPI api) {
		if(api == null) {
			return;
		}
		DarkAuction.get().hologramAPI = api;
		HologramAPI.setAPI(api);
	}
	public static void reloadDarkAuction() {
		DarkAuction.get().reload();
	}
	public static void stopAuction() {
		DarkAuction.get().finishAuction();
	 	
	}
	public static GuiManager getGuiManager() {
		return DarkAuction.get().guiManager;
	 	
	}
	public static String getPluginVersion() {
		return DarkAuction.get().version;
	}
	public static String getBukkitAPIVersion() {
		return DarkAuction.get().apiversion;
	}
	public static void notifyPlayers(String message) {
		DarkAuction.get().notifyPlayers(message);
		
	}
	public static Offer getHigherOffer() {
		return DarkAuction.get().getHigherOffer();
	}
	public static boolean isInProgress() {
		return DarkAuction.get().isStarted;
	}
	public static ItemStack getCurrentItem() {
		return DarkAuction.get().currentlyItem;
	}
	static {
		
	}
}
