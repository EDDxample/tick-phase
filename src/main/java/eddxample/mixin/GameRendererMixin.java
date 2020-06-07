package eddxample.mixin;

import eddxample.BlockRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"))
    private void renderWorldStart(float delta, long time, MatrixStack matrixStack, CallbackInfo ci) {
        BlockRenderer.onRender(matrixStack);
        BlockRenderer.matrix = matrixStack;
    }

    @Inject(method = "renderWorld", at = @At("TAIL"))
    private void renderWorldEnd(float delta, long time, MatrixStack matrixStack, CallbackInfo ci) {
        BlockRenderer.matrix = null;
    }
}