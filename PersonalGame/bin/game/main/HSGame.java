package game.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import g.tab.GTab;
import game.character.Characters;
import game.character.CharactersOpt;
import game.character.CharactersPC;
import game.menus.GBoard;
import game.utils.UtilItemByte;
import game.utils.UtilOth;
import game.utils.UtilWorldS;


public class HSGame {


	private String GAME_NAME;
	private int ARENA_NUMBER;
	private World  world;

	private GameState state;
	
	private int MIN_PLAYER_SIZE = 1;
	
	private HashMap<Player, String> ROLE_P;
	private HashMap<Player, GBoard> P_B;
	private HashMap<Player, GTab> Tab_B;
	
	private ArrayList<Location> signsL;
	private Location killerSpawn;
	
	private ArrayList<Player> PLAYERS;
	private ArrayList<Player> SPECTS;
	private ArrayList<Player> KILLER;
	
	private ArrayList<Player> KILLED;
	
	private CharactersPC pc;
	
	private String gameTime = "";
	
	//private Scoreboard score = Bukkit.getScoreboardManager().getMainScoreboard();
//	private Team t = score.registerNewTeam("nhide");

	public HSGame(String game_name,  int arena_number) {
		this.GAME_NAME    = game_name;
		this.ARENA_NUMBER = arena_number;
		this.state        = GameState.LOBBY;
		
		//this.t.setNameTagVisibility(NameTagVisibility.NEVER);
		
		this.PLAYERS  = new ArrayList<Player>();
		this.KILLER   = new ArrayList<Player>();
		
		this.signsL   = new ArrayList<Location>();
		
		this.ROLE_P   = new HashMap<Player, String>(); 
		this.P_B      = new HashMap<Player, GBoard>();
		this.Tab_B    = new HashMap<Player, GTab>();
		
		UtilWorldS.loadWorld(GManager.ARENA_NAME, GManager.ARENA_NAME+this.ARENA_NUMBER);
		
		this.world = Bukkit.getWorld(GManager.ARENA_NAME+this.ARENA_NUMBER);
		
		setSpawnLocations();
		setGameTask2();
	}
	
