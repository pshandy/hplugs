package com.maxharmony.hplug;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

public class Hplug extends JavaPlugin{

    public static HashMap<UUID, UUID> msg = new HashMap<UUID, UUID>();

    public static boolean contains(String str) {
        for (ChatColor c : ChatColor.values())
            if (c.name().equals(str))
                return true;
        return false;
    }
    public void checkCfg() {

        int toUpdate = 0;

        if(getConfig().get("Radius") == null || !contains(getConfig().getString("Radius"))) {
            getConfig().set("Radius", "10");
            toUpdate = 1;
        }

        if(getConfig().get("G Parentheses color") == null
                || !contains(getConfig().getString("G Parentheses color"))) {
            getConfig().set("G Parentheses color", "GRAY");
            toUpdate = 1;
        }

        if(getConfig().get("G Prefix color") == null
                || !contains(getConfig().getString("G Prefix color"))) {
            getConfig().set("G Prefix color", "YELLOW");
            toUpdate = 1;
        }

        if(getConfig().get("G Name color") == null
                || !contains(getConfig().getString("G Name color"))) {
            getConfig().set("G Name color", "WHITE");
            toUpdate = 1;
        }

        if(getConfig().get("G Message color") == null
                || !contains(getConfig().getString("G Message color"))) {
            getConfig().set("G Message color", "GRAY");
            toUpdate = 1;
        }

        if(getConfig().get("L Name color") == null
                || !contains(getConfig().getString("L Name color"))) {
            getConfig().set("L Name color", "WHITE");
            toUpdate = 1;
        }

        if(getConfig().get("L Message color") == null
                || !contains(getConfig().getString("L Message color"))) {
            getConfig().set("L Message color", "WHITE");
            toUpdate = 1;
        }

        if (toUpdate == 1) {
            saveConfig();
            reloadConfig();
        }

    }

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(new MyListener(this), this);

        saveDefaultConfig();
        checkCfg();

    }

    private void sendAll(String str) {
        for (Player p : getServer().getOnlinePlayers())
            p.sendMessage(ChatColor.WHITE + str);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player))
            sender.sendMessage(ChatColor.RED + "Только игроки могут использовать эту команду!");
        if (args.length == 0)
            return (false);

        Player p = (Player) sender;

        StringBuilder str = new StringBuilder();
        for(int i = 0; i < args.length; i++)
            str.append(args[i]).append(" ");
        String strSum = str.toString();

        if (label.equalsIgnoreCase("me")) {
            sendAll("*" + sender.getName() + " " + strSum);

        } else if (label.equalsIgnoreCase("do")) {
            sendAll("*" + strSum + sender.getName());

        } else if (label.equalsIgnoreCase("try")) {
            Random rand = new Random();
            int x = rand.nextInt(2) + 1;
            sendAll("*" + sender.getName() + " " + strSum + (x == 1? "| Удачно" : "| Неудачно"));

        } else if (label.equalsIgnoreCase("r")) {

        }
            if (args.length < 1)
                return (false);

            UUID targetUUID = msg.get(((Player)sender).getUniqueId());

            if (targetUUID != null) {
                if (Bukkit.getPlayer(targetUUID) == null) {
                    msg.remove(((Player)sender).getUniqueId());
                } else {
                    Player target = Bukkit.getPlayer(targetUUID);

                    StringBuilder message = new StringBuilder();
                    for(int i = 0; i < args.length; i++)
                        message.append(args[i]).append(" ");

                    ((Player)sender).sendMessage(ChatColor.GRAY + "[To " + ChatColor.YELLOW
                            + target.getName() + ChatColor.GRAY + "] " + ChatColor.YELLOW + message.toString());
                    Bukkit.getPlayer(targetUUID).sendMessage(ChatColor.GRAY + "[From " + ChatColor.YELLOW
                            + ((Player)sender).getName() + ChatColor.GRAY + "] " + ChatColor.YELLOW + message.toString());
                }

        } else if (label.equalsIgnoreCase("m")) {

            if (args.length < 2)
                return (false);

            Player target = Bukkit.getPlayerExact(args[0]);
            if(target == null) {
                sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD +
                        "The player " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "не в сети");
                return true;
            }

            StringBuilder message = new StringBuilder();
            for(int i = 1; i < args.length; i++)
                message.append(args[i]).append(" ");

            sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW
                    + target.getName() + ChatColor.GRAY + "] " + ChatColor.YELLOW + message.toString());
            target.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW
                    + sender.getName() + ChatColor.GRAY + "] " + ChatColor.YELLOW + message.toString());

            msg.put(((Player)sender).getUniqueId(), target.getUniqueId());
            msg.put(target.getUniqueId(), ((Player)sender).getUniqueId());

        }

        return (true);
    }
}