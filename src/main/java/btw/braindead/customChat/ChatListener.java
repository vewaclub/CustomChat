package btw.braindead.customChat;

import btw.braindead.customChat.util.PlayTimeUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    private static final int LOCAL_RADIUS = 50;
    private static final double LOCAL_RADIUS_SQUARED = (double) LOCAL_RADIUS * LOCAL_RADIUS;

    @EventHandler
    private void onPlayerChat(AsyncChatEvent e) {
        Player source = e.getPlayer();
        Component originalMessage = e.message();

        String plain = PlainTextComponentSerializer.plainText().serialize(originalMessage);
        boolean isGlobal = plain.startsWith("!");

        Component displayMessage = isGlobal
                ? stripPrefix(originalMessage)
                : originalMessage;

        if (!isGlobal) {
            e.viewers().removeIf(viewer -> {
                if (viewer.equals(source)) return false;
                if (!(viewer instanceof Player p)) return true;
                if (!p.getWorld().equals(source.getWorld())) return true;
                return p.getLocation().distanceSquared(source.getLocation()) > LOCAL_RADIUS_SQUARED;
            });
        }

        final boolean global = isGlobal;
        final Component msg = displayMessage;
        e.renderer((src, sourceDisplayName, ignored, viewer) -> render(src, sourceDisplayName, msg, viewer, global));
    }

    private Component stripPrefix(Component message) {
        if (message instanceof TextComponent text && text.content().startsWith("!")) {
            return text.content(text.content().substring(1));
        }
        return message;
    }

    private Component render(Player source, Component sourceDisplayName, Component message, Audience viewer, boolean isGlobal) {

        if (!(viewer instanceof Player viewerPlayer)) {
            return Component.empty();
        }

        boolean russian = viewerPlayer.locale().getLanguage().equals("ru");
        String playTime = PlayTimeUtil.getPlayTime(source, russian);

        Component channel = (isGlobal
                ? Component.text("G | ", NamedTextColor.GOLD)
                : Component.text("L | ", NamedTextColor.DARK_GRAY)
        ).hoverEvent(HoverEvent.showText(
                russian
                        ? (isGlobal
                           ? Component.text("G Глобальный чат\n", NamedTextColor.GOLD)
                        .append(Component.text("Виден всем на сервере. Префикс: !", NamedTextColor.GRAY))
                           : Component.text("L Локальный чат\n", NamedTextColor.DARK_GRAY)
                        .append(Component.text("Виден в радиусе 50 блоков", NamedTextColor.GRAY)))
                        : (isGlobal
                           ? Component.text("G Global chat\n", NamedTextColor.GOLD)
                        .append(Component.text("Visible to everyone. Prefix: !", NamedTextColor.GRAY))
                           : Component.text("L Local chat\n", NamedTextColor.DARK_GRAY)
                        .append(Component.text("Visible within 50 blocks", NamedTextColor.GRAY)))
        ));

        Component name = sourceDisplayName
                .color(NamedTextColor.GRAY)
                .hoverEvent(HoverEvent.showText(
                        russian
                                ? Component.text()
                                .append(sourceDisplayName)
                                .append(Component.text("\nВремя в игре: ", NamedTextColor.GRAY))
                                .append(Component.text(playTime + "\n\n"))
                                .append(Component.text("Нажмите, чтобы отправить сообщение", NamedTextColor.YELLOW))
                                .build()
                                : Component.text()
                                .append(sourceDisplayName)
                                .append(Component.text("\nTime played: ", NamedTextColor.GRAY))
                                .append(Component.text(playTime + "\n\n"))
                                .append(Component.text("Click to send a message", NamedTextColor.YELLOW))
                                .build()
                ))
                .clickEvent(ClickEvent.suggestCommand("/msg " + source.getName() + " "));

        return Component.text()
                .append(channel)
                .append(name)
                .append(Component.text(": "))
                .append(message)
                .build();
    }
}