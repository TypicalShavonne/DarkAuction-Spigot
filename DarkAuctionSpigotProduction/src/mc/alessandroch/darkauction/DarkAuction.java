package mc.alessandroch.darkauction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import org.bukkit.Sound;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


import mc.alessandroch.darkauction.events.AuctionEndEvent;
import mc.alessandroch.darkauction.events.AuctionStartEvent;
import mc.alessandroch.darkauction.events.ModifyOfferEvent;
import mc.alessandroch.darkauction.events.NewOfferEvent;
import mc.alessandroch.darkauction.events.SetHologramAPIEvent;
import mc.alessandroch.darkauction.gui.GuiManager;
import mc.alessandroch.darkauction.itemsender.ItemSender;

import mc.alessandroch.darkauction.itemsender.ItemSender_1_13_R2;
import mc.alessandroch.darkauction.itemsender.ItemSender_1_14_R1;
import mc.alessandroch.darkauction.itemsender.ItemSender_1_15_R1;
import mc.alessandroch.darkauction.itemsender.ItemSender_1_16_R2;
import mc.alessandroch.darkauction.itemsender.ItemSender_1_16_R3;
import mc.alessandroch.darkauction.itemsender.ItemSender_1_17_R1;
import mc.alessandroch.darkauction.itemsender.ItemSender_1_18_R1;
import mc.alessandroch.darkauction.itemsender.ItemSender_1_18_R2;
import mc.alessandroch.darkauction.itemsender.ItemSender_1_19_R1;
import mc.alessandroch.darkauction.itemsender.ItemSender_1_19_R2;
import mc.alessandroch.darkauction.metrics.Metrics;
import mc.alessandroch.darkauction.updater.UpdateChecker;
import net.milkbowl.vault.economy.Economy;

public class DarkAuction extends JavaPlugin implements Listener{

	public Location loc;
	public List<ItemStack> allItems = new ArrayList<ItemStack>();
	public ItemStack currentlyItem;
	private static DarkAuction instance;
	public boolean isStarted = false;
	public Economy econ = null;
	public int time = 60*60; //seconds*minutes
	public int initialCost = 2000;
	public ItemSender itemSender;
	public String version = "beta1.3.3_2";
	public String apiversion = "v??";
	public String materialname = "";
	public UpdateChecker updater;
	public Sound sound;
	public List<Offer> offers = new ArrayList<Offer>();
	public GuiManager guiManager;
	public HologramAPI hologramAPI;
	public boolean debug = false;
	FileConfiguration config;
	
