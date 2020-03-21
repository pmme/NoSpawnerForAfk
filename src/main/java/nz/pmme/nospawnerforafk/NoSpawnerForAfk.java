package nz.pmme.nospawnerforafk;

import nz.pmme.nospawnerforafk.commands.Commands;
import nz.pmme.nospawnerforafk.listeners.EventsListener;
import org.bukkit.plugin.java.JavaPlugin;

public class NoSpawnerForAfk extends JavaPlugin
{
    private boolean enabled = true;
    private net.ess3.api.IEssentials essentials = null;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getCommand("nospawnerforafk").setExecutor(new Commands(this));
        this.getServer().getPluginManager().registerEvents( new EventsListener(this), this );

        this.essentials = (net.ess3.api.IEssentials)this.getServer().getPluginManager().getPlugin( "Essentials" );
        if( this.essentials == null ) {
            this.getServer().getLogger().severe("Disabling NoSpawnerForAfk because Essentials was not found.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.enableNoSpawnerForAfk();
    }

    @Override
    public void onDisable() {
        this.disableNoSpawnerForAfk();
    }

    public boolean isNoSpawnerForAfkEnabled() {
        return this.enabled;
    }
    public void enableNoSpawnerForAfk() {
        this.enabled = true;
    }
    public void disableNoSpawnerForAfk() {
        this.enabled = false;
    }

    public net.ess3.api.IEssentials getEssentials() {
        return this.essentials;
    }
}

