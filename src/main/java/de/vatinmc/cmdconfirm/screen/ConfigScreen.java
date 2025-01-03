package de.vatinmc.cmdconfirm.screen;

import de.vatinmc.cmdconfirm.client.CmdConfirmClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private final List<String> cmds = new LinkedList<>();
    private TextFieldWidget textFieldAddCommand;

    public ConfigScreen(Screen parent) {
        super(Text.literal("Cmd List"));
        this.parent = parent;
    }
    public ConfigScreen() {
        super(Text.literal("Cmd List"));
        this.parent = null;
    }

    @Override
    protected void init() {
        super.init();

        cmds.clear();
        cmds.addAll(CmdConfirmClient.cmds);

        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("Add"), button -> {
            String cmd = textFieldAddCommand.getText();
            if(cmd.startsWith("/")) cmd = cmd.substring(1);
            if(!cmd.isEmpty()){
                cmds.addFirst(cmd);
                CmdConfirmClient.cmds.add(cmd);
            }
            textFieldAddCommand.setText("");
        }).dimensions(14,16,30,16).build();

        textFieldAddCommand = new TextFieldWidget(textRenderer, 14 + 30 + 2, 16, width - 14 * 2 - 30 - 2, 16, Text.of("cmdText"));
        textFieldAddCommand.setMaxLength(200);

        addDrawableChild(buttonWidget);
        addDrawableChild(textFieldAddCommand);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        Row.top = 16 + 20;
        renderScrollableList(context,mouseX,mouseY,delta);
    }

    private void renderScrollableList(DrawContext context, int mouseX, int mouseY, float ignoredDelta){
        int row = 0;
        for(String cmd : cmds){
            Row.render(context, row, cmd, new int[]{ mouseX, mouseY });
            row++;
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
        int x1 = 5;
        int y1 = 5;
        int x2 = width - 5;
        int y2 = height - 5;
        int border = 1;

        context.fill(x1, y1, x2,y2,0xff000000);//black
        context.fill(x1 + border, y1 + border, x2 - border,y2 - border,0xffffffff);//white

        Identifier texture = Identifier.of("minecraft", "textures/block/deepslate.png");
        context.drawTexture(texture, x1 +border,y1+border,0,0,x2-5-border*2,y2-5-border*2, 16, 16);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == GLFW_KEY_UP)
                scrollCmds(true);
        if(keyCode == GLFW_KEY_DOWN)
            scrollCmds(false);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        click(mouseX, mouseY);

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if(verticalAmount > 0.0)
                scrollCmds(true);
            if(verticalAmount < 0.0)
                scrollCmds(false);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void close() {
        if (client != null) {
            client.setScreen(parent);
        }
    }

    private void click(double mouseX, double mouseY){
        int[] mousePos = new int[]{ (int) mouseX, (int) mouseY };
        Pair<Integer, Boolean> rowInfo = Row.getClickedRow(mousePos, new int[]{ width, height });
        int row = rowInfo.getLeft();
        if(row == -1 || row >= cmds.size()) return;
        boolean delete = rowInfo.getRight();

        String cmd = cmds.get(row);
        if(delete){
            cmds.remove(cmd);
            CmdConfirmClient.cmds.remove(cmd);
        }
    }

    private void scrollCmds(boolean up){
        if(cmds.size() <= 1) return;
        if(!Row.isOffScreen(cmds.size() - 1, height)) return;
        String cmd;
        if(up){
            cmd = cmds.removeLast();
            cmds.addFirst(cmd);
        } else {
            cmd = cmds.removeFirst();
            cmds.add(cmd);
        }
    }

    private static class Row {
        public static int left = 16;
        public static int top = 16;
        public static int spacing = 5;
        public static int height = 16;
        public static int leftText = left + spacing;
        public static int paddingRowBg = 2;

        public static int colorText = 0xffffffff;
        public static int colorTextHover = 0xffffd700;
        public static int colorBg = 0x99000000;
        public static int colorBgHover = 0xbb000000;

        public Row(){}

        public static void render(DrawContext context, int row, String cmd, int[] mousePos){
            if(isOffScreen(row, context.getScaledWindowHeight())) return;
            int top = getTop(row);
            int topText = top + 4;
            int colorText = Row.colorText;
            int colorRowBg = Row.colorBg;

            int[] areaBg = getArea(row, context.getScaledWindowWidth());
            if(hovered(mousePos, areaBg)){
                colorText = Row.colorTextHover;
                colorRowBg = Row.colorBgHover;

                int leftDelete = areaBg[2] - 16 - 2;
                context.drawItem(new ItemStack(Items.BARRIER, 1), leftDelete, top);
            }

            Text title = Text.of("/" + cmd);
            context.fill(areaBg[0], areaBg[1], areaBg[2], areaBg[3], colorRowBg);

            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawTextWithShadow(textRenderer, title, leftText, topText, colorText);
        }

        public static int getTop(int row){
            return top + row * (height + spacing);
        }

        public static boolean isOffScreen(int row, int screenHeight){
            return (getTop(row) + height + spacing > screenHeight - 6);
        }

        public static int[] getArea(int row, int screenWidth){
            return new int[]{ left - paddingRowBg, getTop(row) - paddingRowBg, screenWidth - left + paddingRowBg, getTop(row) + height + paddingRowBg };
        }

        public static Pair<Integer, Boolean> getClickedRow(int[] mousePos, int[] areaDimensions){
            Pair<Integer, Boolean> out = new Pair<>(-1, false);
            int row = 0;
            boolean clickedOnRow = false;
            boolean clickedOnDelete = false;
            while (!isOffScreen(row, areaDimensions[1])){
                if(hovered(mousePos, getArea(row, areaDimensions[0]))) {
                    clickedOnRow = true;
                    int[] areaDelete = getArea(row, areaDimensions[0]);
                    areaDelete[0] = areaDelete[2] - 16 - 2;
                    if(hovered(mousePos, areaDelete))
                        clickedOnDelete = true;
                    break;
                }
                row++;
            }
            if(clickedOnRow){
                out.setLeft(row);
                out.setRight(clickedOnDelete);
            }

            return out;
        }

        public static boolean hovered(int[] mouse, int[] area){
            boolean hovered = false;
            if(mouse.length == 2 && area.length == 4)
                if(mouse[0] > area[0] && mouse[0] < area[2])
                    if(mouse[1] > area[1] && mouse[1] < area[3])
                        hovered = true;

            return hovered;
        }
    }
}