	Inventory inv = Bukkit.createInventory(null,9*6,"");
	public static DarkAuction get() {
		return instance;
	}
	public void onDisable() {
		if(hologramAPI == null) return;
		for(Player plr : getServer().getOnlinePlayers()) {
			itemSender.removeItem(plr, currentlyItem, loc);
		}
		save();
	}
	public void setUpMessages() {
		setup("MESSAGES.not-started-auction-msg","&0Auction not started");
		
		
		inv = Bukkit.createInventory(null,9*6,getString("MESSAGES.not-started-auction-msg"));
		setup("MESSAGES.offer-msg-gui","&cOffer");
		setup("MESSAGES.darkauction-prefix","&9[DarkAuction]");
		setup("MESSAGES.remaining-time-msg","&fRemaining ");
		setup("MESSAGES.dark-auction-endin","&eAuction ends in ");
		setup("MESSAGES.no-money-for-offer","&CYou do not have enough money to make the offer");
		setup("MESSAGES.no-money-for-raise-offer","&cYou don't have enough money to raise the offer");
		setup("MESSAGES.initialOffer-hologram-msg","Initial offer:");
		setup("MESSAGES.currentOffer-hologram-msg","Current offer:");
		setup("MESSAGES.method-for-see-theitem-hologram-msg","Run /openauction to see the item");
		setup("MESSAGES.auctionstarts-in-hologram-msg","&fThe auction starts in");
		setup("MESSAGES.auctionend-winner","The auction has ended! the winner is &e{playername} &f for offering &a${offer}");
		setup("MESSAGES.auctionend-nowinner","The auction has ended! Nobody won the item because there were no bids!");
		setup("MESSAGES.auctionstartednew","A new illegal auction has started!");
		setup("MESSAGES.newbid","&c{playername}&f has bid &a${offer}&f for the auction item!");
		setup("MESSAGES.bidincreased","&c{playername}&f has increased the bid to &a${offer}&f for the auction item!");
		setup("MESSAGES.currencysymbol","$");
		
		
	}
	public String getString(String string) {
		
	
		return config.getString(string).replace("&", "§");
	}
	private void setup(String string, String string2) {
		
		File file = new File(getDataFolder(), "messages.yml");
	    config = YamlConfiguration.loadConfiguration(file);
		if(config.get(string) == null) {
			config.set(string, string2);
			try {
				config.save(file);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
	}

	public boolean getItemEconomy() {
		if(getConfig().get("item-economy") == null) {
			getConfig().set("item-economy", false);
			getConfig().set("item-material-economy", Material.EMERALD.name());
			materialname = getConfig().getString("item-material-economy");
			saveConfig();
		}
		return getConfig().getBoolean("item-economy");
	}
	public Material getMaterialEco() {
		return Material.getMaterial(materialname);
		
	}
	
	public void onEnable() {
		 
		  itemSender = setupItemSender();
		 
		  updater = new UpdateChecker(this);
           if(itemSender == null) {
			  
			  
			  return;
		  }
           Metrics metrics = new Metrics(this, 12953);
	
	        instance = this;
	   	
		if (!setupEconomy() && getItemEconomy() == false ) {
        	this.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
        	this.getLogger().severe(String.format("If you are not using Vault, enable item economy from config!"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		if(getItemEconomy() == true) {
			this.getLogger().info(String.format("Enabling with Item Based Economy "));
			this.getLogger().info(String.format("This Economy is still in BETA"));
		}else {
			 EconomyHelper.vaultversion = getServer().getPluginManager().getPlugin("Vault").getDescription().getVersion();
			this.getLogger().info(String.format("Enabling with Vault Economy system"));
		}
		
		SetHologramAPIEvent event = new SetHologramAPIEvent();
		 Bukkit.getPluginManager().callEvent(event);
		 if(event.getAPI() != null) {
			
			 
			 hologramAPI = event.getAPI();
			 try {
				 event.getAPI().start();
			 }catch(Exception e) {
				 hologramAPI = null;
				 this.getLogger().severe("Error while starting custom hologram system:");
				 this.getLogger().severe(e.getMessage());
				 disableCustom();
		            return;
			 }
		 }else {
			 hologramAPI = HologramAPI.newAPI();
			 if(hologramAPI == null) {
					this.getLogger().severe(String.format("[%s] - Disabled due to no Holographic plugin dependency found!", getDescription().getName()));
		        	
					disableCustom();
		            return;
				}
				hologramAPI.start();
		 }
		
		
		setUpMessages();
		BukkitTask task = new BukkitRunnable() {
    	    public void run() 
    	    {
    	    	if(loc != null)updateHologram();
    	    	if(isStarted == false) {
    	    		time -= 1;
    	    		
    	    		inv= Bukkit.createInventory(null, 9*6, getString("MESSAGES.not-started-auction-msg")); //owner can be "null" or a player
    	    		setupInv(inv);
    	    		if(time < 1) {
    	    			startAuction();
    	    		}
    	    	}else {
    	    		time -= 1;
    	    		
    	    		offerChecker();
    	    		addOffering();
    	    		if(time < 1) {
    	    			finishAuction();
    	    		}
    	    	}
    	    	
    	    }

			

			
    	}.runTaskTimer(this,20,20);
    	 BukkitTask task2 = new BukkitRunnable() {
	      
    		 
	            @Override
	            public void run() {
	                try {
	                loadAll();
	                updater.startUpdateCheck(null);
	                if(loc != null && isStarted == true)itemSender.createItm(currentlyItem, loc);
	                for(Player plr : getServer().getOnlinePlayers()) {
	        			if(loc != null && isStarted == true)itemSender.sendItem(plr, currentlyItem, loc);
	        		}
	                setupInv(inv);
	                }catch(Exception e) {
	                	e.getStackTrace();
	                }

	            }

				
	           
	        }.runTaskLaterAsynchronously(this, 50);
	        this.getServer().getPluginManager().registerEvents(this, this);
	        this.getCommand("darkauction").setExecutor(this);
	        this.getCommand("openauction").setExecutor(this);
	}
	private ItemSender setupItemSender() {

		 String version;
         ItemSender sender = null;
	        try {

	            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

	        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
	        	getLogger().severe("Disabling DarkAuction cause:");
	        	getLogger().severe("Unable to get minecraft server version");
	        	getLogger().severe("Use at least v1_13_R2");
	        	getServer().getPluginManager().disablePlugin(this);
	        	return null;
	        }

	        getLogger().info("Your server is running version " + version);

	        
	        if (version.equals("v1_19_R4")) {
	            //server is running 1.19.4+
	        	sender = new ItemSender_1_19_R1();
	        	apiversion = "v1_19_R4";
	        	sound = Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON;
	        }else if (version.equals("v1_19_R3")) {
	            //server is running 1.19.4+
	        	sender = new ItemSender_1_19_R1();
	        	apiversion = "v1_19_R3";
	        	sound = Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON;
	        }else if (version.equals("v1_19_R2")) {
	            //server is running 1.19.3+
	        	sender = new ItemSender_1_19_R1();
	        	apiversion = "v1_19_R2";
	        	sound = Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON;
	        }else if (version.equals("v1_19_R1")) {
	            //server is running 1.19+
	        	sender = new ItemSender_1_19_R1();
	        	apiversion = "v1_19_R1";
	        	sound = Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON;
	        }else if (version.equals("v1_18_R2")) {
	            //server is running 1.18
	        	sender = new ItemSender_1_18_R2();
	        	apiversion = "v1_18_R2";
	        	sound = Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON;
	        }else if (version.equals("v1_18_R1")) {
	            //server is running 1.18
	        	sender = new ItemSender_1_18_R1();
	        	apiversion = "v1_18_R1";
	        	sound = Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON;
	        }else if (version.equals("v1_17_R1")) {
	            //server is running 1.17.1
	        	sender = new ItemSender_1_17_R1();
	        	apiversion = "v1_17_R1";
	        	sound = Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON;
	        }else if (version.equals("v1_16_R3")) {
	            //server is running 1.16.4+
	        	sender = new ItemSender_1_16_R3();
	        	apiversion = "v1_16_R3";
	        	sound = Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON;
	        } else if (version.equals("v1_16_R2")) {
	            //server is running 1.16.2+ 
	        	sender = new ItemSender_1_16_R2();
	        	apiversion = "v1_16_R2";
	        	sound = Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON;
	        } else if (version.equals("v1_15_R1")) {
	            //server is running 1.15+ 
	        	sender = new ItemSender_1_15_R1();
	        	apiversion = "v1_15_R1";
	        	sound = Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON;
	        } else if (version.equals("v1_14_R1")) {
	            //server is running 1.14+
	        	sender = new ItemSender_1_14_R1();
	        	apiversion = "v1_14_R1";
	        	sound = Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON;
	        } else if (version.equals("v1_13_R2")) {
	            //server is running 1.14+
	        	apiversion = "v1_13_R2";
	        	sound = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
	        	sender = new ItemSender_1_13_R2();
	        } /*else if (version.equals("v1_12_R1")) {
	            //server is running 1.14+
	        	apiversion = "v1_12_R1";
	        	sound = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
	        	sender = new ItemSender_1_12_R1();
	        }*/ else {
	        	getLogger().severe("Version " + version+" isn't supported for ItemSender");
	        	getLogger().severe("Disabling DarkAuction cause:");
	        	getLogger().severe("Unsupported Minecraft version, please update to at least");
	        	getLogger().severe("version 1_13_R2 (1.13.2)");
	        	apiversion = version;
	        	getServer().getPluginManager().disablePlugin(this);
	        	return null;
	        }
	       guiManager = new GuiManager();
	        
		return sender;
	}
	private void loadAll() {
		
		File file = new File(getDataFolder(), "config.yml");
		try {
			getConfig().load(file);
			
		}catch(Exception e) {
			return;
		}
		if(getConfig().get("isStarted") != null) isStarted = getConfig().getBoolean("isStarted");
		if(getConfig().get("time") != null) time = getConfig().getInt("time");
		if(getConfig().get("currentlyItem") != null) currentlyItem = getConfig().getItemStack("currentlyItem");
		if(getConfig().get("loc") != null) loc = getConfig().getLocation("loc");
		if(getConfig().get("allItems") != null) allItems = (List<ItemStack>) getConfig().getList("allItems");
		setUpMessages();
		if(getConfig().get("initialCost") == null) {
			getConfig().set("initialCost", 2000);
			initialCost = getConfig().getInt("initialCost");
			saveConfig();
		}else {
			initialCost = getConfig().getInt("initialCost");
		}
		materialname = getConfig().getString("item-material-economy");
		if(getConfig().get("durationauction-in-seconds") == null) {
			getConfig().set("durationauction-in-seconds", 60*5);
			
			
			saveConfig();
		}
		if(getConfig().get("startauction-in-seconds") == null) {
			getConfig().set("startauction-in-seconds", 60*20);
			
			time = getConfig().getInt("startauction-in-seconds");
			saveConfig();
		}
		if(getConfig().get("debug-mode") == null) {
			getConfig().set("debug-mode", debug);
			
			debug = getConfig().getBoolean("debug-mode");
			
		}
		
	}
	public void disableCustom() {
		this.setEnabled(false);
		
	}
	public void reload() {
		if(!this.isEnabled()) {
			try {
				this.setEnabled(true);
				//onEnable();
				
			}catch(Exception e) {
				disableCustom();
			}
			
		}else {
			File file = new File(getDataFolder(), "config.yml");
			try {
				getConfig().load(file);
				initialCost = getConfig().getInt("initialCost");
				materialname = getConfig().getString("item-material-economy");
				debug = getConfig().getBoolean("debug-mode");
				  
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
		
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				
				e.printStackTrace();
			}
			file = new File(getDataFolder(), "messages.yml");
			config = YamlConfiguration.loadConfiguration(file);
		}
		
	}
	public void save() {
		getConfig().set("isStarted",isStarted);
		getConfig().set("time",time);
		getConfig().set("currentlyItem",currentlyItem);
		getConfig().set("loc",loc);
		getConfig().set("allItems",allItems);
		getConfig().set("debug-mode", debug);
		saveConfig();
	}
	private void addOffering() {
		
		Bukkit.getScheduler().runTaskAsynchronously(DarkAuction.get(), () -> {
		Offer offer = getHigherOffer();
		double money = 0;
		if(offer == null) money = initialCost;
		if(offer != null) money = offer.offer+initialCost;
		//TODO Implement customizable position and more offer options
		inv.setItem(4, currentlyItem);
		inv.setItem(5, makeTime());
		inv.setItem(9*3+4, createGuiItem(Material.GOLD_INGOT,getString("MESSAGES.offer-msg-gui"),"§a"+money+getString("MESSAGES.currencysymbol")));
		});
		}

	private ItemStack makeTime() {
		
		 int p1 = time % 60;
	        int p2 = time / 60;
	        int p3 = p2 % 60;
	        p2 = p2 / 60;
		return createGuiItem(Material.CLOCK,getString("MESSAGES.remaining-time-msg")+" §f"+p2 + "h " + p3 + "m " + p1+"s");
	}
	 @EventHandler
	    public void onInventoryClick(InventoryClickEvent e) {
	        

	        //e.getAction().
			
	        ItemStack clickedItem = e.getCurrentItem();
	        Inventory inventory = e.getInventory();
	        // verify current item is not null
	      // if( e.getClick().equals(ClickType.SWAP_OFFHAND) && )e.setCancelled(true);
	        if (clickedItem == null || clickedItem.getType().equals(Material.AIR)) return;

	        Player p = (Player) e.getWhoClicked();
	        
	       String name = "null";
	       if(currentlyItem != null) name = getName(currentlyItem);
	       double actualoffer = 0;
	       Offer offer = getHigherOffer();
	       if(offer != null) actualoffer = offer.offer;
	       if(e.getView().getTitle().equals("§0"+name) || e.getView().getTitle().equals(getString("MESSAGES.not-started-auction-msg")))
	        {
	        	e.setCancelled(true);
	        
	        	p.playSound(p.getLocation(), sound, 1, 1);
	        	if(clickedItem.getItemMeta().getDisplayName().equals(getString("MESSAGES.offer-msg-gui"))) {
	        		makeAction(p,actualoffer+initialCost);
	        	}
	        }else if(guiManager.guinames.contains(e.getView().getTitle())) {
	        	e.setCancelled(true);
		        
	        	p.playSound(p.getLocation(),  sound, 1, 1);
	        	guiManager.guiClick(e.getView().getTitle(), p, clickedItem,inventory);
	        }
	    }
	 protected static ItemStack createGuiItem(Material material, String name, String... lore ) {
	        final ItemStack item = new ItemStack(material, 1);
	        final ItemMeta meta = item.getItemMeta();

	        // Set the name of the item
	        meta.setDisplayName(name);
        
	        // Set the lore of the item
	        meta.setLore(Arrays.asList(lore));

	        item.setItemMeta(meta);

	        return item;
	    }
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
        Player player = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("darkauction")) {
        	
        	if(args.length > 0) {
        		if(args[0].equals("additem")  && player.hasPermission("darkauction.admin")) {
        			allItems.add(new ItemStack(player.getInventory().getItemInMainHand().clone()));
        			player.sendMessage("§aItem added to auction");
        			return true;
        		}
        		if(args[0].equals("removeitem")  && player.hasPermission("darkauction.admin")) {
        			if(args.length > 1) {
        				try {
        				int i = Integer.parseInt(args[1]);
        				
        				allItems.remove(i);
        				player.sendMessage("§aItem removed! check the modified list with /darkauction info");
        				
        				}catch(Exception e) {
        					player.sendMessage("§cAn error occured. Are you sure you put a number or maybe the list is too short?");
        				}
        			}else {
        				player.sendMessage("§cUsage: /darkauction removeitem (number)");	
        			}
        			
        			
        			return true;
        		}
        		if(args[0].equals("giveitem")  && player.hasPermission("darkauction.admin")) {
        			if(args.length > 1) {
        				try {
        				int i = Integer.parseInt(args[1]);
        				
        				ItemStack stack = allItems.get(i).clone();
        				player.getInventory().addItem(stack);
        				player.sendMessage("§aItem gived!");
        				
        				}catch(Exception e) {
        					player.sendMessage("§cAn error occured. Are you sure you put a number or maybe the list is too short?");
        				}
        			}else {
        				player.sendMessage("§cUsage: /darkauction giveitem (number)");	
        			}
        			
        			
        			return true;
        		}
        		if(args[0].equals("info")  && player.hasPermission("darkauction.admin")) {
        			
        			player.sendMessage("Items:");
        			int i = -1;
        			for(ItemStack stack : allItems) {
        				i++;
        				player.sendMessage("("+i+") "+getName(stack));
        			}
        			return true;
        		}
                 if(args[0].equals("setlocation") && player.hasPermission("darkauction.admin")) {
        			
                	 loc = player.getLocation();
                	 player.sendMessage("Location changed");
                	 return true;
        		}
                 if(args[0].equals("reload")  && player.hasPermission("darkauction.admin")) {
                	 reload();
                	 player.sendMessage("Config and messages reloaded");
                	 return true;
                 }
                 if(args[0].equals("forcestart")  && player.hasPermission("darkauction.admin")) {
                	
                	 player.sendMessage("Force start...");
                	 if(!isStarted) startAuction();
                	 return true;
                 }
                 if(args[0].equals("forcestop")  && player.hasPermission("darkauction.admin")) {
                 	
                	 player.sendMessage("Force stop...");
                	 if(isStarted)finishAuction();
                	 return true;
                 }
                 if(args[0].equals("checkforupdate")  && player.hasPermission("darkauction.admin")) {
                  	
                	 player.sendMessage("DarkAuction Actual version is "+version);
                	 updater.startUpdateCheck(player);
                	 
                	 return true;
                 }
                 if(args[0].equals("gui")  && player.hasPermission("darkauction.admin")) {
                   	
                	 
                	 guiManager.openMainGui(player);
                	 
                	 return true;
                 }
                 player.sendMessage("§cUnknown sub command. Type /darkauction for a list of sub commands.");
        	}else {
        		 player.sendMessage("§eDarkAuction all sub commands:");
        		 
        		 player.sendMessage("§e/darkauction additem - Add into the random list the item in your hand");
        		 player.sendMessage("§e/darkauction removeitem (number)");
        		 player.sendMessage("§e/darkauction giveitem (number)");
        		 player.sendMessage("§e/darkauction info - Get the list of all the items in the random list");
        		 player.sendMessage("§e/darkauction setlocation - Set the hologram location in your position");
        		 player.sendMessage("§e/darkauction reload - Reload config and messages of the plugin");
        		 player.sendMessage("§e/darkauction forcestart");
        		 player.sendMessage("§e/darkauction forcestop");
        		 player.sendMessage("§e/darkauction checkforupdate");
        		 player.sendMessage("§e/darkauction gui - [BETA (Made in 1.16)] Open a gui management for darkauction");
        	}
        	return true;
        }
        if (cmd.getName().equalsIgnoreCase("openauction")) {
        	
        	player.openInventory(inv);
        	return true;
        }
		return false;
	}
	private String getName(ItemStack stack) {
		
		if(stack.getType().name() == null) return "unknown";
		if(stack.getItemMeta().getDisplayName().length() > 0) return stack.getItemMeta().getDisplayName();
		
		return stack.getType().name();
	}
	public void startAuction() {
		if(allItems.size() < 2) {
			
			return;
		}
		AuctionStartEvent event = new AuctionStartEvent();
		 Bukkit.getPluginManager().callEvent(event);
		 if(event.isCancelled()) {
			 isStarted = false;
			 time = getConfig().getInt("startauction-in-seconds");
			 return;
		 }
		currentlyItem = allItems.get(new Random().nextInt(allItems.size()-1));
		time = getConfig().getInt("durationauction-in-seconds");;
		isStarted = true;
		notifyPlayers(getString("MESSAGES.auctionstartednew")+"");
		if(loc != null)itemSender.createItm(currentlyItem, loc);
		for(Player plr : getServer().getOnlinePlayers()) {
			if(loc != null)itemSender.sendItem(plr, currentlyItem, loc);
		}
		inv= Bukkit.createInventory(null, 9*6, "§0"+getName(currentlyItem)); //owner can be "null" or a player
		setupInv(inv);
		
	}
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player plr = event.getPlayer();
        if(loc != null && isStarted)itemSender.sendItem(plr, currentlyItem, loc);
        if(plr.hasPermission("darkauction.updatechecker")) {
        	 updater.startUpdateCheck(plr);
        }
       
        
	}
	public void notifyPlayers(String message) {
		for(Player plr : getServer().getOnlinePlayers()) {
			plr.sendMessage(getString("MESSAGES.darkauction-prefix")+"§f "+message);
		}
	}
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
            
        }
        if(EconomyHelper.isOlder(EconomyHelper.vaultversion)) {
        	 RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
             if (rsp == null) {
                 return false;
             }
             econ = rsp.getProvider();
             this.getLogger().severe(String.format("You are using an old version of Vault that support Minecraft 1.12.2"));
             this.getLogger().severe(String.format("Make sure to use new Vault version!"));
             return econ != null;
        }else {
        	
        	 RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
             if (rsp == null) {
                 return false;
             }
             econ = rsp.getProvider();
             
             return econ != null;
        }
       
        
       
    }
	public void offerChecker() {
		Bukkit.getScheduler().runTaskAsynchronously(DarkAuction.get(), () -> {
			for(int i=0; i < offers.size(); i++) {
				Offer offer = offers.get(i);
				Player plr = getServer().getPlayer(offer.playername);
				if(plr == null || EconomyHelper.getMoney(offer.playername) < offer.offer) {
					offers.remove(offer);
				}
			}
			    
        });
		
	}
	
