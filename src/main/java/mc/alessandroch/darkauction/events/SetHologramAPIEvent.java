package mc.alessandroch.darkauction.events;


import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import mc.alessandroch.darkauction.HologramAPI;


public class SetHologramAPIEvent extends Event {


    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private HologramAPI holoAPI;

    public SetHologramAPIEvent() {


    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public HologramAPI getAPI() {
        return holoAPI;
    }

    public void setAPI(HologramAPI api) {
        holoAPI = api;
    }
}


