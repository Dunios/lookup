package me.Dunios.Lookup;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.Dunios.Lookup.ChatLog.message.MessageManager;
import me.Dunios.Lookup.SPlayer.SPlayerManager;
import me.Dunios.Lookup.command.ChatLogCommand;
import me.Dunios.Lookup.command.LookupCommand;
import me.Dunios.Lookup.events.PlayerChatListener;
import me.Dunios.Lookup.events.PlayerLoginListener;
import me.Dunios.Lookup.events.PlayerLogoutListener;
import me.Dunios.Lookup.utils.SpigotUpdater;

public class Main
  extends JavaPlugin
{
  private static Main plugin;
  
  public void onEnable()
  {
    plugin = this;
    commands();
    events();
    utils();
  }
  
  public void onDisable() {}
  
  public static Main getInstance()
  {
    return plugin;
  }
  
  private void commands()
  {
    getCommand("lookup").setExecutor(new LookupCommand());
    getCommand("chatlog").setExecutor(new ChatLogCommand());
  }
  
  private void events()
  {
    PluginManager pm = Bukkit.getPluginManager();
    pm.registerEvents(new PlayerLoginListener(), this);
    pm.registerEvents(new PlayerLogoutListener(), this);
    pm.registerEvents(new PlayerChatListener(), this);
  }
  
  private void utils()
  {
    try
    {
    	@SuppressWarnings("unused")
    	SpigotUpdater localSpigotUpdater = new SpigotUpdater(getInstance(), 27238);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    SPlayerManager.init();
    MessageManager.loadMessages();
  }
}
