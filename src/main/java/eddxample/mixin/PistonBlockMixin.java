package eddxample.mixin;

import eddxample.TickPhase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.server.world.BlockAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PistonBlock.class)
public class PistonBlockMixin extends FacingBlock {
    protected PistonBlockMixin(Block.Settings settings) {
        super(settings);
    }

    @Inject(method = "tryMove", locals = LocalCapture.CAPTURE_FAILSOFT, at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/world/World;addBlockAction(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;II)V"))
    private void scheduleExtension(World world, BlockPos pos, BlockState state, CallbackInfo ci, Direction direction) {
        BlockAction tempNormal = new BlockAction(pos, this, 0, direction.getId());
        if (!((IServerWorldMixin)world).getPendingBlockActions().contains(tempNormal)) {
            TickPhase.sendMsg(world.getServer(), "§dEXTEND§r scheduled", pos, "blockEvents");
        }
    }

    @Inject(method = "tryMove", locals = LocalCapture.CAPTURE_FAILSOFT, at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/world/World;addBlockAction(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;II)V"))
    private void scheduleRetraction(World world, BlockPos pos, BlockState state, CallbackInfo ci, Direction direction, int i) {
        BlockAction temp = new BlockAction(pos, this, i, direction.getId());

        if (!((IServerWorldMixin)world).getPendingBlockActions().contains(temp)) {
            TickPhase.sendMsg(world.getServer(), String.format("§d%s§r scheduled", TickPhase.blockEventTypes[i]), pos, "blockEvents");
        }
    }

    @Inject(method = "onBlockAction", at = @At("HEAD"))
    private void runBlockEvent(BlockState state, World world, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> cir) {
        TickPhase.sendMsg(world.getServer(), String.format("§d%s§r executed§r", TickPhase.blockEventTypes[type]), pos, "blockEvents");
    }

    @Inject(method = "onBlockAction", at = @At("RETURN"))
    private void failedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) TickPhase.sendMsg(world.getServer(), String.format("§c%s§r failed§r", TickPhase.blockEventTypes[type]), pos, "blockEvents");
    }
}
