package nnhomoli.bettertogether.mixins;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.IVehicle;

import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class, remap=false)
abstract class entityMixin {
	@Inject(method="startRiding",at= @At(value = "INVOKE", target = "Lnet/minecraft/core/world/IVehicle;setPassenger(Lnet/minecraft/core/entity/Entity;)V"))
	public void startRiding(IVehicle vehicle, CallbackInfo ci) {
		Entity entity = (Entity) (Object) this;
		if(entity instanceof Player) {
			Player p = (Player) entity;
			if (vehicle instanceof PlayerServer) p.heightOffset = 0.5F;
			else p.heightOffset = 0.0F;
		}
	}

	@Inject(method= "ejectRider",at= @At(value = "HEAD"))
	public void ejectRider(CallbackInfoReturnable<Entity> cir) {
		Entity entity = (Entity) (Object) this;
		if(entity instanceof Player) {
			Player p = (Player) entity;
			if(p.getPassenger() instanceof PlayerServer && p.getPassenger().vehicle instanceof PlayerServer) p.getPassenger().heightOffset = 0.0F;
		}
	}
}
