package game.skill.o;

import java.util.HashSet;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import ci.particle.utils.OrdinaryColor;
import ci.particle.utils.ParticleEffect;

import game.utils.UtilOth;
import game.main.Main;
import game.utils.UtilItemByte;
import game.utils.UtilPacketItem;
import game.utils.UtilPlaySound;


public class Skills {

	public static void sendM(Player player) {
		player.sendMessage("a");
	}

	@SuppressWarnings("deprecation")
	public static void thunder(Player player) {
		Location location = player.getTargetBlock((HashSet<Byte>) null, 10).getLocation();
		Location ne = player.getLocation().add(0,0.2,0);
		createVecSt(ne, location, "9", 0.5, true, true, 0, 0, 0, 4, player);
		
		UtilPlaySound.playSpesificSound(location, 20, "ambient.weather.thunder", 2F, 1.9F);
		UtilPlaySound.playSpesificSound(location, 20, "fireworks.blast", 0F, 1.9F);
	}
	
	
	public static void createVecSt(Location location, Location tolocation, String itemcolor, double deviation, boolean attack, boolean ench,
			int rgb_a, int rgb_b, int rgb_c, int slot, Player player){

		float x = 0;
		float y = 0;
		float z = 0;
		for(double i = 0; i <= tolocation.distance(location); i+=0.2){		
	
			x =x + (float) -deviation + (float) (Math.random() * ((deviation - -0) + deviation));
			y =y + (float) -deviation + (float) (Math.random() * ((deviation - -0) + deviation));
			z =z + (float) -deviation + (float) (Math.random() * ((deviation - -0) + deviation));
						
			Vector unitVector = tolocation.toVector().subtract(location.toVector()).normalize();
	
			Location ab = location.clone().add(unitVector.multiply(i));
						
			ab.add(x,y,z);
			
			int randomBlock = (int)(Math.random() * 3 + 1);
			
			ItemStack block = null;
			if(itemcolor != "99"){
				if(randomBlock == 1){
					block = UtilItemByte.itemStack("35:"+itemcolor, 1);
				}
				if(randomBlock == 2){
					if(itemcolor == "0"){
						block = UtilItemByte.itemStack("155", 1);
					}else {
						block = UtilItemByte.itemStack("159:"+itemcolor, 1);
					}
					
				}
				if(randomBlock == 3){
					block = UtilItemByte.itemStack("95:"+itemcolor, 1);
				}
			}else {
				block = UtilItemByte.itemStack("95:"+itemcolor, 1);
			}
			
			if(itemcolor == "99") {
				block = UtilItemByte.itemStack("0:0", 1);
			}
			UtilPacketItem item = new UtilPacketItem(ab, block, slot, true);
			if(slot == 1) {
				Location fs = ab.clone();
				fs.setYaw(fs.getYaw() - 90);
				fs.setPitch(0);
				Vector fsv = fs.getDirection().multiply(-0.05);
				ab.add(fsv);
			}
			item.teleport(ab);
			if(rgb_a != 0) {
				ParticleEffect.REDSTONE.display(new OrdinaryColor(Color.fromRGB(rgb_a, rgb_b, rgb_c)), item.getLocation(), 30.0D);
			}
			removeItemPacket(item);
			if(randomBlock == 3){
				UtilOth.addLight(item.getLocation());
			}
			
			for(Entity e : UtilOth.getNearbyEntities(ab, 1.5F)){
				if(!attack) continue;
				if(e.equals(player)) continue; 
				((CraftLivingEntity) e).damage(15, player);
			}
			
			
			int random = (int)(Math.random() * 4 + 1);
			
			if(random == 3){
				
				int maxFar = 1;
				double deviationM = 0.1;
				
				float randomx = (float) -maxFar + (float) (Math.random() * ((maxFar - -maxFar) + maxFar));
				float randomy = (float) -maxFar + (float) (Math.random() * ((maxFar - -maxFar) + maxFar));
				float randomz = (float) -maxFar + (float) (Math.random() * ((maxFar - -maxFar) + maxFar));
				
				Location foc = item.getLocation().clone().add(randomx,randomy,randomz);
				float x2 = 0;
				float y2 = 0;
				float z2 = 0;
				for(double is = 0; is <= item.getLocation().distance(foc); is+=0.1){	
					
					x2 = x2 + (float) -deviationM + (float) (Math.random() * ((deviationM - -0) + deviationM));
					y2 =y2 + (float) -deviationM + (float) (Math.random() * ((deviationM - -0) + deviationM));
					z2 =z2 + (float) -deviationM + (float) (Math.random() * ((deviationM - -0) + deviationM));
					
					Vector unitVectors = item.getLocation().toVector().subtract(foc.toVector()).normalize();
					
					Location abc = item.getLocation().clone().add(unitVectors.multiply(is));
					abc.add(x2,y2,z2);
					UtilPacketItem itemf = new UtilPacketItem(abc, block, 0, true);
					itemf.teleport(abc);
					if(rgb_a != 0) {
						ParticleEffect.REDSTONE.display(new OrdinaryColor(Color.fromRGB(rgb_a,rgb_b, rgb_c)), item.getLocation(), 30.0D);
					}
		
					removeItemPacket(itemf);
					
					for(Entity e : UtilOth.getNearbyEntities(abc, 1F)){
						if(!attack) continue;
						if(e.equals(player)) continue; 
						((CraftLivingEntity) e).damage(15, player);
					}
				}
			}
		}
	}
	
	public static void removeItemPacket(UtilPacketItem item){
		new BukkitRunnable(){
			int t = 0;
			public void run(){
				t++;
				if(t>=20){
					this.cancel();
					item.remove();
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
}
