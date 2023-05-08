package mc.alessandroch.darkauction;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;


import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

public class HoloDisplayAPI extends HologramAPI {

	
	private com.gmail.filoghost.holographicdisplays.api.Hologram hologram;

	@Override
	public void start() {
		
		hologram = HologramsAPI.createHologram((Plugin)DarkAuction.get(), new Location(Bukkit.getWorlds().get(0),0,1.3,0));
		
	}
	
	
	@Override
	public void teleport(Location add) {
		
		hologram.teleport(add);
	}
	
	@Override
	public void clearLines() {
		hologram.clearLines();
		
	}
	@Override
	public void appendTextLine(String string) {
	hologram.appendTextLine(string);
		
	}
}
