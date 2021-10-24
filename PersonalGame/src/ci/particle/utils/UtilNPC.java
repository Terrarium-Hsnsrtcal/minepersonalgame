package ci.particle.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;

public class UtilNPC {

	private World world;
	private Location location;
	
	private ArrayList<Player> sended_Players = new ArrayList<Player>();
	private EntityPlayer entityPlayer;
	
	private String name;
	
	private float radius;
	
	
	public UtilNPC(String name, Location location, float radius){
		this.name     = name;
		this.location = location;
		this.radius   = radius;
		this.world    = location.getWorld();
		
		setNewNPC();
	}
	
	public void setNewNPC(){
		MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();;
		WorldServer nmsWorld = (((CraftWorld) world).getHandle());
		
		String prop[];
		
		prop = texture();
		
		GameProfile gp = new GameProfile(UUID.randomUUID(), name);
		gp.getProperties().put("textures", new Property("textures",prop[0],prop[1]));
		
		EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld,
				gp,
				new PlayerInteractManager(nmsWorld));
		
		npc.setLocation(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
				this.location.getPitch());
		
		byte flags = (byte) 0xFF;
		npc.getDataWatcher().watch(10,  flags) ;
		
		
		this.entityPlayer = npc;
	
	}
	
	public Location getLocation(){
		return this.location;
	}
	
	public void spawn(){
		for(Player player : Bukkit.getOnlinePlayers()){
			if(player.getLocation().distance(this.location) > this.radius) continue;
			
			this.sended_Players.add(player);
			
			PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
			connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, this.entityPlayer));
			connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this.entityPlayer));
		}
	}
	
	public void deSpawn(){
		for(Player player : this.sended_Players){
			PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
			connection.sendPacket(new PacketPlayOutEntityDestroy(this.entityPlayer.getId()));
			connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
					this.entityPlayer));
		}
	}

	  public String[] texture(){
	        try {
	        URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + this.name);
	        InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
	     
	        String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();
	        URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
	        InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
	     
	        JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
	        String texture = textureProperty.get("value").getAsString();
	        String signature = textureProperty.get("signature").getAsString();
	        return new String[]{texture,signature};
	        }catch(IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	  }
	
}
