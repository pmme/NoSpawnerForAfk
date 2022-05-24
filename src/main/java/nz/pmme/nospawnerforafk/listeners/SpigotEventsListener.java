package nz.pmme.nospawnerforafk.listeners;

import nz.pmme.nospawnerforafk.NoSpawnerForAfk;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SpigotEventsListener implements Listener
{
    private NoSpawnerForAfk plugin;
    public SpigotEventsListener(NoSpawnerForAfk plugin ) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawnerSpawnSpigot( org.bukkit.event.entity.SpawnerSpawnEvent event )
    {
        if( !this.plugin.isNoSpawnerForAfkEnabled() ) return;
        org.bukkit.block.CreatureSpawner spawner = event.getSpawner();
        Location spawnerLocation = spawner.getLocation();
        this.plugin.checkSpawnEvent(event, spawnerLocation, "Spigot");
    }

    @EventHandler
    public void onNaturalSpawn( org.bukkit.event.entity.EntitySpawnEvent event )
    {
        if( !this.plugin.isNoSpawnerForAfkEnabled() ) return;
        if( event.getEntityType().isAlive() ) {
            this.plugin.checkSpawnEvent( event, event.getEntity().getLocation(), "Natural" );
        }
    }
}
