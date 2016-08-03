package me.Dunios.Lookup.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Dunios.Lookup.SPlayer.SPlayer;
import me.Dunios.Lookup.SPlayer.SPlayerManager;

public class PlayerLogoutListener
  implements Listener
{
  @EventHandler
  public void onLogout(PlayerQuitEvent e)
  {
    SPlayer sp = SPlayerManager.getSPlayerByName(e.getPlayer().getName());
    sp.setOnline(false);
    sp.save();
  }
}
