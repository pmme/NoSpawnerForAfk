package nz.pmme.nospawnerforafk.listeners;

import nz.pmme.nospawnerforafk.NoSpawnerForAfk;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EpicSpawnersListener implements Listener
{
    private NoSpawnerForAfk plugin;
    public EpicSpawnersListener(NoSpawnerForAfk plugin ) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawnerSpawnEpicSpawners( com.songoda.epicspawners.api.events.SpawnerSpawnEvent event )
    {
        if( !this.plugin.isNoSpawnerForAfkEnabled() ) return;
        com.songoda.epicspawners.spawners.spawner.Spawner spawner = event.getSpawner();
        Location spawnerLocation = spawner.getLocation();
        this.plugin.checkSpawnEvent(event, spawnerLocation, "EpicSpawners");
    }
}
