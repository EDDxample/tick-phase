package eddxample;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BlockRenderer {

    static BlockPos target;
    static int timestamp;
    public static MatrixStack matrix;

    public static void render() {

        Vec3d camPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        if (camPos == null || target == null || buffer == null) return;

        GlStateManager.lineWidth(2.0f);
        buffer.begin(3, VertexFormats.POSITION_COLOR);

        double drawX = target.getX() - camPos.x, drawY = target.getY() - camPos.y, drawZ = target.getZ() - camPos.z;

        buffer.vertex(drawX, drawY, drawZ).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        buffer.vertex(drawX+1, drawY, drawZ).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        buffer.vertex(drawX+1, drawY+1, drawZ).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        buffer.vertex(drawX,drawY+1,drawZ).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        buffer.vertex(drawX,drawY,drawZ).color(1.0f, 1.0f, 1.0f, 1.0f).next();

        buffer.vertex(drawX,drawY,drawZ+1).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        buffer.vertex(drawX,drawY+1,drawZ+1).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        buffer.vertex(drawX+1,drawY+1,drawZ+1).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        buffer.vertex(drawX+1,drawY,drawZ+1).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        buffer.vertex(drawX,drawY,drawZ+1).color(1.0f, 1.0f, 1.0f, 1.0f).next();

        buffer.vertex(drawX,drawY+1,drawZ).color(1.0f, 1.0f, 1.0f, 0.0f).next();
        buffer.vertex(drawX,drawY+1,drawZ+1).color(1.0f, 1.0f, 1.0f, 1.0f).next();

        buffer.vertex(drawX+1,drawY+1,drawZ).color(1.0f, 1.0f, 1.0f, 0.0f).next();
        buffer.vertex(drawX+1,drawY+1,drawZ+1).color(1.0f, 1.0f, 1.0f, 1.0f).next();

        buffer.vertex(drawX+1,drawY,drawZ).color(1.0f, 1.0f, 1.0f, 0.0f).next();
        buffer.vertex(drawX+1,drawY,drawZ+1).color(1.0f, 1.0f, 1.0f, 1.0f).next();

        tessellator.draw();

    }

    public static void onRender(MatrixStack matrix) {

        if (target == null || matrix == null) return;

        RenderSystem.pushMatrix();
        RenderSystem.multMatrix(matrix.peek().getModel());

        GlStateManager.disableTexture();
        GlStateManager.enableAlphaTest();
        if(true) { // ---------------------------------------------------
            GlStateManager.disableDepthTest();
        }

        render();

        RenderSystem.popMatrix();
    }
}
