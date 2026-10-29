package ua.dashrpg;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SkillGui extends GuiScreen {
    private final List<GuiButton> upgradeButtons = new ArrayList<GuiButton>();
    private EntityPlayer player;

    @Override
    public void initGui() {
        player = mc.player;
        buttonList.clear();
        upgradeButtons.clear();
        int left = width / 2 - 150;
        int top = height / 2 - 82;
        String[] names = {"Extra jumps", "Damage", "Health", "Armor", "Mining speed", "Dodge chance"};
        for (int i = 0; i < names.length; i++) {
            GuiButton b = new GuiButton(i, left, top + i * 22, 300, 20, names[i] + "  +1");
            buttonList.add(b);
            upgradeButtons.add(b);
        }
        buttonList.add(new GuiButton(99, width / 2 - 50, top + 137, 100, 20, "Close"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 99) {
            mc.displayGuiScreen(null);
            return;
        }
        if (player == null) return;
        NBTTagCompound d = DashRPG.data(player);
        if (d.getInteger("points") <= 0) return;
        String[] keys = {"jumps", "damage", "health", "armor", "mining", "dodge"};
        int idx = button.id;
        if (idx < 0 || idx >= keys.length) return;
        int max = idx == 0 ? 10 : 10;
        String key = keys[idx];
        int current = d.getInteger(key);
        if (idx == 0) current = Math.max(0, current - 1); // Stored as extra-jump count; start at 2.
        if (current >= max) return;
        d.setInteger(key, d.getInteger(key) + 1);
        d.setInteger("points", d.getInteger("points") - 1);
        if (idx == 2) {
            player.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.MAX_HEALTH)
                    .setBaseValue(20.0D + Math.min(10, d.getInteger("health")) * 2.0D);
            player.setHealth(Math.min(player.getHealth() + 2.0F, player.getMaxHealth()));
        }
        if (idx == 3) {
            player.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.ARMOR)
                    .setBaseValue(Math.min(20.0D, d.getInteger("armor") * 2.0D));
        }
        if (idx == 0) {
            d.setInteger("jumps", Math.min(10, d.getInteger("jumps") + 1));
        }
        initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        NBTTagCompound d = player == null ? new NBTTagCompound() : DashRPG.data(player);
        drawCenteredString(fontRenderer, "Dash RPG - Skills", width / 2, height / 2 - 105, 0x55FFFF);
        drawCenteredString(fontRenderer, "Skill points: " + d.getInteger("points"), width / 2, height / 2 - 94, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
