package eddxample.mixin;

import net.minecraft.util.profiler.DisableableProfiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisableableProfiler.class)
public abstract class DisableProfilerMixin {



    @Inject(method = "swap(Ljava/lang/String;)V", at = @At("HEAD"))
    private void swap(String type, CallbackInfo ci) {
//        if (type.equals("hand")) BlockRenderer.render(); // loop
    }

}