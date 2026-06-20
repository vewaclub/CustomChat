package btw.braindead.customChat.util;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

public final class PlayTimeUtil {

    private PlayTimeUtil() {}

    public static String getPlayTime(Player player, boolean russian) {

        int ticks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);

        long seconds = ticks / 20L;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;

        if (russian) {
            return hours + " ч. " + minutes + " мин.";
        }

        return hours + " h " + minutes + " m";
    }
}