package nnhomoli.bettertogether.mixins;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.IVehicle;
import net.minecraft.server.entity.player.PlayerServer;
import nnhomoli.bettertogether.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static nnhomoli.bettertogether.misc.checkTower.*;

@Mixin(value = Player.class,remap = false)
public abstract class playerMixin {

	@Inject(method = "useCurrentItemOnEntity",at= @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/entity/Entity;interact(Lnet/minecraft/core/entity/player/Player;)Z"))
	public void useCurrentItemOnEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		Player player = (Player) (Object) this;
		if(entity instanceof Player) {
			IVehicle last = entity.vehicle;
			if(Main.towering) last = checkTowerRoot(entity);
			if(last == null || Main.towering && last instanceof PlayerServer) {
				if (entity.getPassenger() == null && player.getPassenger() == null) {
					if(player.vehicle != null) player.heightOffset = 0;
					player.startRiding(entity);
					player.heightOffset += 0.5F;
				}  else if(Main.towering && !includedInTower(entity,player)) {
					player.startRiding(getTowerTop(entity));
					player.heightOffset += 0.5F;
				}
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
		Player player = (Player) (Object) this;
		if(entity.vehicle == player && player.getPassenger() instanceof PlayerServer) {
//			removing vehicle seems to be isolated, no way to do this without making client side part too
//			unless if there's some workaround that pushes player out of vehicle, which I didn't seem to find
//			pretty sure simplified auth had also issue alike
 			if(Main.vehicleEject) entity.startRiding(null);
			ci.cancel();
		} else if(entity.getPassenger() != null && entity == entity.getPassenger().vehicle && entity.getPassenger().vehicle instanceof PlayerServer) ci.cancel();
	}

	@Inject(method="causeFallDamage",at=@At("HEAD"), cancellable = true)
	protected void causeFallDamage(float distance, CallbackInfo ci) {
		Player player = (Player) (Object) this;
		if(player.vehicle instanceof PlayerServer) ci.cancel();
	}
}
