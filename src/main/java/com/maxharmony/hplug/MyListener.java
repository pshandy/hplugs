package com.maxharmony.hplug;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Set;
public class MyListener implements Listener {

    private Hplug main;
    public MyListener(Hplug plug) {
        main = plug;
    }

    @EventHandler
    public void joinFormat(PlayerJoinEvent event) {
        event.setJoinMessage(ChatColor.GRAY + event.getPlayer().getDisplayName()
                + ChatColor.WHITE + " присоединился к игре.");
    }

    @EventHandler
    public void joinFormat(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.GRAY + event.getPlayer().getDisplayName()
                + ChatColor.WHITE + " покинул игру.");
    }

    @EventHandler
    public void chatFormat(AsyncPlayerChatEvent event) {

        event.setCancelled(true);

        String message = event.getMessage();
        Player currentPlayer = event.getPlayer();
        Location currentLocation = currentPlayer.getLocation();
        Set<Player> players = event.getRecipients();

        if (event.getMessage().charAt(0) == '!') {
            for (Player p: players)
                p.sendMessage(ChatColor.valueOf(main.getConfig().getString("G Parentheses color")) + "["
                        + ChatColor.valueOf(main.getConfig().getString("G Prefix color")) + "G"
                        + ChatColor.valueOf(main.getConfig().getString("G Parentheses color")) + "] "
                        + ChatColor.valueOf(main.getConfig().getString("G Name color")) + currentPlayer.getDisplayName() + ": "
                        + ChatColor.valueOf(main.getConfig().getString("G Message color")) + message.substring(1));
        } else
            for (Player p: players)
                if (currentLocation.distance(p.getLocation()) <= main.getConfig().getInt("Radius"))
                    p.sendMessage(ChatColor.valueOf(main.getConfig().getString("L Name color")) + currentPlayer.getDisplayName()
                            + ChatColor.valueOf(main.getConfig().getString("L Message color")) + ": " + message);
    }
}
