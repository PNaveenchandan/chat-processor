package com.smarttech.agribot.util;

public class ResponseUtil {
  public static boolean isValidReply(String reply){
    return reply != null && !reply.isEmpty() && !reply.contains("ERR");
  }
}
