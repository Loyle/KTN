package fr.loyle.ktn.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import fr.loyle.ktn.KTN;

public class PrintConfig implements BasicCommand {

	private KTN plugin;
	private String path = "KTN.";

	public PrintConfig(KTN pl) {
		this.plugin = pl;
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		FileConfiguration config = this.plugin.getConfig();
		if (KTN.hasPermission(player, getPermission()).booleanValue()) {
			Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "---   Configuration actuelle du KTN   ---");
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(ChatColor.GOLD + "- Joueurs min: " + ChatColor.RED + config.getInt(path + "MinPlayers") + ChatColor.GOLD + " | Joueurs max: " + ChatColor.RED + config.getInt(path + "MaxPlayers"));
			int mapSize = config.getInt(path + "Map.Size");
			Bukkit.broadcastMessage(ChatColor.GOLD + "- Taille de la map: " + ChatColor.RED + mapSize + ChatColor.GOLD + " (" + ((mapSize / 2) - mapSize) + "/" + (mapSize/2) + ")");
			Bukkit.broadcastMessage(ChatColor.GOLD + "- Jour permanent: " + ChatColor.RED + (config.getBoolean(path + "DoLightCycle.Do") ? "D�sactiv�" : "Activ�"));
			Bukkit.broadcastMessage(ChatColor.GOLD + "- Potions LVL 2: " + ChatColor.RED + (config.getBoolean(path + "PotionLvlDeux") ? "Activ�e" : "D�sactiv�e"));
			Bukkit.broadcastMessage(ChatColor.GOLD + "- R�g�n�ration: " + ChatColor.RED + (config.getBoolean(path + "NaturalRegeneration") ? "Activ�e" : "D�sactiv�e"));
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(ChatColor.GOLD + "---   Gestion de la bordure   ---");
			Bukkit.broadcastMessage(ChatColor.GOLD + "- R�duction � l'�pisode: " + ChatColor.RED + config.getInt(path + "WallReduce.Episode"));
			Bukkit.broadcastMessage(ChatColor.GOLD + "- Dur�e de r�duction: " + ChatColor.RED + config.getInt(path + "WallReduce.TimeForReduce") +" min");
			int borderSize = config.getInt(path + "WallReduce.Size");
			Bukkit.broadcastMessage(ChatColor.GOLD + "- Taille final: " + ChatColor.RED + borderSize + ChatColor.GOLD + " (" + ((borderSize / 2) - borderSize) + "/" + (borderSize/2) + ")");
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(ChatColor.GOLD + "- Teams: " + ChatColor.RED + (config.getBoolean(path + "PlayWithTeams") ? "Activ�e" : "D�sactiv�e"));
			Bukkit.broadcastMessage(ChatColor.GOLD + "- Spawns al�atoire: " + ChatColor.RED + (config.getBoolean(path + "RandomSpawns") ? "Activ�e" : "D�sactiv�e"));
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(ChatColor.GOLD + "---   Gestion des mods   ---");
			Bukkit.broadcastMessage(ChatColor.GOLD + "- TaupeMode: " + ChatColor.RED + (config.getBoolean(path + "TaupeMode.Activated") ? "Activ�e" : "D�sactiv�e"));
			if(this.plugin.getConfig().getBoolean(path + "TaupeMode.Activated") && this.plugin.getConfig().getBoolean(path + "PlayWithTeams")) {
				Bukkit.broadcastMessage(ChatColor.GOLD + "  - Taupe(s) par teams: " + ChatColor.RED + config.getInt(path + "TaupeMode.TaupePerTeam"));
				Bukkit.broadcastMessage(ChatColor.GOLD + "  - Nombre de team(s) de taupes: " + ChatColor.RED + config.getInt(path + "TaupeMode.NumberTaupeTeams"));
				Bukkit.broadcastMessage(ChatColor.GOLD + "  - Annonce des taupes � l'�pisode: " + ChatColor.RED + config.getInt(path + "TaupeMode.TaupeSelect"));
				Bukkit.broadcastMessage(ChatColor.GOLD + "  - AutoReveal � l'�pisode: " + ChatColor.RED + config.getInt(path + "TaupeMode.AutoReveal"));
				Bukkit.broadcastMessage(ChatColor.GOLD + "  - Pr�-s�lection des taupes: " + ChatColor.RED + (config.getBoolean(path + "TaupeMode.PreSelect") ? "Activ�e" : "D�sactiv�e"));
			}
			Bukkit.broadcastMessage(ChatColor.GOLD + "- SwitchMode: " + ChatColor.RED + (config.getBoolean(path + "SwitchMode.Activated") ? "Activ�e" : "D�sactiv�e"));
			if(this.plugin.getConfig().getBoolean(path + "SwitchMode.Activated") && this.plugin.getConfig().getBoolean(path + "PlayWithTeams")) {
				Bukkit.broadcastMessage(ChatColor.GOLD + "  - Les switchs ont lieu entre les �pisodes : " + ChatColor.RED + config.getInt(path + "SwitchMode.StartEpisode") + ChatColor.GOLD + " � " + ChatColor.RED + config.getInt(path + "SwitchMode.EndEpisode"));
				Bukkit.broadcastMessage(ChatColor.GOLD + "  - Les joueurs sont switch au bout de " + ChatColor.RED + config.getInt(path + "SwitchMode.SwitchTime") + ChatColor.GOLD + " min de chaque �pisodes");
			}
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(ChatColor.GOLD + "- Chaques kills donnent " + ChatColor.RED + "1" + ChatColor.GOLD + " pomme d'or \n- le reveal donne " + ChatColor.RED + "1" + ChatColor.GOLD + " pomme d'or \n- Pour ce reveal: " + ChatColor.RED + "/ktn reveal" + ChatColor.GOLD + " \n- Pour le kit: " + ChatColor.RED + "/ktn kit" + ChatColor.GOLD + " \n- Pour le tchat des taupes: " + ChatColor.RED + "/ktn t" + ChatColor.GOLD + " (tapez 1 fois pour entrer, tapez 1 fois pour sortir du tchat");
		}
		return true;
	}

	@Override
	public String help(Player p) {
		if (KTN.hasPermission(p, getPermission()).booleanValue()) {
			return "/ktn printcfg - List la configuration actuelle";
		}
		return "";
	}

	@Override
	public String getPermission() {
		return "KTN.admin";
	}
}
