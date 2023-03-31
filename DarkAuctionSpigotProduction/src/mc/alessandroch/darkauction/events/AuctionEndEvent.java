package mc.alessandroch.darkauction.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import mc.alessandroch.darkauction.Offer;

public class AuctionEndEvent extends Event implements Cancellable {
    private Player playerWon;
    private Offer playerOffer;
    
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    public AuctionEndEvent(Offer offer){
        this.playerOffer = offer;
        if(offer != null)
        this.playerWon = Bukkit.getServer().getPlayer(offer.playername);
        
        this.isCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public Player getPlayerWon() {
        return playerWon;
    }

    public Offer getOffer() {
    	return playerOffer;
    }
}


