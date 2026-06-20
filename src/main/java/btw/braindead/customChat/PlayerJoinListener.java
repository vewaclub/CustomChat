package btw.braindead.customChat;

import btw.braindead.customChat.util.PlayTimeUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    private void playerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        e.joinMessage(null);

        boolean firstJoin = !player.hasPlayedBefore();

        for (Player viewer : Bukkit.getOnlinePlayers()) {
            viewer.sendMessage(joinMessage(viewer, player, firstJoin));
        }
    }

    public Component joinMessage(Player viewer, Player player, boolean firstJoin) {

        boolean russian = viewer.locale().getLanguage().equals("ru");
        String playTime = PlayTimeUtil.getPlayTime(player, russian);

        if (firstJoin) {

            Component name = Component.text(player.getDisplayName(), NamedTextColor.YELLOW)
                    .hoverEvent(HoverEvent.showText(
                            Component.text(
                                    russian
                                            ? "Нажмите, чтобы поприветствовать игрока"
                                            : "Click to welcome the player",
                                    NamedTextColor.GOLD
                            )
                    ))
                    .clickEvent(ClickEvent.suggestCommand(
                            "!" + player.getName() + ", привет!"
                    ));

            return Component.text()
                    .append(name)
                    .append(Component.text(
                            russian
                                    ? " впервые зашёл на сервер"
                                    : " joined the server for the first time",
                            NamedTextColor.YELLOW
                    ))
                    .build();
        }

        Component name = Component.text(player.getDisplayName(), NamedTextColor.YELLOW)
                .hoverEvent(HoverEvent.showText(
                        Component.text()
                                .append(Component.text(player.getDisplayName() + "\n"))
                                .append(Component.text(
                                        russian
                                                ? "Время в игре: "
                                                : "Time played: ",
                                        NamedTextColor.GRAY
                                ))
                                .append(Component.text(playTime + "\n\n"))
                                .append(Component.text(
                                        russian
                                                ? "Нажмите, чтобы отправить сообщение"
                                                : "Click to send a message",
                                        NamedTextColor.YELLOW
                                ))
                                .build()
                ))
                .clickEvent(ClickEvent.suggestCommand("/msg " + player.getName() + " "));

        return Component.text()
                .append(name)
                .append(Component.text(
                        russian
                                ? " присоединился к игре"
                                : " joined the game",
                        NamedTextColor.YELLOW
                ))
                .build();
    }
}