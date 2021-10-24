package g.tab;

import org.bukkit.entity.Player;

import game.utils.PlayerList;

public class GTab {

	private Player player;
	private PlayerList playerList;
	
	
	public GTab(Player player) {
		this.player = player;
		this.playerList = new PlayerList(player,PlayerList.SIZE_FOUR);
	}
	
	public void createTab() {
		 
		
		
		playerList.initTable();
		
		playerList.updateSlot(0,"Top left");
		 
		//Sets the HeaderFooter.
		playerList.setHeaderFooter("Welcome","Test Message");
		 
		
	}
	
	public void delete() {
		this.playerList.removePlayer(this.player);
	}
	
}
