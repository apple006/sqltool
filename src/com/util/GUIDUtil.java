package com.util;
import java.util.UUID;

public class GUIDUtil {
	public static String getKey()
	  {
	    String sResult = "";
	    String sKey = UUID.randomUUID().toString();
	    for (int i = 0; i < sKey.length(); i++) {
	      char c = sKey.charAt(i);
	      if ((c == ':') || (c == '-')) {
	        continue;
	      }
	      sResult = sResult + c;
	    }
	    return sResult;
	  }

}
