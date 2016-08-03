package me.Dunios.Lookup.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.Dunios.Lookup.SPlayer.SPlayer;
import me.Dunios.Lookup.SPlayer.SPlayerManager;

public class PlayerLoginListener
  implements Listener
{
  @EventHandler
  public void onJoin(PlayerJoinEvent e)
  {
    Bukkit.getConsoleSender().sendMessage("§8[§cLookup§8] §e" + e.getPlayer().getName() + " §7joined §8(§7" + e.getPlayer().getUniqueId() + "§8)");
    if (!SPlayerManager.userExist(e.getPlayer().getUniqueId()))
    {
      SPlayerManager.createPlayer(e.getPlayer());
      Bukkit.getConsoleSender().sendMessage("§8[§cLookup§8] §7Created profile for §e" + e.getPlayer().getName());
    }
    SPlayer sp = SPlayerManager.getSPlayerByUUID(e.getPlayer().getUniqueId());
    sp.setOnline(true);
    sp.setLastLogin(SPlayerManager.getCurrentDate());
    sp.setJoinCount(sp.getJoinCount() + 1);
    if (!e.getPlayer().getName().equalsIgnoreCase(sp.getName()))
    {
      Bukkit.getConsoleSender().sendMessage("§8[§cLookup§8] §e" + sp.getName() + "§7 changed his name to §e" + e.getPlayer().getName());
      sp.setName(e.getPlayer().getName());
    }
    if (!SPlayerManager.hasChatLog(sp)) {
      SPlayerManager.createChatLog(sp);
    }
    sp.save();
  }
}
