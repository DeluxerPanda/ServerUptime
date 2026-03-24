package se.deluxerpanda.serveruptime;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class ServerUpTime extends JavaPlugin implements CommandExecutor {
    public static long serverStart;

    FileConfiguration config = getConfig();

    private final String ownDay = Objects.requireNonNull(config.getString("ServerUptime.Days")).replace("&","§");
    private final String ownHours = Objects.requireNonNull(config.getString("ServerUptime.Hours")).replace("&","§");
    private final String ownMinutes = Objects.requireNonNull(config.getString("ServerUptime.Minutes")).replace("&","§");
    private final String ownSeconds = Objects.requireNonNull(config.getString("ServerUptime.Seconds")).replace("&","§");

    private final String colorDay = Objects.requireNonNull(config.getString("ServerUptime.numbers.Days")).replace("&","§");
    private final String colorHours =  Objects.requireNonNull(config.getString("ServerUptime.numbers.Hours")).replace("&","§");
    private final String colorMinutes = Objects.requireNonNull(config.getString("ServerUptime.numbers.Minutes")).replace("&","§");
    private final String colorSeconds = Objects.requireNonNull(config.getString("ServerUptime.numbers.Seconds")).replace("&","§");

    @Override
    public void onEnable() {
        serverStart = System.currentTimeMillis();
        saveConfig();
        Objects.requireNonNull(getCommand("uptime")).setExecutor(this);

        getServer().getConsoleSender().sendMessage(Objects.requireNonNull(config.getString("ServerUptime.ServerUptime")).replace("&","§") + ChatColor.DARK_AQUA + " Started");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(Objects.requireNonNull(config.getString("ServerUptime.ServerUptime")).replace("&","§") + ChatColor.RED + " Stopped");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

            if (config.getBoolean("ServerUptime.Permissions") && !sender.hasPermission("serveruptime.use")) {
                sender.sendMessage(Objects.requireNonNull(config.getString("ServerUptime.NoPermissions")));
                return true;
            }

            long diff = System.currentTimeMillis() - serverStart;
            String uptimeName = Objects.requireNonNull(config.getString("ServerUptime.ServerUptime")).replace("&","§");
            int days = (int) (diff / 86400000);
            int hours = (int) (diff / 3600000 % 24);
            int minutes = (int) (diff / 60000 % 60);
            int seconds = (int) (diff / 1000 % 60);


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
