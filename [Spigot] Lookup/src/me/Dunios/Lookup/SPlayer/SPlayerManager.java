package me.Dunios.Lookup.SPlayer;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.Dunios.Lookup.Main;
import me.Dunios.Lookup.ChatLog.message.MessageManager;

public class SPlayerManager
{
  public static ArrayList<SPlayer> players = new ArrayList<>();
  
  public static void init()
  {
    players.clear();
    File folder = new File(Main.getInstance().getDataFolder() + "/players");
    if (!folder.exists()) {
      folder.mkdirs();
    }
    File[] arrayOfFile;
    int j = (arrayOfFile = folder.listFiles()).length;
    for (int i = 0; i < j; i++)
    {
      File f = arrayOfFile[i];
      
      FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
      SPlayer sp = new SPlayer();
      sp.loadFromConfig(cfg);
    }
    for (Player p : Bukkit.getOnlinePlayers())
    {
      if (!userExist(p.getUniqueId())) {
        createPlayer(p);
      }
      if (!hasChatLog(getSPlayerByUUID(p.getUniqueId()))) {
        createChatLog(getSPlayerByUUID(p.getUniqueId()));
      }
    }
    Bukkit.getConsoleSender().sendMessage("§8[§cLookup§8] §7Loaded §e" + players.size() + "§7 users");
  }
  
  public static ArrayList<SPlayer> getSPlayersByIP(String ip)
  {
    ArrayList<SPlayer> list = new ArrayList<>();
    for (SPlayer p : players) {
      if (p.getIp().equalsIgnoreCase(ip)) {
        list.add(p);
      }
    }
    return list;
  }
  
  public static SPlayer getSPlayerByName(String username)
  {
    for (SPlayer sp : players) {
      if (sp.getName().equalsIgnoreCase(username)) {
        return sp;
      }
    }
    return null;
  }
  
  public static SPlayer getSPlayerByUUID(UUID uuid)
  {
    for (SPlayer sp : players) {
      if (sp.getUuid().equals(uuid)) {
        return sp;
      }
    }
    return null;
  }
  
  public static boolean hasChatLog(SPlayer sp)
  {
    if (sp.getChatLog() != null) {
      return true;
    }
    return false;
  }
  
  public static void createChatLog(SPlayer sp)
  {
    File f = new File(Main.getInstance().getDataFolder() + "/chatlog", sp.getUuid().toString() + ".yml");
    if (!f.exists()) {
      try
      {
        f.createNewFile();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
    cfg.set("messages", Integer.valueOf(0));
    try
    {
      cfg.save(f);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    MessageManager.loadMessages(sp);
  }
  
  public static boolean userExist(UUID uuid)
  {
    for (SPlayer sp : players) {
      if (sp.getUuid().equals(uuid)) {
        return true;
      }
    }
    return false;
  }
  
  public static void createPlayer(Player player)
  {
    SPlayer p = new SPlayer();
    p.setName(player.getName());
    p.setUuid(player.getUniqueId());
    p.setFirstLogin(getCurrentDate());
    p.setLastLogin(getCurrentDate());
    p.setIp(player.getAddress().getAddress().getHostAddress());
    p.setOnline(true);
    p.setJoinCount(1);
    p.save();
  }
  
  public static String getCurrentDate()
  {
    Date d = new Date();
    DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    
    return df.format(d);
  }
}
