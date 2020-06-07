package eddxample;

import net.minecraft.server.MinecraftServer;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;

import java.util.concurrent.ConcurrentHashMap;

public class TickPhase {
    private static int lastTick = 0;
    private static String tickColor = "§6", currentPhase = "idk";
    private static boolean updateFlag, logged;

    public static ConcurrentHashMap<String, Boolean> loggers = new ConcurrentHashMap<>(5);

    static {
//        loggers.put("playerActions", false);
        loggers.put("tileTicks", false);
        loggers.put("blockEvents", false);
//        loggers.put("entities", false);
//        loggers.put("blockEntities", false);
        loggers.put("inputs", false);
    }

    public static String[] blockEventTypes = {"EXTEND ", "RETRACT", "DROP "};


    private static void updateTimestamp(int ticks) {
        if (updateFlag) {
            lastTick = ticks;
            tickColor = tickColor.equals("§6") ? "§e" : "§6";
            updateFlag = false;
        }

        // Block renderer
        if (ticks - BlockRenderer.timestamp > 60) BlockRenderer.target = null;
    }

    public static void sendMsg(MinecraftServer server, String s, BlockPos pos, String type) {
        if (server != null) {

            if (!loggers.get(type)) return;

            if (server.getTicks() != lastTick) {
                updateFlag = true;
            }
            int ticks = server.getTicks() + (currentPhase.equals("PlayerActions") ? 1 : 0);
            LiteralText text = new LiteralText(String.format("[%s%05d§r] [§7§l%s§r] %s at §3[%d, %d, %d]§r", tickColor, ticks, currentPhase, s, pos.getX(), pos.getY(), pos.getZ()));
            text.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/temp %d %d %d", pos.getX(), pos.getY(), pos.getZ())));

            server.getPlayerManager().sendToAll(text);
            logged = true;
        }
    }

    public static void setPhase(String s) {
        currentPhase = s;
    }

    public static void endTick(MinecraftServer server) {
        updateTimestamp(server.getTicks());
        if (logged) {
            logged = false;
        }
    }
}
