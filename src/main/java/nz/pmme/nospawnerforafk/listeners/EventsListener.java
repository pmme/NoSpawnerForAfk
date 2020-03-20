package nz.pmme.nospawnerforafk.listeners;

import nz.pmme.nospawnerforafk.NoSpawnerForAfk;
import org.bukkit.Location;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

import java.util.Collection;

public class EventsListener implements Listener
{
    private NoSpawnerForAfk plugin;
    public EventsListener( NoSpawnerForAfk plugin ) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onSpawnerSpawn( SpawnerSpawnEvent event )
    {
        int range = this.plugin.getConfig().getInt( "check-range" );
        CreatureSpawner spawner = event.getSpawner();
        Location spawnerLocation = spawner.getLocation();
        Collection<Entity> nearbyEntities = spawnerLocation.getWorld().getNearbyEntities( spawnerLocation, range, range, range, (e)->(e.getType()==EntityType.PLAYER) );
        
        boolean allPlayersAfk = true;
        for( Entity entity : nearbyEntities ) {
            if( entity instanceof Player ) {
                Player player = ( Player )entity;
                if( /*entity != afk*/false || player.hasPermission( "nospawnerforafk.bypass" ) ) {
                    allPlayersAfk = false;
                    break;
                }
            }
        }
        if( allPlayersAfk ) event.setCancelled( true );
    }

}
