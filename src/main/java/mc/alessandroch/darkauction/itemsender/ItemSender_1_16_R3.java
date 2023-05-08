package mc.alessandroch.darkauction.itemsender;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.minecraft.server.v1_16_R3.EntityItem;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_16_R3.Vec3D;


public class ItemSender_1_16_R3 implements ItemSender{

	EntityItem idd = null;
	@Override
	public void createItm(ItemStack item,Location loc) {
		
		 CraftWorld world = (CraftWorld)loc.getWorld();
	        
	        EntityItem entityItem = new EntityItem(world.getHandle(),loc.getX(), loc.getY()+0.5, loc.getZ());
	        entityItem.setItemStack(CraftItemStack.asNMSCopy(item));
	        entityItem.setPosition(loc.getX(), loc.getY()+0.5, loc.getZ());
	       
	        entityItem.setOnGround(true);
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
       
       PacketPlayOutEntityVelocity packet2 = new PacketPlayOutEntityVelocity(entityItem.getId(), new Vec3D(0, 0, 0) );
       entityPlayer.playerConnection.sendPacket(packet2);
       
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
