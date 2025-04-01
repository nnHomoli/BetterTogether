package nnhomoli.bettertogether.mixins;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.packet.PacketAddPlayer;
import net.minecraft.core.net.packet.PacketRemoveEntity;
import net.minecraft.core.world.IVehicle;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static nnhomoli.bettertogether.lib.checkTower.*;
import static nnhomoli.bettertogether.BetterTogether.*;

@Mixin(value = Player.class,remap=false)
abstract class playerMixin extends Entity {
	public playerMixin(World world) {super(world);}

	@Inject(method="interact",at= @At(value = "HEAD"), cancellable = true)
	public void Interact(Player p, CallbackInfoReturnable<Boolean> cir) {
		Player player = (Player) (Object) this;

		if(p.isSneaking()) {
			if(getPlayerPickup()) {
				Player og = p;
				p = player;
				player = og;
			} else return;
		}

		IVehicle last = player.vehicle;
		if(getTowering()) last = getTowerRoot(player);

		if(last == null || getTowering()) {
			if(last != null && getVehicleLimit() && !(last instanceof Player)) return;

			if (player.getPassenger() == null && p.getPassenger() == null) p.startRiding(player);
			else if (getTowering() && !includedInTower(player, p)) p.startRiding(getTowerTop(player));

			cir.setReturnValue(true);
		}
	}
	@Inject(method = "onDeath",at=@At("HEAD"))
	public void onDeath(Entity entityKilledBy, CallbackInfo ci) {
		Player player = (Player) (Object) this;
		if(player.getPassenger() != null) {
			player.ejectRider();
		}
	}
	@Inject(method= "attackTargetEntityWithCurrentItem",at= @At(value = "HEAD"), cancellable = true)
	public void attackTargetEntityWithCurrentItem(Entity entity, CallbackInfo ci) {
		Player player = (Player) (Object) this;
		if(entity.vehicle == player && player.getPassenger() instanceof Player) {
 			if(getVehicleEject()) {
				player.ejectRider();

				PlayerServer ent = (PlayerServer) entity;
				ent.playerNetServerHandler.sendPacket(new PacketRemoveEntity(player.id));
				ent.playerNetServerHandler.sendPacket(new PacketAddPlayer(player));
			}
			ci.cancel();
		} else if(entity instanceof Player && includedInTower(player,entity)) ci.cancel();
	}
	@Inject(method="causeFallDamage",at=@At("HEAD"), cancellable = true)
	protected void causeFallDamage(float distance, CallbackInfo ci) {
		Player player = (Player) (Object) this;
		if(player.vehicle instanceof Player) ci.cancel();
	}

	@Override
	public void startRiding(IVehicle e) {
		Player p = (Player) (Object) this;
		if(getVehicleLimit() && p.getPassenger() != null && !(e instanceof Player) && !(e instanceof TileEntity)) {return;}
		super.startRiding(e);
	}
	@Override
	public double getRideHeight(){
		return (double)this.bbHeight * (double)1.25F;
	}
}
