package me.Dunios.Lookup.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.Dunios.Lookup.ChatLog.message.Message;
import me.Dunios.Lookup.SPlayer.SPlayer;
import me.Dunios.Lookup.SPlayer.SPlayerManager;

public class PlayerChatListener
  implements Listener
{
  @EventHandler
  public void onChat(AsyncPlayerChatEvent e)
  {
    SPlayer sp = SPlayerManager.getSPlayerByUUID(e.getPlayer().getUniqueId());
    sp.getChatLog().addMessage(new Message(System.currentTimeMillis(), sp, e.getMessage()));
  }
}
