package de.vatinmc.cmdconfirm.mixin;

import de.vatinmc.cmdconfirm.screen.ConfirmationScreen;
import de.vatinmc.cmdconfirm.client.CmdConfirmClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {
    @Shadow protected TextFieldWidget chatField;

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if(keyCode == 257 || keyCode == 335){
            if(chatField.getText().startsWith("/")){
                String command = chatField.getText().substring(1);

                for(String cmd : CmdConfirmClient.cmds){
                    if(command.equals(cmd)){
                        client.setScreen(new ConfirmationScreen(this, cmd));
                        cir.cancel();
                        break;
                    }
                }
            }
        }
    }
}
