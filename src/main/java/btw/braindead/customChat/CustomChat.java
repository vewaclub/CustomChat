package btw.braindead.customChat;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomChat extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
