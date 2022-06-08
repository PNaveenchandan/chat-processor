package com.smarttech.agribot.cache;

import com.rivescript.Config;
import com.rivescript.RiveScript;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
public class RiveCache {

  public static Map<String, RiveScript> riveCache = new HashMap<>();

  @Value("${spring.profiles.active}")
  private String profile;

  @PostConstruct
  public void loadRiveBots() throws FileNotFoundException, URISyntaxException {
    RiveScript bot = new RiveScript(Config.utf8());
    if(profile.equals("dev")) {
      Path path = Path.of(this.getClass().getResource("/riveScripts").toURI());
      bot.loadDirectory(path.toFile());
    }else {
      File resource = ResourceUtils.getFile("/tmp/riveScripts");
      bot.loadDirectory(resource);
    }
    bot.sortReplies();
    riveCache.put("BOT", bot);
  }
}
