package eddxample;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.arguments.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.server.command.CommandManager.literal;

public class RenderBlockCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("temp")
                .then(CommandManager.argument("block", BlockPosArgumentType.blockPos())
                        .executes(context -> f0(context.getSource(), BlockPosArgumentType.getBlockPos(context, "block")))));

        LiteralArgumentBuilder<ServerCommandSource> command = literal("log")
            .executes(context -> {
                TickPhase.loggers.forEach((k, v) -> context.getSource().sendFeedback(new LiteralText("§7" + k + " = " + v), false));
                return 1;
            });

        TickPhase.loggers.forEach((key, value) -> {
            command.then(literal(key).executes(context -> {
                context.getSource().sendFeedback(new LiteralText("§7" + key + " = " + TickPhase.loggers.get(key)), false);
                return 1;
            }).then(CommandManager.argument("value", BoolArgumentType.bool())
                .executes(context -> {
                    boolean newVal = BoolArgumentType.getBool(context, "value");
                    TickPhase.loggers.put(key, newVal);
                    context.getSource().sendFeedback(new LiteralText("§7" + key + " = " + newVal), false);
                    return 1;
                })));
        });

        dispatcher.register(command);
    }

    /* Functions */

    private static int f0(ServerCommandSource src, BlockPos pos) {
        BlockRenderer.target = pos;
        BlockRenderer.timestamp = src.getMinecraftServer().getTicks();
        return 1;
    }

    private static int f1(ServerCommandSource src) {
        return 1;
    }
}
