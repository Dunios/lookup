package me.Dunios.Lookup.ChatLog.message;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Dunios.Lookup.Main;
import me.Dunios.Lookup.ChatLog.ChatLog;
import me.Dunios.Lookup.SPlayer.SPlayer;
import me.Dunios.Lookup.SPlayer.SPlayerManager;

public class MessageManager
{
  public static void saveMessages()
  {
    int messagecounter = 0;
    for (SPlayer sp : SPlayerManager.players) {
      if (sp.getChatLog().getCachedmessages().size() > 0)
      {
        File f = new File(Main.getInstance().getDataFolder() + "/chatlog", sp.getUuid().toString() + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        int messages = sp.getChatLog().getMessagecount();
        for (Message m : sp.getChatLog().getCachedmessages())
        {
          cfg.set(messages + ".time", Long.valueOf(m.getTime()));
          cfg.set(messages + ".message", m.getMessage());
          messages++;
          messagecounter++;
        }
        sp.getChatLog().getCachedmessages().clear();
        cfg.set("messages", Integer.valueOf(messages));
        try
        {
          cfg.save(f);
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
    Bukkit.getConsoleSender().sendMessage("§8[§cChatLog§8] §7Saved §e" + messagecounter + "§7 messages for this session");
  }
  
  public static void loadMessages()
  {
    File folder = new File(Main.getInstance().getDataFolder() + "/chatlog");
    if (!folder.exists()) {
      folder.mkdirs();
    }
    File[] arrayOfFile;
    int j = (arrayOfFile = folder.listFiles()).length;
    for (int i = 0; i < j; i++)
    {
      File f = arrayOfFile[i];
      
      FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
      
      SPlayer sp = SPlayerManager.getSPlayerByUUID(UUID.fromString(f.getName().replaceAll(".yml", "")));
      ChatLog c = new ChatLog(sp);
      c.setMessagecount(cfg.getInt("messages"));
      for (String path : cfg.getKeys(false)) {
        if (!path.equalsIgnoreCase("messages"))
        {
          Message s = new Message(cfg.getLong(path + ".time"), sp, cfg.getString(path + ".message"));
          c.addMessage(s, true);
        }
      }
      sp.setChatLog(c);
    }
  }
  
  public static void loadMessages(SPlayer sp)
  {
    File folder = new File(Main.getInstance().getDataFolder() + "/chatlog");
    if (!folder.exists()) {
      folder.mkdirs();
    }
    File f = new File(Main.getInstance().getDataFolder() + "/chatlog", sp.getUuid().toString() + ".yml");
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
    
    ChatLog c = new ChatLog(sp);
    c.setMessagecount(cfg.getInt("messages"));
    for (String path : cfg.getKeys(false)) {
      if (!path.equalsIgnoreCase("messages"))
      {
        Message s = new Message(cfg.getLong(path + ".time"), sp, cfg.getString(path + ".message"));
        c.addMessage(s, true);
      }
    }
    sp.setChatLog(c);
  }
}
