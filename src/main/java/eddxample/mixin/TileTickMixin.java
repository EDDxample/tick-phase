package eddxample.mixin;

import eddxample.TickPhase;
import net.minecraft.server.world.ServerTickScheduler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.TickPriority;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ServerTickScheduler.class)
public abstract class TileTickMixin {

    @Shadow @Final private ServerWorld world;
    @Shadow public abstract boolean isScheduled(BlockPos pos, Object tt);

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    private void executeTileTick(Consumer consumer, Object tt) {
        if (tt != null) TickPhase.sendMsg(world.getServer(), String.format("tiletick %d executed", ((ScheduledTick)tt).priority.getIndex()), ((ScheduledTick)tt).pos, "tileTicks");
        consumer.accept(tt);
    }

    @Inject(method = "schedule", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerTickScheduler;addScheduledTick(Lnet/minecraft/world/ScheduledTick;)V"))
    private void schedule(BlockPos pos, Object tt, int delay, TickPriority priority, CallbackInfo ci) {
        if (!this.isScheduled(pos, tt))TickPhase.sendMsg(world.getServer(), String.format("tiletick %d %d scheduled", priority.getIndex(), delay), pos, "tileTicks");
    }

}
