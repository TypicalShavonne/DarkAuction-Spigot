package mc.alessandroch.darkauction.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import mc.alessandroch.darkauction.api.DarkAuctionAPI;




public class GuiManager {

	public GuiManager() {
		registerGuiName("DarkAuction [Menu]");
		registerGuiName("DarkAuction [Menu->Item List]");
		registerGuiName("DarkAuction [Menu->Add item]");
		registerGuiName("DarkAuction [Menu->Delete item List]");
		registerGuiName("DarkAuction [Menu->Give item List]");
	}
	public List<String> guinames = new ArrayList<String>();
	public void openMainGui(Player player) {
		Inventory inv = Bukkit.createInventory(null, 9, "DarkAuction [Menu]");
		Bukkit.getScheduler().runTaskAsynchronously(DarkAuction.get(), () -> {
		
		inv.setItem(0, createGuiItem(Material.PAPER,"�eItem list","�7Open Item list page"));
		inv.setItem(1, createGuiItem(Material.LIME_DYE,"�eAdd new item","�7Add a new item into the","�7list by pressing an item","�7in your inventory"));
		inv.setItem(2, createGuiItem(Material.BARRIER,"�cRemove an item","�7Remove the item","�7you click in the list"));
		inv.setItem(3, createGuiItem(Material.ARROW,"�eGive an item","�7Give the item","�7you click in the list"));
		inv.setItem(4, createGuiItem(Material.OAK_SLAB,"�eSet location","�7Set the location","�7of the hologram"));
		inv.setItem(5, createGuiItem(Material.BLACK_STAINED_GLASS_PANE,"�7"));
		inv.setItem(6, createGuiItem(Material.GREEN_DYE,"�aForce start","�7Force start the","�7auction"));
		inv.setItem(7, createGuiItem(Material.RED_DYE,"�cForce stop","�7Force stop the","�7auction"));
		inv.setItem(8, createGuiItem(Material.OAK_SIGN,"�eInformations","�7Plugin version: "+checkVersion(),"�7Server version: �e"+Bukkit.getVersion(),"�7API version: �e"+DarkAuction.get().apiversion));
		});
		player.openInventory(inv);
	}
	private String checkVersion() {
		
		if(DarkAuction.get().updater.updateCheckNewAvailable()) {
			return "�c"+DarkAuction.get().version+"�e�l NEW UPDATE AVAILABLE";
		}else {
			return "�a"+DarkAuction.get().version+"";
		}

	}
	public void openAddGui(Player player) {
		Inventory inv = Bukkit.createInventory(null, 9, "DarkAuction [Menu->Add item]");
		Bukkit.getScheduler().runTaskAsynchronously(DarkAuction.get(), () -> {
		
		inv.setItem(0, createGuiItem(Material.BARRIER,"�cBack"));
		inv.setItem(1, createGuiItem(Material.LIME_DYE,"�eAdd an item","�7Add an item by pressing","�7it in your inventory"));
		
		
		});
		player.openInventory(inv);
	}
	public void openListGui(Player player, int p) {
		Inventory inv = Bukkit.createInventory(null, 9*3, "DarkAuction [Menu->Item List]");
		Bukkit.getScheduler().runTaskAsynchronously(DarkAuction.get(), () -> {
		
		inv.setItem(getLoc(0,2), createGuiItem(Material.PAPER,"�ePage back"));
		inv.setItem(getLoc(4,2), createGuiItem(Material.LIME_DYE,"�ePage number",""+p));
		inv.setItem(getLoc(5,2), createGuiItem(Material.BARRIER,"�cBack"));
		inv.setItem(getLoc(8,2), createGuiItem(Material.PAPER,"�ePage forward"));
		
		
		int page = p-1;
		int count = 9*2;
		for(int i = 0; i < 9*2; i++) {
			try {
				if(DarkAuction.get().allItems.size() >= i+count*page) {
					ItemStack is = DarkAuction.get().allItems.get(i+count*page);
					
						inv.setItem(i, is);
					
					
					
				}else {
					inv.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE,"�7"));
				}
			}catch (Exception e){
				inv.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE,"�7"));
			}
			
		}
		});
		player.openInventory(inv);
	}
	public void openDeleteListGui(Player player, int p) {
		Inventory inv = Bukkit.createInventory(null, 9*3, "DarkAuction [Menu->Delete item List]");
		Bukkit.getScheduler().runTaskAsynchronously(DarkAuction.get(), () -> {
		
		inv.setItem(getLoc(0,2), createGuiItem(Material.PAPER,"�ePage back"));
		inv.setItem(getLoc(4,2), createGuiItem(Material.LIME_DYE,"�ePage number",""+p));
		inv.setItem(getLoc(5,2), createGuiItem(Material.BARRIER,"�cBack"));
		inv.setItem(getLoc(8,2), createGuiItem(Material.PAPER,"�ePage forward"));
		
		
		int page = p-1;
		int count = 9*2;
		for(int i = 0; i < 9*2; i++) {
			try {
				if(DarkAuction.get().allItems.size() >= i+count*page) {
					ItemStack is = DarkAuction.get().allItems.get(i+count*page);
					/*ItemMeta meta = is.getItemMeta();
					NamespacedKey pos = new NamespacedKey(DarkAuction.get(), "position");
					meta.getCustomTagContainer().setCustomTag(pos, ItemTagType.INTEGER,i+count*page);
					is.setItemMeta(meta);	*/
					inv.setItem(i, is);
					    
					
					
				}else {
					inv.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE,"�7"));
				}
			}catch (Exception e){
				inv.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE,"�7"));
			}
			
		}
		});
		player.openInventory(inv);
	}
	public void openGiveListGui(Player player, int p) {
		Inventory inv = Bukkit.createInventory(null, 9*3, "DarkAuction [Menu->Give item List]");
		Bukkit.getScheduler().runTaskAsynchronously(DarkAuction.get(), () -> {
		
		inv.setItem(getLoc(0,2), createGuiItem(Material.PAPER,"�ePage back"));
		inv.setItem(getLoc(4,2), createGuiItem(Material.LIME_DYE,"�ePage number",""+p));
		inv.setItem(getLoc(5,2), createGuiItem(Material.BARRIER,"�cBack"));
		inv.setItem(getLoc(8,2), createGuiItem(Material.PAPER,"�ePage forward"));
		
		
		int page = p-1;
		int count = 9*2;
		for(int i = 0; i < 9*2; i++) {
			try {
				if(DarkAuction.get().allItems.size() >= i+count*page) {
					ItemStack is = DarkAuction.get().allItems.get(i+count*page);
					/*ItemMeta meta = is.getItemMeta();
					NamespacedKey pos = new NamespacedKey(DarkAuction.get(), "position");
					meta.getCustomTagContainer().setCustomTag(pos, ItemTagType.INTEGER,i+count*page);
					is.setItemMeta(meta);	*/
					inv.setItem(i, is);
					    
					
					
				}else {
					inv.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE,"�7"));
				}
			}catch (Exception e){
				inv.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE,"�7"));
			}
			
		}
		});
		player.openInventory(inv);
	}
	public boolean checkPageAvailable(int pg,boolean forward) {
		if(forward) {
			int pgg = pg-1;
			return DarkAuction.get().allItems.size() > ((9 * 2) * pgg);
		}else {
			return pg > 1;
		}
	}
	public int getLoc(int x, int y) {
		return (9*y)+x;
	}
	public void registerGuiName(String name) {
		guinames.add(name);
		
	}
	public void guiClick(String guiname,Player player, ItemStack item,Inventory inv) {
		String itemname = item.getItemMeta().getDisplayName();
		if(guiname.equals("DarkAuction [Menu]")) {
			if(itemname.equals("�eItem list")) {
				openListGui(player,1);
			}
			if(itemname.equals("�eAdd new item")) {
				openAddGui(player);
			}
			if(itemname.equals("�cRemove an item")) {
				openDeleteListGui(player,1);
			}
			if(itemname.equals("�eGive an item")) {
				openGiveListGui(player,1);
			}
			if(itemname.equals("�eSet location")) {
				player.sendMessage("�eLocation changed correctly in your position!");
				DarkAuction.get().loc = player.getLocation().clone();
			}
			if(itemname.equals("�aForce start")) {
				player.closeInventory();
				if(!DarkAuction.get().isStarted)DarkAuctionAPI.startAuction();
			}
			if(itemname.equals("�cForce stop")) {
				player.closeInventory();
				if(DarkAuction.get().isStarted)DarkAuctionAPI.stopAuction();
			}
			
		}else if(guiname.equals("DarkAuction [Menu->Item List]")) {
			if(itemname.equals("�cBack")) {
				openMainGui(player);
			}
			if(itemname.equals("�ePage back")) {
				int pagenumber = Integer.parseInt(inv.getItem(getLoc(4,2)).getItemMeta().getLore().get(0));
				if(checkPageAvailable(pagenumber,false)) {
					openListGui(player,pagenumber-1);
				}
			}
			if(itemname.equals("�ePage forward")) {
				int pagenumber = Integer.parseInt(inv.getItem(getLoc(4,2)).getItemMeta().getLore().get(0));
				if(checkPageAvailable(pagenumber+1,true)) {
					openListGui(player,pagenumber+1);
				}
			}
		}else if(guiname.equals("DarkAuction [Menu->Add item]")) {
			if(itemname.equals("�cBack")) {
				openMainGui(player);
			}else if(itemname.equals("�eAdd an item")) {
				
			}else {
				DarkAuction.get().allItems.add(item);
				player.sendMessage("�aItem added correctly!");
			    openListGui(player,1);
			}
			
		}else if(guiname.equals("DarkAuction [Menu->Delete item List]")) {
			if(itemname.equals("�cBack")) {
				openMainGui(player);
			}else if(itemname.equals("�ePage back")) {
				int pagenumber = Integer.parseInt(inv.getItem(getLoc(4,2)).getItemMeta().getLore().get(0));
				if(checkPageAvailable(pagenumber,false)) {
					openDeleteListGui(player,pagenumber-1);
				}
			}else
			if(itemname.equals("�ePage forward")) {
				int pagenumber = Integer.parseInt(inv.getItem(getLoc(4,2)).getItemMeta().getLore().get(0));
				if(checkPageAvailable(pagenumber+1,true)) {
					openDeleteListGui(player,pagenumber+1);
				}
			}else if(itemname.equals("�ePage number")){
				
			}else if(itemname.equals("�7")){
				
			}else {
				ItemMeta meta = item.getItemMeta();
				NamespacedKey pos = new NamespacedKey(DarkAuction.get(), "position");
				int pagenumber = Integer.parseInt(inv.getItem(getLoc(4,2)).getItemMeta().getLore().get(0));
				DarkAuction.get().allItems.remove(item) ;
				player.sendMessage("�cItem removed!");
				openDeleteListGui(player,pagenumber);
			}
		}else if(guiname.equals("DarkAuction [Menu->Give item List]")) {
			if(itemname.equals("�cBack")) {
				openMainGui(player);
			}else if(itemname.equals("�ePage back")) {
				int pagenumber = Integer.parseInt(inv.getItem(getLoc(4,2)).getItemMeta().getLore().get(0));
				if(checkPageAvailable(pagenumber,false)) {
					openGiveListGui(player,pagenumber-1);
				}
			}else
			if(itemname.equals("�ePage forward")) {
				int pagenumber = Integer.parseInt(inv.getItem(getLoc(4,2)).getItemMeta().getLore().get(0));
				if(checkPageAvailable(pagenumber+1,true)) {
					openGiveListGui(player,pagenumber+1);
				}
			}else if(itemname.equals("�ePage number")){
				
			}else if(itemname.equals("�7")){
				
			}else {
				ItemMeta meta = item.getItemMeta();
				NamespacedKey pos = new NamespacedKey(DarkAuction.get(), "position");
				int pagenumber = Integer.parseInt(inv.getItem(getLoc(4,2)).getItemMeta().getLore().get(0));
				player.getInventory().addItem(item);
				player.sendMessage("�aItem gived!");
				openGiveListGui(player,pagenumber);
			}
		}
	}
	public static ItemStack createGuiItem(Material material, String name, String... lore ) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);
       
        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }
}
