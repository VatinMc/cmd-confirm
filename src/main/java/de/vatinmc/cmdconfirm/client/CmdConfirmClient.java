package de.vatinmc.cmdconfirm.client;

import de.vatinmc.cmdconfirm.screen.ConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
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
    public static final List<String> cmds = new ArrayList<>();
    private static final List<String> cmdsPre = new ArrayList<>();
    private static final String dirPath = "config/" + MOD_ID;
    private static final String filePath = dirPath + "/cmds.txt";

    @Override
    public void onInitializeClient() {
        initCmdsPre();
        handleFormerConfigFile();
        loadCmdsFile();
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("ccconfig").executes(context -> {
                    MinecraftClient client = context.getSource().getClient();
                    client.send(() -> client.setScreen(new ConfigScreen()));
            return 1;
        })));
        onClose();
    }

    private void initCmdsPre() {
        cmdsPre.add("kill");
        cmdsPre.add("kill @e");
        cmdsPre.add("kill @p");
    }

    private void onClose(){
        ClientLifecycleEvents.CLIENT_STOPPING.register((client) -> saveCmdsFile());
    }

    public static void saveCmdsFile(){
        try {
            File dir = new File(dirPath);
            if(dir.mkdir()){
                LOGGER.info("Directory {} created. Where did it go?", dir.getAbsolutePath());
            }
            try {
                File file = new File(filePath);
                Path filePath = Path.of(file.getAbsolutePath());

                if(!file.delete())
                    LOGGER.error("deleting outdated cmds file failed; located at: {}", filePath);
                if(file.createNewFile()){
                    LOGGER.info("cmds file {} updated.", file.getAbsolutePath());

                    for(String cmd : cmds){
                        String line = cmd + "\n";
                        Files.write(filePath, line.getBytes(), StandardOpenOption.APPEND);
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Updating cmds file failed.");
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadCmdsFile(){
        try {
            File dir = new File(dirPath);
            if(dir.mkdir()){
                LOGGER.info("Directory {} created.", dir.getAbsolutePath());
            }

            try {
                File file = new File(filePath);
                if(file.createNewFile()){
                    LOGGER.info("cmds file {} created.", file.getAbsolutePath());
                    Path filePath = Path.of(file.getAbsolutePath());
                    for (String cmd : cmdsPre){
                        String line = cmd + "\n";
                        Files.write(filePath, line.getBytes(), StandardOpenOption.APPEND);
                    }
                    cmds.addAll(cmdsPre);
                } else {
                    cmds.clear();
                    Scanner fileScanner = new Scanner(file);
                    while (fileScanner.hasNextLine()){
                        String cmd = fileScanner.nextLine();
                        cmds.add(cmd);
                    }
                    fileScanner.close();
                }
            } catch (IOException e) {
                LOGGER.error("Loading/Creating cmds file failed.");
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void handleFormerConfigFile(){
        try {
            File dir = new File(dirPath);
            if(!dir.exists()) return;
            try {
                String configPath = dirPath + "/config.txt";
                File config = new File(configPath);
                if(!config.exists()) return;

                cmdsPre.clear();
                Scanner cfgScanner = new Scanner(config);
                while(cfgScanner.hasNextLine()){
                    String cmd = cfgScanner.nextLine();
                    if(!cmd.isEmpty())
                        cmdsPre.add(cmd);
                }
                cfgScanner.close();
                if(!config.delete())
                    LOGGER.error("Deleting former config file failed; located at: {}", configPath);
            } catch (IOException e) {
                LOGGER.error("Loading former config file failed.");
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
