package mc.alessandroch.darkauction.itemsender;

/*import net.minecraft.server.v1_12_R1.PacketDataSerializer;

import net.minecraft.server.v1_12_R1.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_12_R1.Vec3D;*/

/*public class PacketEntityVelocity_1_12_R1 extends PacketPlayOutEntityVelocity {

	
	int id;
	short x;
	short y;
	short z;
	
	public PacketEntityVelocity_1_12_R1(int itemid,Vec3D vec) {
		this.id = itemid;
		this.x = (short) vec.x;
		this.y = (short)vec.y;
		this.z = (short)vec.z;
		
	}
    public void b(PacketDataSerializer packetdataserializer) {
        packetdataserializer.writeInt(this.id);
        packetdataserializer.writeShort(this.x);
        packetdataserializer.writeShort(this.y);
        packetdataserializer.writeShort(this.z);
    }
    
    public void a(PacketDataSerializer packetdataserializer) {
        this.id = packetdataserializer.readInt();
        this.x = packetdataserializer.readShort();
        this.y = packetdataserializer.readShort();
        this.z = packetdataserializer.readShort();
    }

    



    public String b() {
        return String.format("id=%d, x=%.2f, y=%.2f, z=%.2f", new Object[] { Integer.valueOf(this.id), Float.valueOf((float) this.x / 8000.0F), Float.valueOf((float) this.y / 8000.0F), Float.valueOf((float) this.z / 8000.0F)});
    }

   
}*/
public class PacketEntityVelocity_1_12_R1{
	
}