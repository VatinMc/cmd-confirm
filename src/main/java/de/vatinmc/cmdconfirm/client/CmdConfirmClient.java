package de.vatinmc.cmdconfirm.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CmdConfirmClient implements ClientModInitializer {
    public static final String MOD_ID = "cmd-confirm";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static List<String> cmds = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        reloadConfig();
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("CCCreload").executes(context -> {
            reloadConfig();
            context.getSource().sendFeedback(Text.literal("[" + MOD_ID + "] Config loaded"));
            return 1;
        })));
    }

    public static void reloadConfig(){
        String dirPath = "config/" + MOD_ID;
        try {
            File dir = new File(dirPath);
            if(dir.mkdir()){
                LOGGER.info("Directory {} created.", dir.getAbsolutePath());
            }

            String cfgPath = dirPath + "/config.txt";
            try {
                File config = new File(cfgPath);
                if(config.createNewFile()){
                    LOGGER.info("Config file {} created.", config.getAbsolutePath());
                    Path configPath = Path.of(config.getAbsolutePath());
                    Files.write(configPath, "kill".getBytes(), StandardOpenOption.APPEND);
                    Files.write(configPath, "\nkill @p".getBytes(), StandardOpenOption.APPEND);
                    Files.write(configPath, "\nkill @e".getBytes(), StandardOpenOption.APPEND);
                    cmds.add("kill");
                    cmds.add("kill @p");
                    cmds.add("kill @e");
                } else {
                    cmds.clear();
                    Scanner cfgScanner = new Scanner(config);
                    while (cfgScanner.hasNextLine()){
                        String cmd = cfgScanner.nextLine();
                        cmds.add(cmd);
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Loading/Creating config file failed.");
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
