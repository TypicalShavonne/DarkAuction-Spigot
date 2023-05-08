package mc.alessandroch.darkauction;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemEco {
    public static int getMoney(String name) {

        int items = 0;
        Material mat = DarkAuction.get().getMaterialEco();
        Player plr = Bukkit.getServer().getPlayer(name);
        Inventory inv = plr.getInventory();
        for (int i2 = 0; i2 < inv.getSize(); i2++) {
            ItemStack stack = inv.getItem(i2);
            if (!equalsSystem(plr, stack, mat)) continue;
            items += stack.getAmount();
        }
        return items;
    }

    public static void withdraw(String name, double i) {

        int toremove = (int) i;
        Material mat = DarkAuction.get().getMaterialEco();
        Player plr = Bukkit.getServer().getPlayer(name);
        Inventory inv = plr.getInventory();
        for (int i2 = 0; i2 < inv.getSize(); i2++) {
            ItemStack stack = inv.getItem(i2);
            if (!equalsSystem(plr, stack, mat)) continue;
            if (stack.getAmount() >= toremove) {
                stack.setAmount(stack.getAmount() - toremove);
                toremove = 0;
            } else {
                toremove -= stack.getAmount();
                stack.setAmount(0);
            }
        }
    }


    private static boolean equalsSystem(Player player, ItemStack stack, Material mat) {

        //	Inventory inv = player.getInventory();

        if (stack != null) {
            return stack.getType().equals(mat);

        }
        return false;
    }

    public static void addMoney(String name, double money) {

        Material mat = DarkAuction.get().getMaterialEco();
        if (money > 64) return;
        ItemStack stack = new ItemStack(mat, (int) money);
        Player plr = Bukkit.getServer().getPlayer(name);
        plr.getInventory().addItem(stack);

    }
}
