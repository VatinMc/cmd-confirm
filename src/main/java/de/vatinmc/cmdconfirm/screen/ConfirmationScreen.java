package de.vatinmc.cmdconfirm.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ConfirmationScreen extends Screen {
    private int centerX = 0;
    private int centerY = 0;
    public ButtonWidget confirm;
    public ButtonWidget abort;
    private final Screen parent;
    private final String cmd;
    private final Text confirmText;

    public ConfirmationScreen(Screen parent, String cmd) {
        super(Text.literal("Confirmation Screen"));
        this.parent = parent;
        this.cmd = cmd;
        confirmText = Text.literal("Want to use \"" + cmd + "\"?");
    }

    @Override
    protected void init(){
        centerX = width / 2;
        centerY = height / 2;
        int buttonWidth = 50;
        int buttonHeight = 20;
        int marginX = 5;
        int buttonX = centerX - buttonWidth - marginX;
        int buttonY = centerY - buttonHeight / 2;
        Text buttonText = Text.translatable("gui.yes");

        confirm = ButtonWidget.builder(buttonText,
                button -> {
                    ChatScreen chatScreen = (ChatScreen) parent;
                    chatScreen.sendMessage("/" + cmd, true);
                    client.setScreen(null);
        }).dimensions(buttonX,buttonY,buttonWidth,buttonHeight).build();

        buttonX = centerX + marginX;
        buttonText = Text.translatable("gui.no");
        abort = ButtonWidget.builder(buttonText,
                button -> {
            close();
        }).dimensions(buttonX,buttonY,buttonWidth,buttonHeight).build();

        addDrawableChild(confirm);
        addDrawableChild(abort);
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta){
        super.render(drawContext,mouseX,mouseY,delta);
        drawContext.drawCenteredTextWithShadow(textRenderer, confirmText, centerX,centerY - 30, 0xffffff);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
        int textWidth = textRenderer.getWidth(confirmText);
        int x1 = centerX - 5 - textWidth / 2;
        int y1 = centerY - 30 - 5;
        int x2 = centerX + 5 + textWidth / 2;
        int y2 = centerY - 22 + 5;
        int border = 1;

        context.fill(x1, y1, x2,y2,0xff000000);//black
        context.fill(x1 + border, y1 + border, x2 - border,y2 - border,0xffffffff);//white

        Identifier texture = Identifier.of("minecraft", "textures/block/obsidian.png");
        context.drawTexture(RenderLayer::getGuiTextured, texture, x1 + border, y1 + border, 16,16, x2-x1-border*2, y2-y1-border*2, 8,8);
    }

    @Override
    public void close(){
        client.setScreen(parent);
    }
}