package me.Dunios.Lookup.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SpigotUpdater
  extends Thread
{
  private final Plugin plugin;
  private final int id;
  private final boolean log;
  private boolean enabled = true;
  private URL url;
  
  public SpigotUpdater(Plugin plugin, int resourceID)
    throws IOException
  {
    this(plugin, resourceID, true);
  }
  
  public SpigotUpdater(Plugin plugin, int resourceID, boolean log)
    throws IOException
  {
    if (plugin == null) {
      throw new IllegalArgumentException("Plugin cannot be null");
    }
    if (resourceID == 0) {
      throw new IllegalArgumentException("Resource ID cannot be null (0)");
    }
    this.plugin = plugin;
    this.id = resourceID;
    this.log = log;
    this.url = new URL("https://api.inventivetalent.org/spigot/resource-simple/" + resourceID);
    
    File configDir = new File(plugin.getDataFolder().getParentFile(), "SpigotUpdates");
    File config = new File(configDir, "config.yml");
    YamlConfiguration yamlConfig = new YamlConfiguration();
    if (!configDir.exists()) {
      configDir.mkdirs();
    }
    if (!config.exists())
    {
      config.createNewFile();
      yamlConfig.options().header("Configuration for the SpigotUpdate system\nit will inform you about new versions of all plugins which use this updater\n'enabled' specifies whether the system is enabled (affects all plugins)");
      
      yamlConfig.options().copyDefaults(true);
      yamlConfig.addDefault("enabled", Boolean.valueOf(true));
      yamlConfig.save(config);
    }
    try
    {
      yamlConfig.load(config);
    }
    catch (InvalidConfigurationException e)
    {
      e.printStackTrace();
      return;
    }
    this.enabled = yamlConfig.getBoolean("enabled");
    super.start();
  }
  
  public synchronized void start() {}
  
  public void run()
  {
    if (!this.plugin.isEnabled()) {
      return;
    }
    if (!this.enabled) {
      return;
    }
    HttpURLConnection connection = null;
    try
    {
      connection = (HttpURLConnection)this.url.openConnection();
      connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
      connection.setRequestMethod("GET");
      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      
      String content = "";
      String line = null;
      while ((line = in.readLine()) != null) {
        content = content + line;
      }
      in.close();
      
      JSONObject json = null;
      try
      {
        json = (JSONObject)new JSONParser().parse(content);
      }
      catch (ParseException localParseException) {}
      String currentVersion = null;
      if ((json != null) && (json.containsKey("version")))
      {
        String version = (String)json.get("version");
        if ((version != null) && (!version.isEmpty())) {
          currentVersion = version;
        }
      }
      if (currentVersion == null)
      {
        if (this.log)
        {
          this.plugin.getLogger().warning("[Updater] Invalid response received.");
          this.plugin.getLogger().warning("[updater] Either the author of this plugin has configured the updater wrong, or the API is experiencing some issues.");
        }
        return;
      }
      if (!currentVersion.equals(this.plugin.getDescription().getVersion()))
      {
        Bukkit.broadcastMessage("�8[�cLookup�8]�7 Found new version�8: �a" + currentVersion + "�7! (Your version is �c" + this.plugin.getDescription().getVersion() + "�7)");
        Bukkit.broadcastMessage("�8[�cLookup�8]�7 Download here�8: �6http://www.spigotmc.org/resources/" + this.id);
      }
      else if (this.log)
      {
        Bukkit.getConsoleSender().sendMessage("�8[�cLookup�8]�7 Plugin is up-to-date�8(�a" + this.plugin.getDescription().getVersion() + "�8)");
      }
    }
    catch (IOException e)
    {
      if (this.log)
      {
        if (connection != null) {
          try
          {
            int code = connection.getResponseCode();
            this.plugin.getLogger().warning("[Updater] API connection returned response code " + code);
          }
          catch (IOException localIOException1) {}
        }
        e.printStackTrace();
      }
    }
  }
}
