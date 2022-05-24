package nz.pmme.nospawnerforafk.commands;

import nz.pmme.nospawnerforafk.NoSpawnerForAfk;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter
{
    private NoSpawnerForAfk plugin;
    private static final String[] firstArguments = {
            "on",
            "off",
            "range",
            "log"
    };
    private static final String[] secondArguments = {
            "on",
            "off"
    };
    private static final String[] msgNoSpawnerForAfkUsage = {
            ChatColor.DARK_AQUA + "NoSpawnerForAfk command usage:",
            ChatColor.WHITE + "/nospawnerforafk on/off" + ChatColor.DARK_AQUA + " - Turn NoSpawnerForAfk ON or OFF.",
            ChatColor.WHITE + "/nospawnerforafk range <range>" + ChatColor.DARK_AQUA + " - Set the range to check for players.",
            ChatColor.WHITE + "/nospawnerforafk log on/off" + ChatColor.DARK_AQUA + " - Turn debug logging ON or OFF."
    };
    private static final String msgNoSpawnerForAfkEnabled = ChatColor.GREEN + "NoSpawnerForAfk enabled";
    private static final String msgNoSpawnerForAfkDisabled = ChatColor.GRAY + "NoSpawnerForAfk disabled";
    private static final String msgNoSpawnerForAfkRangeSet = ChatColor.DARK_AQUA + "NoSpawnerForAfk range set to %range%.";
    private static final String msgNoSpawnerForAfkBadRange = ChatColor.RED + "You must finish with an integer number for the range, e.g. 16";
    private static final String msgNoSpawnerForAfkLoggingOn = ChatColor.GREEN + "NoSpawnerForAfk will now log to the console.";
    private static final String msgNoSpawnerForAfkLoggingOff = ChatColor.GRAY + "NoSpawnerForAfk logging disabled";
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
            if(args.length == 2)
            {
                return Arrays.asList( secondArguments );
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
            sender.sendMessage( msgNoSpawnerForAfkRangeSet.replace( "%range%", String.valueOf( plugin.getConfig().getInt( "check-range", 16 ) ) ) );
            sender.sendMessage( msgNoSpawnerForAfkUsage );
            return true;
        }
        else
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
            case "range":
                if(!sender.hasPermission("nospawnerforafk.setrange")) {
                    sender.sendMessage(msgNoPermission);
                    return true;
                }
                if( args.length == 2 ) {
                    try {
                        int range = Integer.parseInt(args[1]);
                        plugin.setRange( range );
                        plugin.getConfig().set( "check-range", range );
                        plugin.saveConfig();
                        sender.sendMessage( msgNoSpawnerForAfkRangeSet.replace( "%range%", String.valueOf(range) ) );
                        return true;
                    } catch( NumberFormatException e ) {
                        sender.sendMessage(msgNoSpawnerForAfkBadRange);
                        return true;
                    }
                }
                break;
            case "log":
                if(!sender.hasPermission("nospawnerforafk.log")) {
                    sender.sendMessage(msgNoPermission);
                    return true;
                }
                if( args.length == 2 ) {
                    switch( args[1].toLowerCase() ) {
                    case "on":
                        plugin.setLogging(true);
                        sender.sendMessage(msgNoSpawnerForAfkLoggingOn);
                        return true;
                    case "off":
                        plugin.setLogging(false);
                        sender.sendMessage(msgNoSpawnerForAfkLoggingOff);
                        return true;
                    }
                }
                break;
            }
        }
        sender.sendMessage(msgNoSpawnerForAfkUsage);
        return true;
    }
}
