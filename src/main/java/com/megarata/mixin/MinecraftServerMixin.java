package com.megarata.mixin;

import com.megarata.server.LootTableCache;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    private MinecraftServer self(){
        return (MinecraftServer)((Object)(this));
    }

    @Inject(method = "loadWorld",at = @At("HEAD"))
    private void loadWorld(CallbackInfo ci){
        LootTableCache.load();
    }


}
