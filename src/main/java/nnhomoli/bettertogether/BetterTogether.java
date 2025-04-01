package nnhomoli.bettertogether;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.ConfigHandler;

import java.util.Properties;

public final class BetterTogether implements ModInitializer {
	private final String MOD_ID = "BetterTogether";
	private final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static boolean towering;
	private static boolean playerPickup;
	private static boolean vehicleEject;
	private static boolean vehicleLimit;

	public void setupConfig() {
		Properties props = new Properties();
		props.setProperty("player-towering", "false");
		props.setProperty("player-pickup","false");
		props.setProperty("vehicle-eject", "true");
		props.setProperty("vehicle-limit","true");
		ConfigHandler cfg = new ConfigHandler(MOD_ID, props);

		towering = cfg.getBoolean("player-towering");
		playerPickup = cfg.getBoolean("player-pickup");
		vehicleEject = cfg.getBoolean("vehicle-eject");
		vehicleLimit = cfg.getBoolean("vehicle-limit");
	}

	public static boolean getTowering() {return towering;}
	public static boolean getPlayerPickup() {return playerPickup;}
	public static boolean getVehicleEject() {return vehicleEject;}
	public static boolean getVehicleLimit() {return vehicleLimit;}

	@Override
	public void onInitialize() {
		setupConfig();
		LOGGER.info("Better Together initialized.");
	}
}
