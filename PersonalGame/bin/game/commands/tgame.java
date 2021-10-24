package game.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import game.main.GManager;
import game.skill.o.MatchSkill;
import game.utils.UtilOth;

public class tgame implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = (Player) sender;		

		if(args.length == 1){
			
			if(args[0].equals("edit")) {
				if(!player.isOp()) return false;
				GManager.editMainWorld(player);
			}
			
			if(args[0].equals("editdone")) {
				GManager.dddd(player);
			}
			
			if(args[0].equals("test")) {
				//UtilOth.changeSkin(player, "LaxTirrek");
			}
			return false;
			
		}
		
		GManager.openArenaMenu(player);
		//UtilOth.changeSkin(player, "LaxTerrier");
		return false;
	}
}