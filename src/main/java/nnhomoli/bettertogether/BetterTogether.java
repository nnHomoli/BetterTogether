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
	private static boolean vehicleEject;

	public void setupConfig() {
		Properties props = new Properties();
		props.setProperty("vehicle-eject", "true");
		props.setProperty("player-towering", "false");
		ConfigHandler cfg = new ConfigHandler(MOD_ID, props);

		towering = cfg.getBoolean("player-towering");
		vehicleEject = cfg.getBoolean("vehicle-eject");
	}

	public static boolean getTowering() {return towering;}
	public static boolean getVehicleEject() {return vehicleEject;}

	@Override
	public void onInitialize() {
		setupConfig();
		LOGGER.info("Better Together initialized.");
	}
}
