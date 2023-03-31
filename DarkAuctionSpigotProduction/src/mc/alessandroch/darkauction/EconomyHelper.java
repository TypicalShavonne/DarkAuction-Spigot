package mc.alessandroch.darkauction;

import org.bukkit.entity.Player;

public class EconomyHelper {

	public static String vaultversion = "";
	public static double getMoney(String name) {
	
		if(DarkAuction.get().getItemEconomy()) {
			
			
			return ItemEco.getMoney(name);
		}else {
			if(isOlder(vaultversion)) {
			return DarkAuction.get().econ.getBalance(name);
			}else {
				return DarkAuction.get().econ.getBalance(name);
			}
		}
		
	}

	public static void withdraw(String name, double i) {
	
        if(DarkAuction.get().getItemEconomy()) {
        	ItemEco.withdraw(name,i);
		}else {
			if(isOlder(vaultversion)) {
		DarkAuction.get().econ.withdrawPlayer(name, i);
			}else {
				DarkAuction.get().econ.withdrawPlayer(name, i);
			}
		}
	}

	public static void setMoney(String name, double i) {
		
        if(DarkAuction.get().getItemEconomy()) {
        	//Set money is not available for item economy
		}else {
			
			withdraw(name,getMoney(name));
			if(isOlder(vaultversion)) {
				DarkAuction.get().econ.depositPlayer(name, i);
			}else {
				DarkAuction.get().econ.depositPlayer(name, i);
			}
			
		}
		
	}

	public static boolean isOlder(String vault) {

		if(vault.equals("1.6.7")
				||
				vault.equals("1.6.6")
				||
				vault.equals("1.5.5")
				) {
			return true;
		}
		return false;
	}

	public static void addMoney(String name, double money) {
		
        if(DarkAuction.get().getItemEconomy()) {
        	ItemEco.addMoney(name,money);
		}else {
			if(isOlder(vaultversion)) {
		DarkAuction.get().econ.depositPlayer(name, money);
			}else {
				DarkAuction.get().econ.depositPlayer(name, money);
			}
		}
	}

}
