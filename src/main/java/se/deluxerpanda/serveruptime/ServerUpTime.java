package se.deluxerpanda.serveruptime;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerUpTime extends JavaPlugin implements CommandExecutor {
    public static long serverStart;

    public void onEnable() {
        serverStart = System.currentTimeMillis();
        loadConfigFiles();
        saveConfig();
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[" + ChatColor.AQUA + "Server Uptime" + ChatColor.GRAY + "]" + ChatColor.DARK_AQUA + " Started");
    }

    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[" + ChatColor.AQUA + "Server Uptime" + ChatColor.GRAY + "]" + ChatColor.RED + " Stoped");
    }

    public void loadConfigFiles() {
        getConfig().options().copyDefaults(true);
    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (cmd.getName().equalsIgnoreCase("uptime")) {
            if (getConfig().getString("ServerUptime.Permissions").equals("true") && !sender.hasPermission("serveruptime.use")) {
                sender.sendMessage(getConfig().getString("ServerUptime.NoPermissions"));
            } else {

                long diff = System.currentTimeMillis() - ServerUpTime.serverStart;
                String UptimeName = String.valueOf(getConfig().getString("ServerUptime.ServerUptime"));
                int days = (int) (diff / 86400000);
                int hours = (int) (diff / 3600000 % 24);
                int minutes = (int) (diff / 60000 % 60);
                int seconds = (int) (diff / 1000 % 60);
                String OwnDay = String.valueOf(getConfig().getString("ServerUptime.Days"));
                String OwnHours = String.valueOf(getConfig().getString("ServerUptime.Hours"));
                String OwnMinutes = String.valueOf(getConfig().getString("ServerUptime.Minutes"));
                String OwnSeconds = String.valueOf(getConfig().getString("ServerUptime.Seconds"));

                String ColorDay = String.valueOf(getConfig().getString("ServerUptime.numbers.Days"));
                String ColorHours = String.valueOf(getConfig().getString("ServerUptime.numbers.Hours"));
                String ColorMinutes = String.valueOf(getConfig().getString("ServerUptime.numbers.Minutes"));
                String ColorSeconds = String.valueOf(getConfig().getString("ServerUptime.numbers.Seconds"));

                if (minutes == 0) sender.sendMessage(UptimeName + " " + ColorSeconds + seconds + " " + OwnSeconds);
                else if (hours == 0)
                    sender.sendMessage(UptimeName + " " + ColorMinutes + minutes + " " + OwnMinutes + " " + ColorSeconds + seconds + " " + OwnSeconds);
                else if (days == 0)
                    sender.sendMessage(UptimeName + " " + ColorHours + hours + " " + OwnHours + " " + ColorMinutes + minutes + " " + OwnMinutes + " " + ColorSeconds + seconds + " " + OwnSeconds);
                else
                    sender.sendMessage(UptimeName + " " + ColorDay + days + " " + OwnDay + " " + ColorHours + hours + " " + OwnHours + " " + ColorMinutes + minutes + " " + OwnMinutes + " " + ColorSeconds + seconds + " " + OwnSeconds);


            }
        }


        return true;
        }


    }

