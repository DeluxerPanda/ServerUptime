package se.deluxerpanda.serveruptime;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Comparator;
import java.util.Objects;

/*
 * Documentation written by ChatGPT
 */
public class ServerUpTime extends JavaPlugin implements CommandExecutor {
    public static long serverStart; // Variable to store the server start time

    @Override
    public void onEnable() {
        serverStart = System.currentTimeMillis(); // Set the server start time
        loadConfigFiles(); // Load the configuration files
        saveConfig(); // Save the configuration
        Objects.requireNonNull(getCommand("uptime")).setExecutor(this); // Register the command
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[" + ChatColor.AQUA + "Server Uptime" + ChatColor.GRAY + "]" + ChatColor.DARK_AQUA + " Started");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[" + ChatColor.AQUA + "Server Uptime" + ChatColor.GRAY + "]" + ChatColor.RED + " Stopped");
    }

    public void loadConfigFiles() {
        FileConfiguration config = getConfig();
        config.options().copyDefaults(true); // Copy default values to config
        // Add default values for configuration options
        config.addDefault("ServerUptime.Permissions", true);
        config.addDefault("ServerUptime.NoPermissions", ChatColor.RED + "You do not have permission to use this command.");
        config.addDefault("ServerUptime.ServerUptime", "&7[&bServer Uptime&7]");
        config.addDefault("ServerUptime.Days", "&bDays");
        config.addDefault("ServerUptime.Hours", "&bHours");
        config.addDefault("ServerUptime.Minutes", "&bMinutes");
        config.addDefault("ServerUptime.Seconds", "&bSeconds");
        config.addDefault("ServerUptime.numbers.Days", "&7");
        config.addDefault("ServerUptime.numbers.Hours", "&7");
        config.addDefault("ServerUptime.numbers.Minutes", "&7");
        config.addDefault("ServerUptime.numbers.Seconds", "&7");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

            FileConfiguration config = getConfig(); // Get the configuration

            // Check for permissions
            if (config.getBoolean("ServerUptime.Permissions") && !sender.hasPermission("serveruptime.use")) {
                sender.sendMessage(Objects.requireNonNull(config.getString("ServerUptime.NoPermissions")));
                return true;
            }

            // Calculate uptime
            long diff = System.currentTimeMillis() - serverStart;
            String uptimeName = Objects.requireNonNull(config.getString("ServerUptime.ServerUptime")).replace("&","§");
            int days = (int) (diff / 86400000);
            int hours = (int) (diff / 3600000 % 24);
            int minutes = (int) (diff / 60000 % 60);
            int seconds = (int) (diff / 1000 % 60);
            String ownDay = Objects.requireNonNull(config.getString("ServerUptime.Days")).replace("&","§");
            String ownHours = Objects.requireNonNull(config.getString("ServerUptime.Hours")).replace("&","§");
            String ownMinutes = Objects.requireNonNull(config.getString("ServerUptime.Minutes")).replace("&","§");
            String ownSeconds = Objects.requireNonNull(config.getString("ServerUptime.Seconds")).replace("&","§");

            String colorDay = Objects.requireNonNull(config.getString("ServerUptime.numbers.Days")).replace("&","§");
            String colorHours =  Objects.requireNonNull(config.getString("ServerUptime.numbers.Hours")).replace("&","§");
            String colorMinutes = Objects.requireNonNull(config.getString("ServerUptime.numbers.Minutes")).replace("&","§");
            String colorSeconds = Objects.requireNonNull(config.getString("ServerUptime.numbers.Seconds")).replace("&","§");

            // Construct the uptime message based on the time calculated
            if (days > 0) {
                sender.sendMessage(String.format("%s %s%d %s %s%d %s %s%d %s %s%d %s",
                        uptimeName, colorDay, days, ownDay, colorHours, hours, ownHours, colorMinutes, minutes, ownMinutes, colorSeconds, seconds, ownSeconds));
            } else if (hours > 0) {
                sender.sendMessage(String.format("%s %s%d %s %s%d %s %s%d %s",
                        uptimeName, colorHours, hours, ownHours, colorMinutes, minutes, ownMinutes, colorSeconds, seconds, ownSeconds));
            } else if (minutes > 0) {
                sender.sendMessage(String.format("%s %s%d %s %s%d %s",
                        uptimeName, colorMinutes, minutes, ownMinutes, colorSeconds, seconds, ownSeconds));
            } else {
                sender.sendMessage(String.format("%s %s%d %s",
                        uptimeName, colorSeconds, seconds, ownSeconds));
            }
            return true;
    }
}
