package de.vatinmc.cmdconfirm.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ConfirmationScreen extends Screen {
    private int centerX = 0;
    private int centerY = 0;
    public ButtonWidget confirm;
    public ButtonWidget abort;
    private final Screen parent;
    private final String cmd;

    public ConfirmationScreen(Screen parent, String cmd) {
        super(Text.literal("Confirmation Screen"));
        this.parent = parent;
        this.cmd = cmd;
    }

    @Override
    protected void init(){
        centerX = width / 2;
        centerY = height / 2;
        int buttonWidth = 80;
        int buttonHeight = 20;
        int marginX = 5;
        int marginY = 20;
        int buttonX = centerX - buttonWidth - marginX;
        int buttonY = centerY + buttonHeight + marginY;

        confirm = ButtonWidget.builder(Text.literal("Confirm"),
                button -> {
                    ChatScreen chatScreen = (ChatScreen) parent;
                    chatScreen.sendMessage("/" + cmd, true);
                    client.setScreen((Screen) null);
        }).dimensions(buttonX,buttonY,buttonWidth,buttonHeight).build();

        buttonX = centerX + marginX;
        abort = ButtonWidget.builder(Text.literal("Abort"),
                button -> {
            close();
        }).dimensions(buttonX,buttonY,buttonWidth,buttonHeight).build();

        addDrawableChild(confirm);
        addDrawableChild(abort);
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta){
        super.render(drawContext,mouseX,mouseY,delta);
        drawContext.drawCenteredTextWithShadow(textRenderer, Text.literal("Want to use \"" + cmd + "\"?"), centerX,centerY - 30, 0xffffff);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderDarkening(context);
    }

    @Override
    public void close(){
        client.setScreen(parent);
    }
}
