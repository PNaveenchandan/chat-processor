package com.smarttech.agribot.cache;

import com.rivescript.Config;
import com.rivescript.RiveScript;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RiveCache {

  public static Map<String, RiveScript> riveCache = new HashMap<>();

  @PostConstruct
  public void loadRiveBots() throws FileNotFoundException, URISyntaxException {
    RiveScript bot = new RiveScript(Config.utf8());
    File resource = ResourceUtils.getFile("/tmp/riveScripts");
    //Path path = Path.of(this.getClass().getResource("/riveScripts").toURI());
    bot.loadDirectory(resource);
    bot.sortReplies();
    riveCache.put("BOT", bot);
  }
}
