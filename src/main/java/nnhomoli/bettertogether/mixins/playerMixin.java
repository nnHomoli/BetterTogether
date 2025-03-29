package nnhomoli.bettertogether.mixins;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.packet.PacketAddPlayer;
import net.minecraft.core.net.packet.PacketRemoveEntity;
import net.minecraft.core.world.IVehicle;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static nnhomoli.bettertogether.misc.checkTower.*;

import static nnhomoli.bettertogether.Main.getTowering;
import static nnhomoli.bettertogether.Main.getVehicleEject;

@Mixin(value = Player.class,remap = false)
public abstract class playerMixin {

	@Inject(method = "useCurrentItemOnEntity",at= @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/entity/Entity;interact(Lnet/minecraft/core/entity/player/Player;)Z"))
	public void useCurrentItemOnEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		Player player = (Player) (Object) this;
		if(entity instanceof Player) {
			IVehicle last = entity.vehicle;
			if(getTowering()) last = checkTowerRoot(entity);
			if(last == null || getTowering() && last instanceof PlayerServer) {
				if (entity.getPassenger() == null && player.getPassenger() == null) {
					if(player.vehicle != null) player.heightOffset = 0;
					player.startRiding(entity);
				}  else if(getTowering() && !includedInTower(entity,player)) {
					player.startRiding(getTowerTop(entity));
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
 			if(getVehicleEject()) {
				player.ejectRider();
//				((PlayerServer)entity).playerNetServerHandler.sendPacket(new PacketSetRiding(entity, null));

				((PlayerServer)entity).playerNetServerHandler.sendPacket(new PacketRemoveEntity(player.id));
				((PlayerServer)entity).playerNetServerHandler.sendPacket(new PacketAddPlayer((player)));
			}
			ci.cancel();
		} else if(entity.getPassenger() instanceof PlayerServer && entity instanceof PlayerServer) ci.cancel();
	}

	@Inject(method="causeFallDamage",at=@At("HEAD"), cancellable = true)
	protected void causeFallDamage(float distance, CallbackInfo ci) {
		Player player = (Player) (Object) this;
		if(player.vehicle instanceof PlayerServer) ci.cancel();
	}
}
