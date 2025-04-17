package nnhomoli.bettertogether.mixins;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.packet.PacketAddPlayer;
import net.minecraft.core.net.packet.PacketRemoveEntity;
import net.minecraft.core.player.gamemode.Gamemode;
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
abstract class playerMixin extends Mob {
	public playerMixin(World world) {super(world);}

	@Inject(method="interact",at= @At(value = "HEAD"), cancellable = true)
	public void interact(Player p, CallbackInfoReturnable<Boolean> cir) {
		Player player = (Player) (Object) this;

		if(p.isSneaking()) {
			if(getPlayerPickup() && p.getGamemode() != Gamemode.adventure) {
				Player og = p;
				p = player;
				player = og;
			} else return;
		}

		IVehicle last = getTowerRoot(player);
		if(getVehicleLimit() && !(last instanceof Player)&& !(last instanceof TileEntity)) return;

		if (player.getPassenger() == null && p.getPassenger() == null && (!getVehicleLimit()||player.vehicle == null||player.vehicle instanceof TileEntity)) p.startRiding(player);
		else if (getTowering() && !includedInTower(player, p)) p.startRiding(getTowerTop(player));

		if(p.vehicle == player) cir.setReturnValue(true);
	}
	@Inject(method = "onDeath",at=@At("HEAD"))
	public void onDeath(Entity entityKilledBy, CallbackInfo ci) {
		if(this.getPassenger() != null) this.ejectRider();
	}
	@Inject(method= "attackTargetEntityWithCurrentItem",at= @At(value = "HEAD"), cancellable = true)
	public void attackTargetEntityWithCurrentItem(Entity entity, CallbackInfo ci) {
		if(entity instanceof  Player) {
			if(includedInTower(this, entity)) ci.cancel();
			if (entity.vehicle == this && getVehicleEject()) {
				this.ejectRider();

				if(!isSyncMyRideLoaded) {
					PlayerServer ent = (PlayerServer) entity;
					ent.playerNetServerHandler.sendPacket(new PacketRemoveEntity(this.id));
					ent.playerNetServerHandler.sendPacket(new PacketAddPlayer((Player) (Object) this));
				}
			}
		}
	}
	@Inject(method="causeFallDamage",at=@At("HEAD"), cancellable = true)
	protected void causeFallDamage(float distance, CallbackInfo ci) {
		if(this.vehicle instanceof Player) ci.cancel();
	}
	@Override
	public void startRiding(IVehicle e) {
		if(getVehicleLimit() && e != null && this.getPassenger() != null && !(e instanceof Player) && !(e instanceof TileEntity)) {return;}
		super.startRiding(e);
	}
	@Override
	public double getRideHeight(){
		return (double)this.bbHeight * (double)1.25F;
	}
}
