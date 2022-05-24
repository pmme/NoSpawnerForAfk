package nz.pmme.nospawnerforafk;

import nz.pmme.nospawnerforafk.commands.Commands;
import nz.pmme.nospawnerforafk.listeners.EpicSpawnersListener;
import nz.pmme.nospawnerforafk.listeners.SpigotEventsListener;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

public class NoSpawnerForAfk extends JavaPlugin
{
    private boolean enabled = true;
    private boolean logging = false;
    private int range = 16;
    private net.ess3.api.IEssentials essentials = null;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.range = this.getConfig().getInt( "check-range", 16 );
        this.getCommand("nospawnerforafk").setExecutor(new Commands(this));
        this.getServer().getPluginManager().registerEvents( new SpigotEventsListener(this), this );
        Plugin esPlugin = this.getServer().getPluginManager().getPlugin("EpicSpawners");
        if( esPlugin != null ) {
            int esMajorVersion = Character.valueOf( esPlugin.getDescription().getVersion().charAt( 0 ) );
            if( esMajorVersion < 7 ) {
                this.getServer().getLogger().severe( "Disabling NoSpawnerForAfk because this version requires EpicSpawners version 7 or later." );
                this.getServer().getPluginManager().disablePlugin(this);
                return;
            }
            this.getServer().getPluginManager().registerEvents( new EpicSpawnersListener(this), this );
        }
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
    public boolean isLoggingEnabled() {
        return this.logging;
    }
    public void setLogging( boolean enableLogging ) {
        this.logging = enableLogging;
    }
    public void setRange( int range ) {
        this.range = range;
    }

    public net.ess3.api.IEssentials getEssentials() {
        return this.essentials;
    }

    public void checkSpawnEvent(org.bukkit.event.Cancellable event, Location spawnerLocation, String spawnType)
    {
        Collection<Entity> nearbyEntities = spawnerLocation.getWorld().getNearbyEntities( spawnerLocation, this.range, this.range, this.range, (e)->(e.getType()==EntityType.PLAYER) );

        int nearByPlayers = 0;
        boolean allPlayersAfk = true;
        for( Entity entity : nearbyEntities ) {
            if( entity instanceof Player) {
                ++nearByPlayers;
                Player player = ( Player )entity;
                net.ess3.api.IUser essentialsUser = this.getEssentials().getUser( player );
                if( !essentialsUser.isAfk() || player.hasPermission( "nospawnerforafk.bypass" ) ) {
                    allPlayersAfk = false;
                    break;
                }
            }
        }
        if( allPlayersAfk ) event.setCancelled( true );
        if( this.isLoggingEnabled() ) {
            String spawnerLocationAsString = spawnerLocation.getBlockX() + "," + spawnerLocation.getBlockY() + "," + spawnerLocation.getBlockZ();
            if( nearByPlayers == 0 ) {
                this.getLogger().info( "Blocked " + spawnType + " spawn at " + spawnerLocationAsString + ". No nearby players." );
            } else if( allPlayersAfk ) {
                this.getLogger().info( "Blocked " + spawnType + " spawn at " + spawnerLocationAsString + ". All " + nearByPlayers + " nearby players are AFK." );
            } else {
                this.getLogger().info( "Allowed " + spawnType + " spawn at " + spawnerLocationAsString );
            }
        }
    }
}

