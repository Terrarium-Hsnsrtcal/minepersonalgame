package game.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import game.character.CharactersOpt;
import game.main.GManager;
import game.main.GameState;
import game.main.HSGame;
import game.skill.o.MatchSkill;

public class GListener implements Listener{

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		
		if(!GManager.hasAGame(player)) return;
		HSGame game = GManager.getArenaViaPlayer(player);

		if(game.gameState().equals(GameState.IN_GAME)) {
			if(player.getInventory().getHeldItemSlot() == 4) {
				if(game.isKiller(player)) {
					MatchSkill.setInvoke(game.getPc().getSkillP(), player);
				}
			}
		}
		
		if(item.getType().equals(Material.AIR)) return;
		
		if(item.getItemMeta().getDisplayName().equals("§cOyundan Çýk")) {
			game.removeFromGame(player);
		}
		if(item.getItemMeta().getDisplayName().equals("§dKarakterini Seç")) {
			CharactersOpt.openChooseMenu(player);
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onKill(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Player killer = player.getKiller();
		
		if(!GManager.hasAGame(player) || !GManager.hasAGame(killer)) return;
		HSGame game = GManager.getArenaViaPlayer(player);
		
		if(game.gameState().equals(GameState.IN_GAME)) {
			game.setKilled(player);
		}
	}
	
	@EventHandler
	public void AsyncChatEvent(AsyncPlayerChatEvent e){
		Player player = e.getPlayer();
		String message = e.getMessage();

		if(!GManager.hasAGame(player)) return;
		HSGame game = GManager.getArenaViaPlayer(player);
		
		game.sendChat(player, message);
	}
	
}
