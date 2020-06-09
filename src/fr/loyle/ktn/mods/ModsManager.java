package fr.loyle.ktn.mods;

import fr.loyle.ktn.KTN;

public class ModsManager {
	private KTN plugin;
	private TaupeMod taupemod;
	private SwitchMod switchmod;
	private String path = "KTN.";
	
	
	public ModsManager(KTN pl) {
		this.plugin = pl;
		Boolean teams = this.plugin.getConfig().getBoolean(path + "PlayWithTeams");
		if(this.plugin.getConfig().getBoolean(path + "TaupeMode.Activated") && teams) {
			this.taupemod = new TaupeMod(this.plugin);
		}
		if(this.plugin.getConfig().getBoolean(path + "SwitchMode.Activated") && teams) {
			this.switchmod = new SwitchMod(this.plugin);
		}
	}
	
	public void callMods(int Episode, int MinutesLeft, int SecondLeft) {
		Boolean teams = this.plugin.getConfig().getBoolean(path + "PlayWithTeams");
		if(this.plugin.getConfig().getBoolean(path + "TaupeMode.Activated") && teams) {
			this.taupemod.check(Episode, MinutesLeft, SecondLeft);
		}
		if(this.plugin.getConfig().getBoolean(path + "SwitchMode.Activated") && teams) {
			this.switchmod.check(Episode, MinutesLeft, SecondLeft);
		}
	}
	
	
	public TaupeMod getTaupeMod() {
		return this.taupemod;
	}
}
