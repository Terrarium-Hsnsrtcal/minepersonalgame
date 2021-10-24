package game.character;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import game.main.GManager;
import game.main.HSGame;
import game.skill.o.MatchSkill;
import game.utils.CallbackInventory;
import game.utils.CallbackInventory.InventoryCallbackListener;
import game.utils.UtilOth;

public class CharactersOpt {

	public static HashMap<String, Characters> PLAYER_CHARACTER_MAP              = new HashMap<String, Characters>(); // player yap
	public static HashMap<Characters, String> CHARACTER_SKIN                    = new HashMap<Characters, String>();
	
	public static HashMap<Characters , HashMap<Integer, String>> CHARACTERS_MAP = new HashMap<Characters , HashMap<Integer, String>>();
	
	public static Characters DEFAULT_CHARACTER = Characters.MECHMEN;
	
	public static void addCharacter(Characters characterName, HashMap<Integer, String> skills) {
		CHARACTERS_MAP.put(characterName, skills);
		GManager.systemInfo("§aeklenen karakter adý: §e" + characterName + " §aMAP: " + skills);
	}
	
	public static void setCharacterSkin(Characters characterName, String skinName) {
		CHARACTER_SKIN.put(characterName, skinName);
	}
	
	public static void addCharacterPlayer(Player player, Characters characterName) {
		PLAYER_CHARACTER_MAP.put(player.getName(), characterName);
		GManager.systemInfo("§aPlayer: §e" + player + " §aMAP: " + PLAYER_CHARACTER_MAP);
	}
	
	public static void removeCharacterPlayer(Player player) {
		PLAYER_CHARACTER_MAP.remove(player.getName());
	}
	
	
	/*
	 * CHARACTER SKILL SET-GET
	 */
	
	
	public static void changeSkin(Player player) {
		HSGame game = GManager.getArenaViaPlayer(player);
		UtilOth.changeSkin(player, "__", game.getPlayers());
	}
	
	public static void setSkillMap(HashMap<Integer, String> f, int i, String skill) {
		f.put(i, skill);
		GManager.systemInfo("§aMAP: " + f + "§a Int: §1" + i + "§a skill: §b" + skill);
	}
	
	public static void setSkill(Characters character, int i, String skill) {
		HashMap<Integer, String> llput = CHARACTERS_MAP.get(character);
		
		llput.put(i, skill);
		
		CHARACTERS_MAP.replace(character, llput);
	}
	
	public static void invokeSkill(Player player, int i) {
		String key = getSkillName(player , i);
		MatchSkill.setInvoke(key, player);
	}
	
	public static String getSkillName(Player player, int i) {
		return CHARACTERS_MAP.get(PLAYER_CHARACTER_MAP.get(player.getName())).get(i);
	}
	

	/*
	 * CHARACTER CHOOSE INV
	 */
	
	
	public static void openChooseMenu(Player player){
		
		CallbackInventory inv = new CallbackInventory("§c§lKATÝL KARAKTERÝNÝ SEÇ", 18,  new InventoryCallbackListener() {
			@Override
			public void onClick(InventoryClickEvent event) {
				ItemStack item = event.getCurrentItem();
				PLAYER_CHARACTER_MAP.put(player.getName(), Characters.valueOf(item.getItemMeta().getDisplayName()));
				player.closeInventory();
			}

			@Override
			public void onClose(InventoryCloseEvent event) {
				
			}
		});
		
		int i = 1;
		for(Characters c : CHARACTERS_MAP.keySet()) {
			i++;
			ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
			SkullMeta meta = (SkullMeta) skull.getItemMeta();
			meta.setOwner(CHARACTER_SKIN.get(c));
			
			ArrayList<String> lore = new ArrayList<String>();
			
			meta.setLore(lore);
			skull.setItemMeta(meta);
			
			String s = c.toString();
			
			inv.setInv("397:1", s, i, false);
		}
		inv.open(player);
	}
	
}
