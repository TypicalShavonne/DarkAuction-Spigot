package mc.alessandroch.darkauction;

public class VaultEco {
	public static double getMoney(String name) {
		
		return DarkAuction.get().econ.getBalance(name);
	}

	public static void withdraw(String name, double i) {
	
		DarkAuction.get().econ.withdrawPlayer(name, i);
	}

	public static void setMoney(String name, double i) {
		
		withdraw(name,getMoney(name));
		DarkAuction.get().econ.depositPlayer(name, i);
	}

	public static void addMoney(String name, double money) {
	
		DarkAuction.get().econ.depositPlayer(name, money);
	}
}
