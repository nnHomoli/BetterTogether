package nnhomoli.betterwithsitting.mixins;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Player.class,remap = false)
public abstract class playerMixin {
	@Shadow public abstract void playDeathSound();

	@Inject(method = "useCurrentItemOnEntity",at= @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/entity/Entity;interact(Lnet/minecraft/core/entity/player/Player;)Z"))
	public void useCurrentItemOnEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		Player player = (Player) (Object) this;
		if(entity instanceof Player && entity.vehicle != player) {
			if(entity.getPassenger() == null) {
//				if(player.vehicle != null) player.vehicle.ejectRider();

				player.startRiding(entity);
				player.heightOffset += 0.5F;
			}
		}
	}

	@Inject(method = "onDeath",at=@At("HEAD"))
	public void onDeath(Entity entityKilledBy, CallbackInfo ci) {
		Player player = (Player) (Object) this;
		if(player.getPassenger() != null) {
			player.ejectRider();
		}
	}

	@Inject(method= "attackTargetEntityWithCurrentItem",at= @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/player/gamemode/Gamemode;canInteract()Z"), cancellable = true)
	public void attackTargetEntityWithCurrentItem(Entity entity, CallbackInfo ci) {
		if(entity.vehicle instanceof PlayerServer && entity.vehicle.getPassenger() instanceof PlayerServer) {
			entity.startRiding(null);
//			entity.vehicle.ejectRider();
			ci.cancel();
		} else
			if(entity.getPassenger() != null && entity == entity.getPassenger().vehicle && entity.getPassenger().vehicle instanceof PlayerServer) {
			ci.cancel();
		}
	}
}
