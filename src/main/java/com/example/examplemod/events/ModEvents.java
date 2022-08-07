package com.example.examplemod.events;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.commands.SetHomeCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event){
        new SetHomeCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }
}
