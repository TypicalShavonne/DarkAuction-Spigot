package mc.alessandroch.darkauction.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import mc.alessandroch.darkauction.DarkAuction;

 
public class UpdateChecker {
DarkAuction plugin;
public UpdateChecker(DarkAuction plugin) {
this.plugin = plugin;
currentVersion = plugin.version;
}
 
private String currentVersion;
private String readurl = "https://raw.githubusercontent.com/AlessandroCH2/DarkAuction-plugin/main/version.txt";
private String readurl2 = "https://raw.githubusercontent.com/AlessandroCH2/DarkAuction-plugin/main/supportedminecraftversions.txt";

public void startUpdateCheck(Player plr2) {

	new BukkitRunnable()
	{
	    @Override
	    public void run()
	    {
	    	List<String> supportedversions = new ArrayList<String>();
	    	Logger log = plugin.getLogger();
	    	try {
	    	log.info("Checking for a new version...");
	    	URL url = new URL(readurl);
	    	URL url2 = new URL(readurl2);
	    	BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
	    	BufferedReader br2 = new BufferedReader(new InputStreamReader(url2.openStream()));
	    	String str;
	    	while ((str = br2.readLine()) != null) {
	    		String vr = str;
	    		supportedversions.add(vr);
	    	}
	    	boolean isSupported = false;
	    	isSupported=checkSupported(supportedversions);
	    	while ((str = br.readLine()) != null) {
	    	String line = str;
	    	if(!line.equals(currentVersion)) {
	    		if(isSupported) {
	    		log.info("DarkAuction "+line+" has been released! Get it on https://www.spigotmc.org/resources/96643");
	    		}else {
	    			log.warning("DarkAuction "+line+" has been released! But not support this minecraft version anymore");
	    			
	    		}
	    		if(plr2 == null) {
	    		for(Player plr :plugin.getServer().getOnlinePlayers()) {
	    			if(plr.hasPermission("darkauction.updatechecker")) {
	    				
	    				if(isSupported) {
	    					plr.sendMessage("DarkAuction §e"+line+" §fhas been released! Get it on §ehttps://www.spigotmc.org/resources/96643");
	    		    		}else {
	    		    			plr.sendMessage("DarkAuction §e"+line+"§f has been released! §cBut not support this minecraft version anymore");
	    		    			
	    		    		}
	    			}
	    			
	    		}
	    		}else {
	    	        if(plr2.hasPermission("darkauction.updatechecker")) {
	    	        	if(isSupported) {
	    					plr2.sendMessage("DarkAuction §e"+line+" §fhas been released! Get it on §ehttps://www.spigotmc.org/resources/96643");
	    		    		}else {
	    		    			plr2.sendMessage("DarkAuction §e"+line+"§f has been released! §cBut not support this minecraft version anymore");
	    		    			
	    		    		}
	    			}
	    		}
	    	}else {
	    		log.info("Your version of DarkAuction is currently up to date");
	    		if(plr2 == null) {
	    			
	    			for(Player plr :plugin.getServer().getOnlinePlayers()) {
	    				if(plr.hasPermission("darkauction.updatechecker")) {
	    					plr.sendMessage("§aYour version of DarkAuction is currently up to date");
	    				}
	    			}
	    			}else {
	    		        if(plr2.hasPermission("darkauction.updatechecker")) {
	    		        	plr2.sendMessage("§aYour version of DarkAuction is currently up to date");
	    				}
	    			}
	    	}
	    	}

	    	br.close();
	    	} catch (IOException e) {
	    	log.severe("The UpdateChecker URL is invalid! Please let me know!");
	    	}
	    	
	    }
	}.runTaskAsynchronously(DarkAuction.get());

	
}

protected boolean checkSupported(List<String> supportedversions) {
	// TODO Auto-generated method stub
	 String version;
    
        try {

            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
            return false;
        }
        if(supportedversions.contains(version)) return true;
	return false;
}


public boolean updateCheckNewAvailable() {

	
	    	List<String> supportedversions = new ArrayList<String>();
	    	Logger log = plugin.getLogger();
	    	try {
	    	log.info("Checking for a new version...");
	    	URL url = new URL(readurl);
	    	URL url2 = new URL(readurl2);
	    	BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
	    	BufferedReader br2 = new BufferedReader(new InputStreamReader(url2.openStream()));
	    	String str;
	    	while ((str = br2.readLine()) != null) {
	    		String vr = str;
	    		supportedversions.add(vr);
	    	}
	    	
	    	
	    	while ((str = br.readLine()) != null) {
	    	String line = str;
	    	if(!line.equals(currentVersion)) {
	    		return true;
	    		
	    	}else {
	    		return false;
	    		
	    	}
	    	}

	    	br.close();
	    	} catch (IOException e) {
	    	log.severe("The UpdateChecker URL is invalid! Please let me know!");
	    	}
	    	
	   

	return false;
}
}
