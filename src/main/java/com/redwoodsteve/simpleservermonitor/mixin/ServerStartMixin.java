package com.redwoodsteve.simpleservermonitor.mixin;

import com.redwoodsteve.simpleservermonitor.EventListeners;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.server.MinecraftServer.class)
public class ServerStartMixin {
    @Inject(method = "runServer", at = @At("HEAD"))
    private void onBeginStart(CallbackInfo ci) {
        EventListeners.onServerBeginStart((MinecraftServer) (Object) this);
    }
}
