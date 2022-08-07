package com.example.examplemod.commands;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import com.example.examplemod.ExampleMod;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;


public class SetHomeCommand {

    public SetHomeCommand(CommandDispatcher<CommandSourceStack> dispatcher) {

        // set home command
        dispatcher.register(Commands.literal("set").then(Commands.literal("home").then(Commands.argument("name", StringArgumentType.string()).executes((command) -> {
            return setHome(command.getSource(), StringArgumentType.getString(command, "name"));
        }))));

        // list homes command
        dispatcher.register(Commands.literal("homes").executes((command) -> {
            return getHomes(command.getSource());
        }));

        // return home command
        dispatcher.register(Commands.literal("home").then(Commands.argument("name", StringArgumentType.string()).executes((command) ->{
            return returnHome(command.getSource(), StringArgumentType.getString(command, "name"));
        })));

        // remove home command
        dispatcher.register(Commands.literal("remove").then(Commands.literal("home").then(Commands.argument("name", StringArgumentType.string()).executes((command) ->{
            return removeHome(command.getSource(), StringArgumentType.getString(command, "name"));
        }))));
    }

    private int returnHome(CommandSourceStack source, String name){
        Entity player = source.getPlayer();
        int[] intArray = player.getPersistentData().getIntArray("examplemod:" + name);
        player.teleportTo(intArray[0], intArray[1], intArray[2]);
        source.sendSystemMessage(Component.literal("Teleported player to home: " + name));
        return 1;
    }

    private int getHomes(CommandSourceStack source){
        ArrayList<String> homes = listHomes(source);
        if (homes.size() == 0){
            source.sendSystemMessage(Component.literal("No homes are set!"));
            return -1;
        }
        source.sendSystemMessage(Component.literal("Homes: "));
        source.sendSystemMessage(Component.literal(homes.toString()));
        return 1;
    }

    private int removeHome(CommandSourceStack source, String name){
        Entity player = source.getPlayer();
        try{
            player.getPersistentData().remove("examplemod:" + name);
            source.sendSystemMessage(Component.literal("Removed home: " + name));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }

    private int setHome(CommandSourceStack source, String name) {
        Entity player = source.getPlayer();

        Object[] objects = player.getPersistentData().getAllKeys().toArray();
        for (Object object : objects) {
            if (object.toString().contains("examplemod:" + name)) {
                source.sendSystemMessage(Component.literal("A home by the name: " + name + " already exists!"));
                source.sendSystemMessage(Component.literal("To remove a home: \"/remove [home name]\""));
                return -1;
            }
        }

        int [] home_coordinates = {player.getBlockX(), player.getBlockY(), player.getBlockZ()};
        String xyz = Arrays.toString(home_coordinates);

        //add the home to the player's data
        player.getPersistentData().putIntArray(ExampleMod.MODID + ":"  + name, home_coordinates);

        source.sendSystemMessage(Component.literal("Set a home at " + xyz));
        return 1;
    }

    private ArrayList<String> listHomes(CommandSourceStack source){
        Entity player = source.getPlayer();
        ArrayList<String> homes = new ArrayList<>();
        Object[] objects = player.getPersistentData().getAllKeys().toArray();
        for (Object object : objects) {
            if (object.toString().contains("examplemod:")) {
                homes.add(object.toString().substring(11));
            }
        }
        return homes;
    }
}
