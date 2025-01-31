package nnhomoli.looking.entity;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Entity;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class seat extends Entity {
	private boolean attached = false;
	private int timeout = 0;
	private final Player player;


	public seat(@Nullable World world, Player plr) {
		super(world);
		this.player = plr;
	}

	@Override
	public void tick()  {
		if(!attached) {
			setPos(player.serverPosX, player.serverPosY-0.2, player.serverPosZ);

			player.vehicle = this;
			this.passenger = player;
			attached = true;
		}

		if(this.getPassenger() == null) this.remove();
//		this.baseTick();
	}
	@Override
	protected void defineSynchedData() {

	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {

	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {

	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
