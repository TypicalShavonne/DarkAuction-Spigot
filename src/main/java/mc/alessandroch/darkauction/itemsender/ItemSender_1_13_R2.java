package mc.alessandroch.darkauction.itemsender;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_13_R2.EntityItem;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_13_R2.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_13_R2.Vec3D;

public class ItemSender_1_13_R2 implements ItemSender{

	EntityItem idd = null;
	@Override
	public void createItm(ItemStack item,Location loc) {
		
		 CraftWorld world = (CraftWorld)loc.getWorld();
	        
		 EntityItem_1_13_R2 entityItem = new EntityItem_1_13_R2(world.getHandle(),loc.getX(), loc.getY()+0.5, loc.getZ());
	        entityItem.setItemStack(CraftItemStack.asNMSCopy(item));
	        entityItem.setPosition(loc.getX(), loc.getY()+0.5, loc.getZ());
	       
	        entityItem.setOnGround(true); //Set on ground not know class for 1.15-
		idd = entityItem;
	}
	@Override
	public void sendItem(Player player, ItemStack item,Location loc)
    {
		CraftPlayer plr = (CraftPlayer) player;
        EntityPlayer entityPlayer = plr.getHandle();
       
        
        EntityItem entityItem = idd;
        PacketPlayOutSpawnEntity itemPacket = new PacketPlayOutSpawnEntity(entityItem, 2);
        
        entityPlayer.playerConnection.sendPacket(itemPacket);

        PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(entityItem.getId(), entityItem.getDataWatcher(), true);
        entityPlayer.playerConnection.sendPacket(meta);
       
       PacketEntityVelocity_1_13_R2 packet2 = new PacketEntityVelocity_1_13_R2(entityItem.getId(), new Vec3D(0, 0, 0) );
       
      // entityPlayer.playerConnection.sendPacket(packet2);
       
       // All slots are disabled to prevent client glitches
       
      
       
        
    }
	@Override
	public void removeItem(Player player, ItemStack currentlyItem, Location loc) {
		// TODO Auto-generated method stub
		CraftPlayer plr = (CraftPlayer) player;
        EntityPlayer entityPlayer = plr.getHandle();
       if(idd != null) entityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(idd.getId()));
	}

}
