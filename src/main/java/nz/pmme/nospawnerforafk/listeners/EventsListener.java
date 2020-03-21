package nz.pmme.nospawnerforafk.listeners;

import nz.pmme.nospawnerforafk.NoSpawnerForAfk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collection;

public class EventsListener implements Listener
{
    private NoSpawnerForAfk plugin;
    public EventsListener( NoSpawnerForAfk plugin ) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawnerSpawnSpigot( org.bukkit.event.entity.SpawnerSpawnEvent event )
    {
        int range = this.plugin.getConfig().getInt( "check-range" );
        org.bukkit.block.CreatureSpawner spawner = event.getSpawner();
        Location spawnerLocation = spawner.getLocation();
        Collection<Entity> nearbyEntities = spawnerLocation.getWorld().getNearbyEntities( spawnerLocation, range, range, range, (e)->(e.getType()==EntityType.PLAYER) );

        boolean allPlayersAfk = true;
        for( Entity entity : nearbyEntities ) {
            if( entity instanceof Player ) {
                Player player = ( Player )entity;
                net.ess3.api.IUser essentialsUser = plugin.getEssentials().getUser( player );
                if( !essentialsUser.isAfk() || player.hasPermission( "nospawnerforafk.bypass" ) ) {
                    allPlayersAfk = false;
                    break;
                }
            }
        }
        if( allPlayersAfk ) event.setCancelled( true );
    }

    @EventHandler
    public void onSpawnerSpawnEpicSpawners( com.songoda.epicspawners.api.events.SpawnerSpawnEvent event )
    {
        int range = this.plugin.getConfig().getInt( "check-range" );
        com.songoda.epicspawners.spawners.spawner.Spawner spawner = event.getSpawner();
        Location spawnerLocation = spawner.getLocation();
        Collection<Entity> nearbyEntities = spawnerLocation.getWorld().getNearbyEntities( spawnerLocation, range, range, range, (e)->(e.getType()==EntityType.PLAYER) );

        boolean allPlayersAfk = true;
        for( Entity entity : nearbyEntities ) {
            if( entity instanceof Player ) {
                Player player = ( Player )entity;
                net.ess3.api.IUser essentialsUser = plugin.getEssentials().getUser( player );
                if( !essentialsUser.isAfk() || player.hasPermission( "nospawnerforafk.bypass" ) ) {
                    allPlayersAfk = false;
                    break;
                }
            }
        }
        if( allPlayersAfk ) event.setCancelled( true );
    }

}
