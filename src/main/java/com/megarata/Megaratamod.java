package com.megarata;

import com.megarata.command.ChestReloadCommand;
import com.megarata.config.Config;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Megaratamod implements ModInitializer {
	public static final String MOD_ID = "megaratamod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitialize() {
		Config.ensureConfigFileExists();
		CommandRegistrationCallback.EVENT.register(ChestReloadCommand::register);
	}

}