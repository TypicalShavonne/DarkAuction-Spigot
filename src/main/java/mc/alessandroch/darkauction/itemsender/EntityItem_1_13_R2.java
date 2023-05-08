package mc.alessandroch.darkauction.itemsender;

import net.minecraft.server.v1_13_R2.EntityItem;
import net.minecraft.server.v1_13_R2.World;

public class EntityItem_1_13_R2 extends EntityItem {

	public EntityItem_1_13_R2(World world, double d0, double d1, double d2) {
		super(world, d0, d1, d2);
		
	        this.setPosition(d0, d1, d2);
	        this.yaw = (float) (Math.random() * 360.0D);
	       
	}

	public void setOnGround(boolean flag) {
		this.onGround = flag;
		
	}
	
	
}
