package ci.sp.util;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import game.utils.UtilBlockStand;


public class UBSMan {

	public static ArrayList<UtilBlockStand> ALL_UBS = new ArrayList<UtilBlockStand>();
	
	public static ArrayList<UtilBlockStand> getAllUBS() {
		return ALL_UBS;
	}
	
	public static void addUBSl(UtilBlockStand ubs) {
		ALL_UBS.add(ubs);
	}
	
	public static void clearAll() {
		for(UtilBlockStand ubs : ALL_UBS) {
			ubs.remove();
		}
		
	}
	
	public static void removeUBS(UtilBlockStand ubs) {
		ALL_UBS.remove(ubs);
	}
	
	public static void reSendPackets(Player player){
		for(UtilBlockStand ubs : ALL_UBS){
			if(ubs.getWorld() != player.getWorld()){
				ubs.hidePackets(player);
				continue;
			}
			ubs.updateItems(player);
		}
	}
}
