package de.vatinmc.cmdconfirm.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import de.vatinmc.cmdconfirm.screen.ConfigScreen;
import net.minecraft.client.gui.screen.Screen;

public class ScreenFactory implements ConfigScreenFactory<ConfigScreen> {
    @Override
    public ConfigScreen create(Screen screen){
        return new ConfigScreen(screen);
    }
}