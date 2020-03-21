package nz.pmme.nospawnerforafk.commands;

import nz.pmme.nospawnerforafk.NoSpawnerForAfk;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter
{
    private NoSpawnerForAfk plugin;
    private static final String[] firstArguments = {
            "on",
            "off"
    };
    private static final String[] msgNoSpawnerForAfkUsage = {
            ChatColor.DARK_AQUA + "NoSpawnerForAfk command usage:",
            ChatColor.WHITE + "/nospawnerforafk on" + ChatColor.DARK_AQUA + " - Turn NoSpawnerForAfk ON.",
            ChatColor.WHITE + "/nospawnerforafk off" + ChatColor.DARK_AQUA + " - Turn NoSpawnerForAfk OFF."
    };
    private static final String msgNoSpawnerForAfkEnabled = ChatColor.GREEN + "NoSpawnerForAfk enabled";
    private static final String msgNoSpawnerForAfkDisabled = ChatColor.GRAY + "NoSpawnerForAfk disabled";
    private static final String msgNoSpawnerForAfkNoConsole = "This command must be used by an active player.";
    private static final String msgNoPermission = ChatColor.RED + "You do not have permission to use this command.";

    public Commands(NoSpawnerForAfk plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args)
    {
        if( commandSender.hasPermission("nospawnerforafk.enable") ) {
            if(args.length == 1)
            {
                List<String> matchingFirstArguments = new ArrayList<>();
                String arg0lower = args[0].toLowerCase();
                for(String argument : firstArguments) {
                    if(arg0lower.isEmpty() || argument.toLowerCase().startsWith(arg0lower)) {
                        matchingFirstArguments.add(argument);
                    }
                }
                return matchingFirstArguments;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args )
    {
        if( !sender.hasPermission("nospawnerforafk.enable") ) {
            sender.sendMessage(msgNoPermission);
            return true;
        }
        if( args.length == 0 ) {
            sender.sendMessage( plugin.isNoSpawnerForAfkEnabled() ? msgNoSpawnerForAfkEnabled : msgNoSpawnerForAfkDisabled );
            sender.sendMessage( msgNoSpawnerForAfkUsage );
            return true;
        }
        else if( args.length == 1 )
        {
            switch( args[0].toLowerCase() ) {
            case "on":
                if(!sender.hasPermission("nospawnerforafk.enable")) {
                    sender.sendMessage(msgNoPermission);
                    return true;
                }
                sender.sendMessage( msgNoSpawnerForAfkEnabled );
                plugin.enableNoSpawnerForAfk();
                return true;
            case "off":
                if(!sender.hasPermission("nospawnerforafk.enable")) {
                    sender.sendMessage(msgNoPermission);
                    return true;
                }
                sender.sendMessage( msgNoSpawnerForAfkDisabled );
                plugin.disableNoSpawnerForAfk();
                return true;
            }
        }
        sender.sendMessage(msgNoSpawnerForAfkUsage);
        return true;
    }
}