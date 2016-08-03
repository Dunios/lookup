package me.Dunios.Lookup.ChatLog;

import java.util.ArrayList;
import me.Dunios.Lookup.ChatLog.message.Message;
import me.Dunios.Lookup.SPlayer.SPlayer;

public class ChatLog
{
  private SPlayer sp;
  private ArrayList<Message> messages;
  private ArrayList<Message> cachedmessages;
  private int messagecount = 0;
  
  public ChatLog(SPlayer sp)
  {
    this.sp = sp;
    this.cachedmessages = new ArrayList<>();
    this.messages = new ArrayList<>();
  }
  
  public SPlayer getSp()
  {
    return this.sp;
  }
  
  public void setSp(SPlayer sp)
  {
    this.sp = sp;
  }
  
  public ArrayList<Message> getMessages()
  {
    return this.messages;
  }
  
  public void setMessages(ArrayList<Message> messages)
  {
    this.messages = messages;
  }
  
  public int getMessagecount()
  {
    return this.messagecount;
  }
  
  public void setMessagecount(int messagecount)
  {
    this.messagecount = messagecount;
  }
  
  public ArrayList<Message> getCachedmessages()
  {
    return this.cachedmessages;
  }
  
  public void setCachedmessages(ArrayList<Message> cachedmessages)
  {
    this.cachedmessages = cachedmessages;
  }
  
  public void addMessage(Message m)
  {
    getCachedmessages().add(m);
  }
  
  public void addMessage(Message m, boolean saved)
  {
    getMessages().add(m);
  }
  
  public ArrayList<Message> getAllMessages()
  {
    ArrayList<Message> list = new ArrayList<>();
    for (Message m : getMessages()) {
      list.add(m);
    }
    for (Message m : getCachedmessages()) {
      list.add(m);
    }
    return list;
  }
}
