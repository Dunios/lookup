package me.Dunios.Lookup.command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Dunios.Lookup.ChatLog.message.Message;
import me.Dunios.Lookup.SPlayer.SPlayer;
import me.Dunios.Lookup.SPlayer.SPlayerManager;

public class ChatLogCommand
  implements CommandExecutor
{
  private String PREFIX = "§3ChatLog §8» §7";
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if (sender.hasPermission("chatlog.use")) {
      if (args.length == 0)
      {
        sender.sendMessage(this.PREFIX + "§cWrong usage: §6/chatlog <playername> <messagecount>");
      }
      else if (args.length == 2)
      {
        int count = 0;
        try
        {
          count = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException ex)
        {
          sender.sendMessage(this.PREFIX + "§6" + args[1] + "§c is not a valid number");
        }
        SPlayer sp = SPlayerManager.getSPlayerByName(args[0]);
        if (sp == null)
        {
          sender.sendMessage(this.PREFIX + "§cCouldn't find this player");
          return false;
        }
        if (count > 0)
        {
          ArrayList<Message> messages = sp.getChatLog().getAllMessages();
          if (messages.size() < count - 1) {
            count = messages.size();
          }
          sender.sendMessage(this.PREFIX + "Loading §6" + count + "§7 messages");
          for (int i = 0; i < count; i++)
          {
            DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            Date d = new Date(((Message)messages.get(i)).getTime());
            sender.sendMessage("§8[§7" + df.format(d) + "§8] §6" + ((Message)messages.get(i)).getSp().getName() + " §8» §7" + ((Message)messages.get(i)).getMessage());
          }
        }
        else
        {
          sender.sendMessage(this.PREFIX + "§6The number §cmust be greater than 0");
        }
      }
    }
    return false;
  }
}
