package com.megarata.command;

import com.megarata.server.LootTableCache;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class ChestReloadCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("chestloot")
                .executes(ChestReloadCommand::run));


    }

    private static int run(CommandContext<ServerCommandSource> context) {
        LootTableCache.load();
        return 1;
    }

}
