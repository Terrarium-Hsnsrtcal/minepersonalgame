package ci.sp.util;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class UBSListener implements Listener{

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UBSMan.reSendPackets(player);
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		UBSMan.reSendPackets(player);
	}
	
	@EventHandler
	public void onSPawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		UBSMan.reSendPackets(player);
	}
	
}
