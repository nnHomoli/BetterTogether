package nnhomoli.bettertogether.misc;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.world.IVehicle;

public class checkTower {
	public static IVehicle checkTowerRoot(IVehicle vehicle) {
		if(vehicle instanceof Entity && ((Entity) vehicle).vehicle != null) {
			return checkTowerRoot(((Entity) vehicle).vehicle);
		} else {
			return vehicle;
		}
	}
	public static Entity getTowerTop(Entity entity) {
		if(entity.passenger == null) return entity;
		return getTowerTop(entity.passenger);
	}
	public static boolean includedInTower(IVehicle vehicle, Entity entity) {
		return checkTowerRoot(vehicle) == checkTowerRoot(entity);
	}
}
