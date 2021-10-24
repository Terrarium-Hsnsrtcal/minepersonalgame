package game.character;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import game.main.GManager;
import game.main.GameState;
import game.main.Main;
import game.utils.ActionBar;
import game.utils.UtilItemByte;
import game.utils.UtilOth;

public class CharactersPC {

	private Player player;
	
	private String skill = "";
	
	private float mana;
	
	private float settedMaxMana;
	
	private float rejMaC = 0.1F;
	
	public CharactersPC(Player player, int settedMaxMana) {
		this.player = player;
		
		this.mana          = settedMaxMana;
		this.settedMaxMana = settedMaxMana;
		
		UtilOth.changeSkin(player, CharactersOpt.CHARACTER_SKIN.get(CharactersOpt.PLAYER_CHARACTER_MAP.get(player.getName())),
				GManager.getArenaViaPlayer(player).getPlayers());
		
		skillTaskKiller();
	}
	
	public void setMaxMana(int i) {
		this.settedMaxMana = i;
	}
	
	@SuppressWarnings("deprecation")
	public boolean decreaseMana(int i) {
		boolean y = false;
		if(i>this.mana) {
			this.player.sendTitle("", "§ayeterli manan yok!");
		}else {
			y = true;
			this.mana = this.mana - i;
		}
		
		return y;
	}
	
	public void increaseMana(int i) {
		if(i+this.mana >= this.settedMaxMana) {
			this.mana = this.settedMaxMana;
			return;
		}
		this.mana = this.mana + i;
	}
	
	public float getMana() {
		return this.mana;
	}
	
	public String getSkillP(){
		return this.skill;
	}
	
	public void skillTaskKiller() {
		for(int i = 0; i <= 8; i++) {
			if(CharactersOpt.getSkillName(this.player, i) == null) {
				this.player.getInventory().setItem(i, standartItem("§c§lX" ,"14"));
			}
			this.player.getInventory().setItem(i, standartItem("§e§l" + i + ". §eSkill", "8"));
		}	
		this.player.getInventory().setItem(4, new ItemStack(Material.AIR));

		new BukkitRunnable() {
			String actionB = "§a§lSkill: §r";
			public void run() {
				
				if(mana > settedMaxMana) {
					mana = settedMaxMana;
				}
				
				if(mana < settedMaxMana) {
					mana += rejMaC;
				}
				
				player.setLevel((int) mana);
				
				player.setExp(mana/100);
				
				if(GManager.getArenaViaPlayer(player) == null || !player.isOnline() || !GManager.getArenaViaPlayer(player).gameState().equals(GameState.IN_GAME)){
					this.cancel();
				}
	
				
				if(player.getInventory().getHeldItemSlot() != 4){
					
					player.playSound(player.getEyeLocation(), Sound.IRONGOLEM_THROW, 1f, 1f);
					skill = CharactersOpt.getSkillName(player, player.getInventory().getHeldItemSlot());
					
					player.getInventory().setHeldItemSlot(4);
					
					
				}
				ActionBar.actionBar(player, actionB + skill);
			}
		}.runTaskTimer(Main.getInstance(),0 ,1);
		
	}
	
	public static ItemStack standartItem(String s, String by){
		ItemStack titem = UtilItemByte.itemStack("160:"+by, 1);
		ItemMeta meta = titem.getItemMeta();
		meta.setDisplayName(s);
		meta.addEnchant(Enchantment.DURABILITY, 1000000, true);
		titem.setItemMeta(meta);
		return titem;
	}
}
