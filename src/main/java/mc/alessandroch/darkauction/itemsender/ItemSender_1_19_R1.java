package mc.alessandroch.darkauction.itemsender;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;








public class ItemSender_1_19_R1 implements ItemSender{

	//EntityItem idd = null;
		Item item = null;

		@Override
		public void createItm(ItemStack item,Location loc) {
			
			
	      
		}
		@Override
		public void sendItem(Player player, ItemStack item2,Location loc)
	    {
			/*CraftPlayer plr = (CraftPlayer) player;
	        EntityPlayer entityPlayer = plr.getHandle();
	       
	        
	        EntityItem entityItem = idd;
	        PacketPlayOutSpawnEntity itemPacket = new PacketPlayOutSpawnEntity(entityItem, 2);
	        
	        entityPlayer.b.sendPacket(itemPacket);

	        PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(entityItem.getId(), entityItem.getDataWatcher(), true);
	        entityPlayer.b.sendPacket(meta);
	       
	       PacketPlayOutEntityVelocity packet2 = new PacketPlayOutEntityVelocity(entityItem.getId(), new Vec3D(0, 0, 0) );
	       entityPlayer.b.sendPacket(packet2);*/
			if(item == null) {
				 item = player.getWorld().dropItem(loc, item2);
                    item.setVelocity(new Vector(0,0,0));
			        item.setPickupDelay(Integer.MAX_VALUE);
			        item.setCustomName("DA-Item");
			        item.setCustomNameVisible(false);
			}
			 
	       
	       // All slots are disabled to prevent client glitches
	       
	      
	       
	        
	    }
		@Override
		public void removeItem(Player player, ItemStack currentlyItem, Location loc) {
			// TODO Auto-generated method stub
			/*CraftPlayer plr = (CraftPlayer) player;
	        EntityPlayer entityPlayer = plr.getHandle();
	       if(idd != null) entityPlayer.b.sendPacket(new PacketPlayOutEntityDestroy(idd.getId()));*/
			if(item != null) {
				item.remove();
				item = null;
				
			}
		}
}
