package nnhomoli.bettertogether.mixins;

import net.minecraft.core.entity.Entity;
import net.minecraft.server.entity.player.PlayerServer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class,remap=false)
public abstract class entityMixin {
	@Shadow @Nullable public Entity passenger;

	@Inject(method= "ejectRider",at= @At(value = "HEAD"))
	public void ejectRider(CallbackInfoReturnable<Entity> cir) {
		if(passenger instanceof PlayerServer &&  passenger.vehicle instanceof PlayerServer) {
			passenger.heightOffset = 0;
			passenger.fallDistance = 0;
		}
	}
}
