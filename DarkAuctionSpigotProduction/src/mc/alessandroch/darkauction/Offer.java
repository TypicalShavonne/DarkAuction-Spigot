package mc.alessandroch.darkauction;

import java.text.NumberFormat;

public class Offer {

	public String playername;
	public double offer;
	
	public Offer(String plr, double offer) {
		this.playername = plr;
		this.offer = offer;
		
		String msg = DarkAuction.get().getString("MESSAGES.newbid");
		NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
		try {
			msg= msg.replace("{playername}", plr).replace("{offer}", ""+defaultFormat.format(offer));
		}catch(Exception e) {
			msg= msg.replace("{playername}", plr).replace("{offer}", ""+offer);
		}
	
		DarkAuction.get().notifyPlayers(msg);
	}
	
	public void addOffer(double offer) {
		this.offer=offer;
		//DarkAuction.get().notifyPlayers("§c"+playername+"§f ha aumentato l'offerta a §a€"+offer+"§f per l'oggetto all'asta!");
		String msg = DarkAuction.get().getString("MESSAGES.bidincreased");
		NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
		try {
			msg= msg.replace("{playername}", playername).replace("{offer}", ""+defaultFormat.format(offer));
		}catch(Exception e) {
			msg= msg.replace("{playername}", playername).replace("{offer}", ""+offer);
		}
		DarkAuction.get().notifyPlayers(msg);
	}
}
