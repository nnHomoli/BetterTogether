package nnhomoli.bettertogether.mixins;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.IVehicle;
import net.minecraft.server.entity.player.PlayerServer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class,remap=false)
public abstract class entityMixin {
	@Shadow @Nullable public Entity passenger;
	@Shadow public float heightOffset;

	@Inject(method="startRiding",at= @At(value = "INVOKE", target = "Lnet/minecraft/core/world/IVehicle;setPassenger(Lnet/minecraft/core/entity/Entity;)V"))
	public void startRiding(IVehicle vehicle, CallbackInfo ci) {
		Entity entity = (Entity) (Object) this;
		if(entity instanceof Player) {
			if (vehicle instanceof PlayerServer) entity.heightOffset += 0.5F;
			else if(heightOffset >= 0.5) entity.heightOffset -= 0.5F;
		}
	}

	@Inject(method= "ejectRider",at= @At(value = "HEAD"))
	public void ejectRider(CallbackInfoReturnable<Entity> cir) {
		if(passenger instanceof PlayerServer &&  passenger.vehicle instanceof PlayerServer) {
			if(passenger.heightOffset >= 0.5) passenger.heightOffset -= 0.5F;
		}
	}
}
