package fr.loyle.ktn.game;

import fr.loyle.ktn.KTN;
import fr.loyle.ktn.game.PlayersManager;
import fr.loyle.ktn.mods.ModsManager;

public class Game {
	
	
	
	// Ce fichier me sert juste à initialiser toutes les class pour le jeu
	private KTN plugin;
	private GameManager GameManager;
	private PlayersManager PlayersManager;
	private GameHandler GameHandler;
	private TeamsManager TeamsManager;
	private ModsManager ModsManager;
	
	public Game(KTN pl) {
		this.plugin = pl;
		this.GameManager = new GameManager(this.plugin);
		this.PlayersManager = new PlayersManager(this.plugin);
		this.GameHandler = new GameHandler(this.plugin);
		this.TeamsManager = new TeamsManager(this.plugin);
		this.ModsManager = new ModsManager(this.plugin);
	}
	
	public GameManager getGameManager() {
		return this.GameManager;
	}
	
	public PlayersManager getPlayerManager() {
		return this.PlayersManager;
	}
	
	public GameHandler getGameHandler() {
		return this.GameHandler;
	}
	
	public TeamsManager getTeamsManager() {
		return this.TeamsManager;
	}
	
	public ModsManager getModsManager() {
		return this.ModsManager;
	}
}