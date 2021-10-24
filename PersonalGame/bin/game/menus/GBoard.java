package game.menus;

import org.bukkit.entity.Player;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import game.main.GManager;
import game.main.GameState;
import game.main.HSGame;

public class GBoard {

	private BPlayerBoard board;
	
	private String sid = "§eharika.server.nw", role = "§d§LROL: ";
	private HSGame gs;
	
	public GBoard(Player player) {
		this.gs = GManager.getArenaViaPlayer(player);

		this.board = Netherboard.instance().createBoard(player,
				"§c | Â§dÂ§l" + GManager.getArenaViaPlayer(player).getGameName() + "§c | ");

		this.board.setAll("§e", "§a", getGameState(), role, "§aKalan Zaman: " + gs.getTime() , sid, "",
				"§e-+");
	}

	public void delete() {
		this.board.delete();
	}
	
	public void setLine(int line, String s) {
		this.board.set(s, line);
	}

	public void addStringToLine(int line, String s) {
		this.board.set(this.board.get(line) + s, line);
	}

	public String getGameState() {
		
		String state = "";
		if (this.gs.gameState() == GameState.LOBBY) {
			state = "§7§lOYUNCULAR BEKLENÝYOR";
		}
		if (this.gs.gameState() == GameState.STARTING) {
			state = "§c§MAGNUM PARASINA GÖRE ÇOK KÜÇÜK";
		}
		return state;
	}

	public void updateState() {
		this.board.set(getGameState(), 6);
		this.board.set( "§aKalan Zaman: " + gs.getTime(), 4);
	}

}
