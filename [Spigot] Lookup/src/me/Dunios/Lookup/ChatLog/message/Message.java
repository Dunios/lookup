package me.Dunios.Lookup.ChatLog.message;

import me.Dunios.Lookup.SPlayer.SPlayer;

public class Message
{
  private long time;
  private SPlayer sp;
  private String message;
  
  public Message(long time, SPlayer sp, String message)
  {
    this.time = time;
    this.sp = sp;
    this.message = message;
  }
  
  public long getTime()
  {
    return this.time;
  }
  
  public void setTime(long time)
  {
    this.time = time;
  }
  
  public SPlayer getSp()
  {
    return this.sp;
  }
  
  public void setSp(SPlayer sp)
  {
    this.sp = sp;
  }
  
  public String getMessage()
  {
    return this.message;
  }
  
  public void setMessage(String message)
  {
    this.message = message;
  }
  
  public void save() {}
}