	public void finishAuction() {
		try {
			for(HumanEntity entity: inv.getViewers()) {
				entity.closeInventory();
			}
		}catch(Exception e) {
			if(debug) {
				this.getLogger().severe("An error occured while closing inventory: "+e.getMessage());
			}
		}
		
		isStarted = false;
		time = getConfig().getInt("startauction-in-seconds");
		Offer offer = getHigherOffer();
		 AuctionEndEvent event = new AuctionEndEvent(offer);
		 Bukkit.getPluginManager().callEvent(event);
		 
		if(offer != null) {
			String msg = getString("MESSAGES.auctionend-winner").replace("{playername}", offer.playername);
			msg = msg.replace("{offer}", ""+offer.offer);
			notifyPlayers(msg);
		
			Player player = getServer().getPlayer(offer.playername);
			if(!event.isCancelled()) {
				player.getInventory().addItem(currentlyItem.clone());
				EconomyHelper.withdraw(offer.playername, offer.offer);
			}
			
		}else {
			notifyPlayers(getString("MESSAGES.auctionend-nowinner"));
		}
		offers.clear();
		
		for(Player plr : getServer().getOnlinePlayers()) {
			if(loc != null)itemSender.removeItem(plr, currentlyItem, loc);
		}
		
	}
	public void makeAction(Player plr,double prizenew) {
		if(isStarted == false) return;
		
		if(isStarted == false) return;
		if(isStarted == false) return;
		if(isStarted == false) return;
		
		if(isStarted == false) return;
		
		Offer offer = getOffer(plr);
		if(offer == null) {
			if(EconomyHelper.getMoney(plr.getName()) >= prizenew) {
				offer = new Offer(plr.getName(),prizenew);
				NewOfferEvent event = new NewOfferEvent(offer);
				 Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled()) {
				offers.add(offer);
				  if(time < 10) time = 10;
			}
			      
			}else {
				plr.sendMessage(getString("MESSAGES.no-money-for-offer"));
			}
		}else {
			Offer off = getHigherOffer();
			if(off.offer == offer.offer) {
				
			}else {
				if(EconomyHelper.getMoney(plr.getName()) >= prizenew) {
					ModifyOfferEvent event = new ModifyOfferEvent(offer);
					 Bukkit.getPluginManager().callEvent(event);
				if(!event.isCancelled()) {
					offer.addOffer(prizenew);
					 if(time < 10) time = 10;
				}
					
				
				}else {
					plr.sendMessage(getString("MESSAGES.no-money-for-raise-offer"));
				}
				
			}
			
		}
		
	}
	public Offer getOffer(Player plr) {
		
		for(Offer offer : offers) if(offer.playername.equals(plr.getName())) return offer;
		return null;
	}
	/*public void updateHologram() 
	{
		 int p1 = time % 60;
	        int p2 = time / 60;
	        int p3 = p2 % 60;
	        p2 = p2 / 60;
	        //System.out.print( p2 + ":" + p3 + ":" + p1);
	        if(loc == null) return;
		if(hologram == null)hologram = HologramsAPI.createHologram(this, loc.clone().add(new Location(loc.getWorld(),0,1.3,0)));
		hologram.teleport(loc.clone().add(new Location(loc.getWorld(),0,0.75,0)));
		VisibilityManager visibilityManager = hologram.getVisibilityManager();
        hologram.clearLines();
        if(isStarted) {
        	hologram.appendTextLine("§b"+getName(currentlyItem));
        	if(offers.size() < 1) {
        		hologram.appendTextLine(getString("MESSAGES.initialOffer-hologram-msg")+" §a"+initialCost+getString("MESSAGES.currencysymbol"));
        	}else {
        		Offer offer = getHigherOffer();
        		hologram.appendTextLine(getString("MESSAGES.currentOffer-hologram-msg")+" §a"+offer.offer+getString("MESSAGES.currencysymbol"));
        		hologram.appendTextLine("§6"+offer.playername);
        		
        	}
        	hologram.appendTextLine(getString("MESSAGES.dark-auction-endin")+" §e"+p2 + "h " + p3 + "m " + p1+"s");
        	hologram.appendTextLine("§e"+getString("MESSAGES.method-for-see-theitem-hologram-msg"));
        	
        }else {
        
        	hologram.appendTextLine(getString("MESSAGES.auctionstarts-in-hologram-msg")+" §e"+p2 + "h " + p3 + "m " + p1+"s");
        }
		
		for(Player plr : getServer().getOnlinePlayers()) 
	        {
		
			 visibilityManager.resetVisibility(plr);
	        	
	        		
		        	visibilityManager.showTo(plr);
	            
	        	
	        	
	        }
		visibilityManager.setVisibleByDefault(false);
		
	}*/
	public void updateHologram() 
	{
		NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
		 int p1 = time % 60;
	        int p2 = time / 60;
	        int p3 = p2 % 60;
	        p2 = p2 / 60;
	        //System.out.print( p2 + ":" + p3 + ":" + p1);
	        if(loc == null) return;
		
		hologramAPI.teleport(loc.clone().add(new Location(loc.getWorld(),0,0.75,0)));
		//VisibilityManager visibilityManager = hologram.getVisibilityManager();
		hologramAPI.clearLines();
        if(isStarted) {
        	hologramAPI.appendTextLine("§b"+getName(currentlyItem));
        	if(offers.size() < 1) {
        		hologramAPI.appendTextLine(getString("MESSAGES.initialOffer-hologram-msg")+" §a"+defaultFormat.format(initialCost)+getString("MESSAGES.currencysymbol"));
        	}else {
        		Offer offer = getHigherOffer();
        		hologramAPI.appendTextLine(getString("MESSAGES.currentOffer-hologram-msg")+" §a"+defaultFormat.format(offer.offer)+getString("MESSAGES.currencysymbol"));
        		hologramAPI.appendTextLine("§6"+offer.playername);
        		
        	}
        	hologramAPI.appendTextLine(getString("MESSAGES.dark-auction-endin")+" §e"+p2 + "h " + p3 + "m " + p1+"s");
        	hologramAPI.appendTextLine("§e"+getString("MESSAGES.method-for-see-theitem-hologram-msg"));
        	
        }else {
            if(time < 0) {
            	if(debug) {
            		hologramAPI.appendTextLine("§cAn error occured? DarkAuction seem to not start the auction...");
                	hologramAPI.appendTextLine("§cTry to see if the auction list is empty");
                	hologramAPI.appendTextLine("§cAnd make sure the list is not lower than 2/3 items");
            	}else {
            		hologramAPI.appendTextLine("§cAn error occured while starting...");
            	}
            	
            }else {
            	hologramAPI.appendTextLine(getString("MESSAGES.auctionstarts-in-hologram-msg")+" §e"+p2 + "h " + p3 + "m " + p1+"s");
            }
        	
        }
		
		/*for(Player plr : getServer().getOnlinePlayers()) 
	        {
		
			 visibilityManager.resetVisibility(plr);
	        	
	        		
		        	visibilityManager.showTo(plr);
	            
	        	
	        	
	        }
		visibilityManager.setVisibleByDefault(false);*/
		
	}
	public Offer getHigherOffer() {
		
		double actual = 0;
		Offer selected = null;
		for(Offer offer : offers) {
			if(offer.offer > actual) {
				actual = offer.offer;
				selected = offer;
					
			}
		}
		return selected;
	}
	public static void setupInv(Inventory inv) {
	
		Bukkit.getScheduler().runTaskAsynchronously(DarkAuction.get(), () -> {
			for(int i = 0; i < inv.getSize(); i++) {
				//new ItemStack(Material.BLACK_STAINED_GLASS_PANE,1)
				inv.setItem(i, GuiManager.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, "§7"));
			}
			
        });
		
		
		
	}
	

	
}
