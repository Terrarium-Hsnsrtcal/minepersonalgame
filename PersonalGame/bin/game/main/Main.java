package game.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import game.commands.tgame;
import game.listeners.GListener;


public class Main extends JavaPlugin{

	private static Main instance;
	
	@Override
	public void onEnable(){
		instance = this;
		
		registerListeners();
		registerCommands();

		GManager.setGame();

	}
	
	@Override
	public void onDisable(){
		
	}
	
	public void registerListeners(){
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new GListener(), this);
	}
	
	public void registerCommands(){
		this.getCommand("tgame").setExecutor(new tgame());
	}
	
	public static Main getInstance(){
		return instance;
	}



}
