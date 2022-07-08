package io.github.drhampust.mysql_sync.mixin;

import io.github.drhampust.mysql_sync.Main;
import net.minecraft.server.dedicated.EulaReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EulaReader.class)
public class EULAMixin {
    /*
    // This works
    @Inject(at = @At("HEAD"), method = "isEulaAgreedTo()Z", cancellable = true)
    private void isEulaAgreedTo0(CallbackInfoReturnable<Boolean> ci) {
        Main.LOGGER.info("isEulaAgreedTo mixin");
        ci.setReturnValue(false);
        ci.cancel();
    }
     */
}
