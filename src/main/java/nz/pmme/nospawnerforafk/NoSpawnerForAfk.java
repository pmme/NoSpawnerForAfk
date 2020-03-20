package nz.pmme.nospawnerforafk;

import nz.pmme.nospawnerforafk.commands.Commands;
import nz.pmme.nospawnerforafk.listeners.EventsListener;
import org.bukkit.plugin.java.JavaPlugin;

public class NoSpawnerForAfk extends JavaPlugin
{
    private boolean enabled = true;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getCommand("nospawnerforafk").setExecutor(new Commands(this));
        this.getServer().getPluginManager().registerEvents( new EventsListener(this), this );
        this.enableNoSpawnerForAfk();
    }

    @Override
    public void onDisable() {
        this.disableNoSpawnerForAfk();
    }

    public boolean isNoSpawnerForAfkEnabled() {
        return enabled;
    }
    public void enableNoSpawnerForAfk() {
        enabled = true;
    }
    public void disableNoSpawnerForAfk() {
        enabled = false;
    }
}

