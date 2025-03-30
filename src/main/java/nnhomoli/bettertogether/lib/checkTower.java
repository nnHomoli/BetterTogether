package nnhomoli.bettertogether.lib;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.world.IVehicle;

public final class checkTower {
	public static IVehicle getTowerRoot(IVehicle vehicle) {
		if(vehicle instanceof Entity && ((Entity) vehicle).vehicle != null) {
			return getTowerRoot(((Entity) vehicle).vehicle);
		} else {
			return vehicle;
		}
	}
	public static Entity getTowerTop(Entity entity) {
		if(entity.passenger == null) return entity;
		return getTowerTop(entity.passenger);
	}
	public static boolean includedInTower(IVehicle vehicle, Entity entity) {
		return getTowerRoot(vehicle) == getTowerRoot(entity);
	}
}
