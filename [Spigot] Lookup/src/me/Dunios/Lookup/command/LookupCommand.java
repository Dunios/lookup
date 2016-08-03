package me.Dunios.Lookup.command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Dunios.Lookup.Main;
import me.Dunios.Lookup.SPlayer.SPlayer;
import me.Dunios.Lookup.SPlayer.SPlayerManager;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class LookupCommand
  implements CommandExecutor
{
  private String PREFIX = "§cLookup §8» §7";
  
  @SuppressWarnings("deprecation")
public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args)
  {
    if (sender.hasPermission("lookup.use")) {
      if (args.length == 0)
      {
        sender.sendMessage(this.PREFIX + "§cWrong usage: §6/lookup <username>");
      }
      else if (args.length == 1)
      {
        sender.sendMessage(this.PREFIX + "Loading data...");
        
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new BukkitRunnable()
        {
          public void run()
          {
            SPlayer sp = SPlayerManager.getSPlayerByName(args[0]);
            if (sp != null)
            {
              sender.sendMessage(LookupCommand.this.PREFIX + "§8§m-----------------------------------");
              sender.sendMessage(LookupCommand.this.PREFIX + "Name§8: §6" + sp.getName());
              sender.sendMessage(LookupCommand.this.PREFIX + "UUID§8: §6" + sp.getUuid());
              sender.sendMessage(LookupCommand.this.PREFIX + "IP§8: §6" + sp.getIp() + " §7(" + LookupCommand.this.getCountry(sp.getIp()) + ")");
              ArrayList<SPlayer> list = SPlayerManager.getSPlayersByIP(sp.getIp());
              if (list.size() > 1)
              {
                sender.sendMessage(LookupCommand.this.PREFIX + "Owned accounts");
                for (SPlayer s : list) {
                  sender.sendMessage(LookupCommand.this.PREFIX + "  §8-§6 " + s.getName());
                }
              }
              if (Bukkit.getServer().getPluginManager().getPlugin("PermissionsEx") != null)
              {
                PermissionUser pu = PermissionsEx.getUser(sp.getName());
                sender.sendMessage(LookupCommand.this.PREFIX + "Ranks§8(PEX)");
                PermissionGroup[] groups = pu.getGroups();
                for (int i = 0; i < groups.length; i++)
                {
                  PermissionGroup pg = groups[i];
                  sender.sendMessage(LookupCommand.this.PREFIX + "  §8- " + pg.getPrefix().replaceAll("&", "§") + pg.getName());
                }
              }
              sender.sendMessage(LookupCommand.this.PREFIX + "Online§8: §6" + sp.isOnline());
              sender.sendMessage(LookupCommand.this.PREFIX + "First login§8: §6" + sp.getFirstLogin());
              sender.sendMessage(LookupCommand.this.PREFIX + "Last login§8: §6" + sp.getLastLogin());
              sender.sendMessage(LookupCommand.this.PREFIX + "Join count§8: §6" + sp.getJoinCount());
              sender.sendMessage(LookupCommand.this.PREFIX + "Messages sent§8: §6" + (sp.getChatLog().getMessages().size() + sp.getChatLog().getCachedmessages().size()) + " messages");
              Player p = Bukkit.getPlayer(sp.getUuid());
              if (p != null)
              {
                sender.sendMessage(LookupCommand.this.PREFIX + "§8§m------------§a§l  Live data §r§8§m-------------");
                sender.sendMessage(LookupCommand.this.PREFIX + "Gamemode§8: §6" + p.getGameMode().toString().toLowerCase());
                sender.sendMessage(LookupCommand.this.PREFIX + "Location§8: §6(" + p.getLocation().getBlockX() + ", " + p.getLocation().getBlockY() + ", " + p.getLocation().getBlockZ() + ") " + p.getLocation().getWorld().getName());
                sender.sendMessage(LookupCommand.this.PREFIX + "Health§8: §6" + p.getHealth());
                sender.sendMessage(LookupCommand.this.PREFIX + "Hunger§8: §6" + p.getFoodLevel());
                sender.sendMessage(LookupCommand.this.PREFIX + "Ping§8: §6" + ((CraftPlayer)p).getHandle().ping + "ms");
              }
              sender.sendMessage(LookupCommand.this.PREFIX + "§8§m-----------------------------------");
            }
            else
            {
              sender.sendMessage(LookupCommand.this.PREFIX + "§cCouldn't find this player");
              return;
            }
          }
        });
      }
    }
    return false;
  }
  
  public String getCountry(String ip)
  {
    try
    {
      URL url = new URL("http://ipinfo.io/" + ip + "/country");
      BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
      return in.readLine();
    }
    catch (Exception ex) {}
    return "undefined";
  }
}
