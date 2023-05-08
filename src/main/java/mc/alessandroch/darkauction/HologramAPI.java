package mc.alessandroch.darkauction;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public abstract class HologramAPI {

    static HologramAPI loadedAPI = null;

    public abstract void start();

    public static HologramAPI newAPI() {

        Plugin DecentHolograms = Bukkit.getServer().getPluginManager().getPlugin("DecentHolograms");
        if (DecentHolograms != null) {
            return new DecentHoloAPI();
        } else {
            if (loadedAPI != null) {
                return loadedAPI;
            }
            return null;
        }

    }

    public static void setAPI(HologramAPI api) {
        loadedAPI = api;
    }

    public abstract void teleport(Location add);

    public abstract void clearLines();

    public abstract void appendTextLine(String string);

}
