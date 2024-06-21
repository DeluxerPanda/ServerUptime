package se.deluxerpanda.serveruptime;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

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
        config.addDefault("ServerUptime.Permissions", "true");
        config.addDefault("ServerUptime.NoPermissions", ChatColor.RED + "You do not have permission to use this command.");
        config.addDefault("ServerUptime.ServerUptime", "&aServer Uptime:");
        config.addDefault("ServerUptime.Days", "&aDays");
        config.addDefault("ServerUptime.Hours", "&aHours");
        config.addDefault("ServerUptime.Minutes", "&aMinutes");
        config.addDefault("ServerUptime.Seconds", "&aSeconds");
        config.addDefault("ServerUptime.numbers.Days", "&e");
        config.addDefault("ServerUptime.numbers.Hours", "&e");
        config.addDefault("ServerUptime.numbers.Minutes", "&e");
        config.addDefault("ServerUptime.numbers.Seconds", "&e");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("uptime")) {
            FileConfiguration config = getConfig(); // Get the configuration

            // Check for permissions
            if (config.getBoolean("ServerUptime.Permissions") && !sender.hasPermission("serveruptime.use")) {
                sender.sendMessage(Objects.requireNonNull(config.getString("ServerUptime.NoPermissions")));
                return true;
            }

            // Calculate uptime
            long diff = System.currentTimeMillis() - serverStart;
            String uptimeName = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("ServerUptime.ServerUptime")));
            int days = (int) (diff / 86400000);
            int hours = (int) (diff / 3600000 % 24);
            int minutes = (int) (diff / 60000 % 60);
            int seconds = (int) (diff / 1000 % 60);
            String ownDay = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("ServerUptime.Days")));
            String ownHours = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("ServerUptime.Hours")));
            String ownMinutes = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("ServerUptime.Minutes")));
            String ownSeconds = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("ServerUptime.Seconds")));

            String colorDay = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("ServerUptime.numbers.Days")));
            String colorHours = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("ServerUptime.numbers.Hours")));
            String colorMinutes = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("ServerUptime.numbers.Minutes")));
            String colorSeconds = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("ServerUptime.numbers.Seconds")));

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
        return false;
    }
}
