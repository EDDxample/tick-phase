package eddxample.mixin;

import eddxample.TickPhase;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MinecraftServer.class)
public class ServerTickMixin {

    @Shadow private int ticks;

    @Inject(method = "tick", at = @At("RETURN"))
    private void tickEnd(CallbackInfo ci) {
        TickPhase.endTick((MinecraftServer)(Object)this);
    }

    @Inject(method = "tickWorlds", at = @At(value = "INVOKE_STRING", args = "ldc=players", target = "Lnet/minecraft/util/profiler/DisableableProfiler;swap(Ljava/lang/String;)V"))
    private void logPlayerPhase(CallbackInfo ci) {
        TickPhase.setPhase("PlayerActions");
    }
}
