package nnhomoli.bettertogether;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.ConfigHandler;

import java.util.Properties;

public class Main implements ModInitializer {
	public static final String MOD_ID = "bettertogether";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static boolean towering;
	public static boolean vehicleEject;

	public static final ConfigHandler cfg;
	static {
		Properties props = new Properties();
		props.setProperty("experimental-vehicle-eject", "false");
		props.setProperty("player-towering", "false");
		cfg = new ConfigHandler(MOD_ID,props);
	}

	@Override
	public void onInitialize() {
		towering = cfg.getBoolean("player-towering");
		vehicleEject = cfg.getBoolean("experimental-vehicle-eject");

		LOGGER.info("Better Together initialized.");
	}
}
