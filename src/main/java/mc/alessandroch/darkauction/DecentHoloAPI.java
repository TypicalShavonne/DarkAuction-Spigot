package mc.alessandroch.darkauction;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;


import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;


public class DecentHoloAPI extends HologramAPI {
    private Hologram hologram;
    private final List<String> strings = new ArrayList<String>();
    Location loc = null;

    @Override
    public void start() {
        hologram = DecentHologramsAPI.get().getHologramManager().getHologram("darkauction-holo");

        if (hologram == null) {
            hologram = DHAPI.createHologram("darkauction-holo", new Location(Bukkit.getWorld("world"), 0, 1.3, 0));
            //hologram = HologramsAPI.createHologram((Plugin)DarkAuction.get(), );
            //hologram.addPage();
            DecentHologramsAPI.get().getHologramManager().registerHologram(hologram);
            hologram.enable();
        }
    }


    @Override
    public void teleport(Location add) {
        DecentHologramsAPI.get().getHologramManager().registerHologram(hologram);
        hologram.enable();
        loc = add.clone();

        DHAPI.moveHologram(hologram, add);
        DHAPI.setHologramLines(hologram, strings);


    }

    @Override
    public void clearLines() {
        strings.clear();
		


    }

    @Override
    public void appendTextLine(String string) {
        //DHAPI.addHologramLine(hologram, string);
        //hologram.showAll();
        strings.add(string);
        //hologram.appendTextLine(string);

    }
}
