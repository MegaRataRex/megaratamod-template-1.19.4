package com.megarata.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ChestBlock.class)
public class ChestBlockMixin {

    @Inject(method = "onPlaced", at = @At("HEAD"))
    public void removeTagOnPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        if (placer instanceof ServerPlayerEntity serverPlayer && !world.isClient) {

            BlockEntity blockEntity = world.getBlockEntity(pos);

            //Añade el tag "used" desde el momento en el que se pone un cofre, esto para no añadirlo desde un principio a cofres ya generados.
            if (blockEntity instanceof ChestBlockEntity chest) {
                NbtCompound nbt = chest.createNbt();
                nbt.putBoolean("used",!serverPlayer.isCreative());
                chest.readNbt(nbt);
            }
        }
    }
}
