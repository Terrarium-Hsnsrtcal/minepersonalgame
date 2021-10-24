package game.skill.o;

import org.bukkit.entity.Player;

import game.main.GManager;
import game.main.HSGame;

public class SkillMana {

	public static boolean decrease(Player player, int i) {
		HSGame g = GManager.getArenaViaPlayer(player);
		return g.getPc().decreaseMana(i);
	}
	
	public static void increase(Player player, int i) {
		HSGame g = GManager.getArenaViaPlayer(player);
		g.getPc().increaseMana(i);
	}
	
}
