package game.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import game.utils.CallbackInventory;
import game.utils.CallbackInventory.InventoryCallbackListener;
import game.utils.UtilOth;
import game.utils.UtilWorldS;

public class GManager {

	
	public static HashMap<String, HSGame> HASH_GAMES = new HashMap<String, HSGame>();
	
	public static int ARENA_INTEGER = 2;
	
	public static String ARENA_NAME = "Fear_";
	public static String GAME_NAME = "§d§lFear §7";
	
	public static ConsoleCommandSender console = Main.getInstance().getServer().getConsoleSender();
	
	public static void setGame(){
		
		GameOptions.setCharacterOpt();

		for(int i = 1; i<= ARENA_INTEGER; i++){
			String gameName = ARENA_NAME+i;
			
			HSGame game = new HSGame(gameName, i);
			console.sendMessage("§c#############################################################################");
			systemInfo("Arena olusturuldu: " + gameName);
			systemInfo("State            : " + game.gameState());
			console.sendMessage("§c#############################################################################");
			HASH_GAMES.put(game.getGameName(), game);	
		}
	
	}
	
	public static void editMainWorld(Player player) {
		UtilWorldS.editWorld(ARENA_NAME);
		gameInfo(player, "editleme modu §aaktif!");
		
		player.teleport(Bukkit.getWorld(ARENA_NAME).getSpawnLocation());
	}
	
	public static void dddd(Player player) {
		UtilWorldS.offEdW(ARENA_NAME);
		gameInfo(player, "editleme modu §cde-aktif!");
		
		player.teleport(Bukkit.getWorld("world").getSpawnLocation());
	}
	
	public static void disableOpt(){
		
	}
	
	public static void systemInfo(String info){
		console.sendMessage(GAME_NAME + info);
	}
	
	public static void gameInfo(Player player, String info) {
		player.sendMessage(GAME_NAME + info);
	}
	
	
	/*
	 * ARENA GETTERS-SETTERS
	 */
	
	
	public static HSGame getArena(String name){
		HSGame game = null;
		for(HSGame g : HASH_GAMES.values()){
			String[] obj = name.split("_");
			int no = Integer.parseInt(obj[1]);
			if(no != g.getArenaNumber()) continue;
			
			game = g;
		}
		if(game == null){
			systemInfo("§c§lRROR: §carena bulunamadi.");
		}
		return game;
	}
	
	public static HashMap<String, HSGame> arenaMaps(){
		return HASH_GAMES;
	}
	
	public static int getArenaNumber(HSGame arena) {
		String[] s = arena.getGameName().split("_");
		return Integer.parseInt(s[1]);
	}
	
	public static HSGame getArenaViaPlayer(Player player){
		HSGame game = null;
		for(HSGame g : HASH_GAMES.values()){
			if(!g.isInGame(player)) continue;
			game = g;
		}
		return game;
	}
	
	public static boolean hasAGame(Player player){
		boolean yes = false;
		HSGame game = null;
		for(HSGame g : HASH_GAMES.values()){
			if(!g.isInGame(player)) continue;
			game = g;
		}
		if(game != null){
			yes = true;
		}
		return yes;
	}
	
	public static void putGame(Player player, HSGame game){
		if(game == null){
			systemInfo("arena bulunamadigindan arena baglantisi gerceklestirilemedi.");
			return;
		}
		game.putInGame(player);
	}
	
	public static void removeFGame(Player player){
		HSGame game = null;
		for(HSGame g : HASH_GAMES.values()){
			if(g.isInGame(player)){
				game = g;
			}
		}
		
		game.removeFromGame(player);
	}
	
	
	/*
	 * INV
	 */
	
	public static void openArenaMenu(Player player){
		
		CallbackInventory inv = new CallbackInventory("§c§lOyun Listesi", 18,  new InventoryCallbackListener() {
			@Override
			public void onClick(InventoryClickEvent event) {
				ItemStack item = event.getCurrentItem();
				putGame(player, getArena(item.getItemMeta().getDisplayName()));
				player.closeInventory();
			}

			@Override
			public void onClose(InventoryCloseEvent event) {
				
			}
		});
		ArrayList<Integer> inte = new ArrayList<Integer>();
		for(String s : arenaMaps().keySet()) {
			int i = getArenaNumber(getArena(s));
			int position = 2*i + 1;
			inte.add(position);
			inv.setInv("130",s, position , true);
		}
		for(int i = 0; i <= 17; i++) {
			if(inte.contains(i)) continue;
			inv.setInv("160:8", "§a", i,  true);
		}

		inv.open(player);
	}

}
