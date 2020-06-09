package fr.loyle.ktn.game;


import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import fr.loyle.ktn.KTN;

public class Teams {
	
	@SuppressWarnings("unused")
	private String path = "KTN.";
	private String TeamName;
	private int TeamID;
	private String DisplayName;
	private String TeamColor;
	private Team team;
	private String Prefix;
	private ArrayList<Player> players = new ArrayList<>();
	
	public KTN plugin;
	
	public Teams(KTN plugin, String TeamName, String DisplayName, String TeamColor, int TeamID, String Prefix) {
		this.plugin = plugin;
		
		this.TeamName = TeamName;
		this.DisplayName = DisplayName;
		this.TeamColor = colorize(TeamColor);
		this.TeamID = TeamID;
		this.Prefix = Prefix;
		
		this.team = this.plugin.scoreboard.createTeam(this.TeamName, this.DisplayName, this.TeamColor, this.TeamID, this.Prefix);
	}
	
	@SuppressWarnings("deprecation")
	public void addPlayer(Player player) {
		this.players.add(player);
		this.team.addPlayer(player);
		
		System.out.println("[KTN] "+ player.getDisplayName() + " join the team " + this.getDisplayName());
		
		for(Player playerlist : this.plugin.getServer().getOnlinePlayers()) {
			playerlist.setScoreboard(this.plugin.scoreboard.getScoreboard());
		}
	}
	@SuppressWarnings("deprecation")
	public void removePlayer(Player player) {
		if(this.players.contains(player)) {
			this.players.remove(player);
			this.team.removePlayer(player);
			
			System.out.println("[KTN] "+ player.getDisplayName() + " leave the team " + this.getDisplayName());
		}
	}
	
	
	// GETTERS / SETTERS
	public String getName() {
		return this.TeamName;
	}
	public String getDisplayName() {
		return this.DisplayName;
	}
	public String getColor() {
		return this.TeamColor;
	}
	public ArrayList<Player> getPlayers() {
		return this.players;
	}
	public String colorize(String message) {
        return message.replaceAll("&([a-f0-9])", ChatColor.COLOR_CHAR + "$1");
    }

	public boolean isAlive() {
		if(this.team.getSize() != 0) {
			return true;
		}
		return false;
	}
	
	public int getSize() {
		return this.team.getSize();
	}
	
	public Boolean checkPlayerIsIn(Player player) {
		return this.players.contains(player);
	}
}