	public boolean isHiderOrKiller(Player player) {
		return (this.KILLER.contains(player)? true : false);
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public String getGameName() {
		return this.GAME_NAME;
	}
	
	public GameState gameState(){
		return this.state;
	}
	
	public String getTime() {
		return this.gameTime;
	}
	
	public ArrayList<Player> getPlayers(){
		return this.PLAYERS;
	}
	
	public CharactersPC getPc() {
		return this.pc;
	}
	
	public void setState(GameState gs) {
		this.state = gs;
		
		if(this.PLAYERS.isEmpty()) return;
		for(Player player : this.PLAYERS) {
			if(player == null) continue;
			this.P_B.get(player).updateState();
		}
		removeAllPlayersItem();
	}
	
	public void resetArena() {
		setState(GameState.LOBBY);
		
		this.ROLE_P.clear();
		this.P_B.clear();
		this.Tab_B.clear();
		
		UtilWorldS.resetWorld(GManager.GAME_NAME + this.ARENA_NUMBER);
		
		setSpawnLocations();
	}
	
	public void setSpawnLocations() {
		for(Sign s : UtilOth.getSigns(this.world, "spawn")) {
			this.signsL.add(s.getLocation().add(0.5,0.5,0.5));
			//s.getBlock().setType(Material.AIR);
		}
		this.killerSpawn = UtilOth.getSpesSign(this.world, "kspawn").add(0.5,0.5,0.5);
	}
	
	public void teleportSpawns() {
		ArrayList<Location> si = this.signsL;
		for(Player p : this.PLAYERS) {
			if(this.KILLER.contains(p)) {
				p.teleport(this.killerSpawn);
				sendEffective(p);
				continue;
			}
			
			int i = new Random().nextInt(si.size());
			
			p.teleport(si.get(i));
			
			si.remove(si.get(i));
		}
	}
	
	public int getArenaNumber(){
		return this.ARENA_NUMBER;
	}
	
	public GBoard getBoardP(Player player) {
		return this.P_B.get(player);
	}
	
	public void removeBoardP(Player player) {
		this.P_B.get(player).delete();
		this.P_B.remove(player);
	}
	
	public boolean isInGame(Player player){
		boolean yes = false;
		if(PLAYERS.contains(player)){
			yes = true;
		}else{
			yes = false;
		}
		return yes;
	}
	
	public boolean isKiller(Player player) {
		return (this.KILLER.contains(player)? true : false);
	}
	
	public void updateBoard() {
		for(Player player : this.PLAYERS) {
			this.getBoardP(player).updateState();
		}	
	}
	
	public void putInGame(Player player){
		PLAYERS.add(player);
		sendInfo(player, "oyuna giriş yaptın.");
		broadCast("§b"+player.getName()+" §aoyuna giriş yaptı.");
		
		GBoard b = new GBoard(player);
		this.P_B.put(player, b);
		
		GTab gt = new GTab(player);
		
		this.Tab_B.put(player, gt);
		
		addItems(player);
		
		CharactersOpt.PLAYER_CHARACTER_MAP.put(player.getName(), Characters.MECHMEN);
	}
	
	public void removeFromGame(Player player){
		PLAYERS.remove(player);
		
		sendInfo(player, "oyundan ayrıldın.");
		broadCast("§e"+player.getName()+" §coyundan ayrıldı.");
		
		removeBoardP(player);
		
		removeItems(player);
		
		this.Tab_B.get(player).delete();
		
		this.P_B  .remove(player);
		this.Tab_B.remove(player);
	}
	
	public void setKilled(Player player){
		this.KILLED.add(player);
		this.SPECTS.add(player);
		
		broadCast("§e" + player.getName() + " §cöldürüldü.");
		
		for(Player p : PLAYERS){
			p.hidePlayer(player);
		}
	}
	
	public void sendInfo(Player player, String message){
		player.sendMessage(""+GManager.GAME_NAME+" §7: "+ message);
	}
	
	public void broadCast(String broadcast){
		for(Player player : PLAYERS){
			if(player == null) continue;
			sendInfo(player, broadcast);
		}
	}

	public void sendChat(Player player, String message) {
		if(this.KILLED.contains(player)) {
			for(Player p : this.KILLED) {
				p.sendMessage("§8"+player.getName()+": §7" + message);
			}
		}else {
			for(Player p : this.PLAYERS) {
				p.sendMessage("§1"+player.getName()+": §9" + message);
			}
		}
		
	}
	
	
	public String timeS(int t){
		String timeString = null;
		
		if(t>=60){
			timeString = "" + t/60 + " dakika";
		}else {
			timeString = "" + t + " saniye";
		}
		return timeString;
	}
	
	public static String detailedTime(int t) {
		String timeString = null;
		int a = t/60;
		int d = t-(60*a);
		String m = (a >= 10)? ""+a : "0"+a;
		String s = (d >= 10)? ""+d : "0"+d;

		timeString = "" + m +":"+ s ;
	

		return timeString;
	}
	
	private boolean setted = false;
	
	public void sendEffective(Player player) {
		ArrayList<String> ss = new ArrayList<String>();
		ss.add("§4§lYOK ET!");
		ss.add("§4Y̴̧͉̯͔̑͗̉͂͒̀͒O̵̯̻̯̜̥̦̣͍̱̺̎̑̀̀̉̐͘͝K̶̜͚̹̀̃͌͊͗ ̴̮͓̰̗͙̙̫͇̠̒̒̂̔̐͘ͅË̵̛̥̬̱̇̾̑͌T̴̞̺͑̅̿̔̈́̈͋͌͊̀");
		ss.add("§c§lTƎ ⋊OY");
		ss.add("§k§4§lYOK ET!");
		ss.add("§4§lY̴̧͉̯͔̑͗̉͂͒̀͒O̵̯̻̯̜̥̦̣͍̱̺̎̑̀̀̉̐͘͝K̶̜͚̹̀̃͌͊͗ ̴̮͓̰̗͙̙̫͇̠̒̒̂̔̐͘ͅË̵̛̥̬̱̇̾̑͌T̴̞̺͑̅̿̔̈́̈͋͌͊̀");
		ss.add(" ");
		ss.add(" ");
		ss.add(" : ) ");
		
		this.pc = new CharactersPC(player, 100);
		
		new BukkitRunnable() {
			int t = 0;
			int a = 0;
			@SuppressWarnings("deprecation")
			public void run() {
				a++;
				t++;
				if(a >= 2) {
					a = 0;
					player.sendTitle(ss.get(new Random().nextInt(ss.size())), ss.get(new Random().nextInt(ss.size())));
				}
				
				if(t >= 20*5) {
					this.cancel();
				}
			}
			
			
		}.runTaskTimer(Main.getInstance(), 0, 1);
		
	}
	
	public void setStartingOptions() {
		if(setted) return;

		setState(GameState.IN_GAME);
		
		Player player = this.PLAYERS.get(new Random().nextInt(this.PLAYERS.size()));
		this.KILLER.add(player);
		
		setted = true;
		teleportSpawns();
	}
	
	
	/*
	 * INV
	 */
	
	public void addItems(Player player) {
		ItemStack egg = UtilItemByte.itemStack("383:58", 1);
		UtilItemByte.setName(egg, "§dKarakterini Seç");
		
		ItemStack door = UtilItemByte.itemStack("324", 1);
		UtilItemByte.setName(door, "§cOyundan Çık");
		//
		player.getInventory().setItem(1, egg);
		player.getInventory().setItem(8, door);
	}
	
	public void removeItems(Player player) {
		for(int i = 0; i <= 8; i++) {
			player.getInventory().setItem(i, new ItemStack(Material.AIR));
		}
	}
	
	public void removeAllPlayersItem() {
		for(Player p : this.PLAYERS) {
			removeItems(p);
		}
	}
	
	
	/*
	 * TASK
	 */
	
	public void setGameTask2() {
		
		new BukkitRunnable() {
			int t = 10;
			
			boolean timer = false;
			boolean warng = false;
			
			public void run() {
			
				/*
				 * LOBİ
				 */
				
				if(gameState() == GameState.LOBBY) {
					if(timer) {
						t--;
						if(PLAYERS.size() < MIN_PLAYER_SIZE) {
							warng = true;
							broadCast("yeterli sayıda oyuncu olmadığından sayım durduruldu.");
							timer = false;
						}
						if(t <= 5){
							if(t <= 0){
								timer = false;
								broadCast("§c§lOYUN BAŞLIYOR!");
								setState(GameState.STARTING);
								return;
							}
							broadCast("oyunun başlamasına §5" + t + " §7saniye...");
						}
					}
					
					if(PLAYERS.size() < MIN_PLAYER_SIZE) {
						if(warng) {
							t = 10;
							broadCast("yeterli sayıda oyuncu olmadığından oyun başlatılamadı.");
							warng = false;
							timer = false;
						}
						
					}else {
						if(!timer) {
							timer = true;
						}
					}
				}
				
				/*
				 * BAŞLAMAKTA
				 */
				
				if(gameState() == GameState.STARTING) {
					//game start options
					setStartingOptions();
				}
				
				/*
				 * OYNANMAKTA
				 */
				
				if(gameState() == GameState.IN_GAME) {
					if(!timer) {
						t = 60*5;
						timer = true;
					}
					t--;
					gameTime = detailedTime(t);
					updateBoard();
					if(t == 60*5/2 || t == 60 || t <= 10) {
						broadCast("oyunun bitmesine " + timeS(t) + " var.");
					}
					
					if(t <= 0) {
						broadCast("§cOYUN BİTTİ!");
						for(Player p : PLAYERS) {
						//	p.sendTitle(GAME_NAME, GAME_NAME); kazanan?
						}
						
						resetArena();//oyunda bulunan oyuncuları unutma
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}
	

}
