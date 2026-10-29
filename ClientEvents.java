package ua.dashrpg;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class ClientEvents {
    private static final Minecraft MC = Minecraft.getMinecraft();
    private static final KeyBinding MENU = new KeyBinding("key.dashrpg.menu", Keyboard.KEY_K, "key.categories.dashrpg");
    private static final KeyBinding DASH = new KeyBinding("key.dashrpg.dash", Keyboard.KEY_R, "key.categories.dashrpg");
    private static final KeyBinding ROLL = new KeyBinding("key.dashrpg.roll", Keyboard.KEY_V, "key.categories.dashrpg");
    private int jumpsUsed = 0;
    private boolean wasOnGround = true;
    private int dashCooldown = 0;
    private int rollCooldown = 0;

    public ClientEvents() {
        ClientRegistry.registerKeyBinding(MENU);
        ClientRegistry.registerKeyBinding(DASH);
        ClientRegistry.registerKeyBinding(ROLL);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || MC.player == null || MC.world == null) return;
        EntityPlayer p = MC.player;
        if (p.onGround) {
            jumpsUsed = 0;
            wasOnGround = true;
        } else if (wasOnGround) {
            wasOnGround = false;
        }
        if (dashCooldown > 0) dashCooldown--;
        if (rollCooldown > 0) rollCooldown--;

        while (MENU.isPressed()) MC.displayGuiScreen(new SkillGui());
        while (DASH.isPressed()) {
            if (dashCooldown == 0 && !p.isRiding()) {
                double yaw = Math.toRadians(p.rotationYaw);
                p.motionX = -Math.sin(yaw) * 1.45D;
                p.motionZ = Math.cos(yaw) * 1.45D;
                p.motionY = Math.max(p.motionY, 0.12D);
                p.velocityChanged = true;
                dashCooldown = 18;
            }
        }
        while (ROLL.isPressed()) {
            if (rollCooldown == 0 && !p.isRiding()) {
                double yaw = Math.toRadians(p.rotationYaw);
                p.motionX = -Math.sin(yaw) * 1.05D;
                p.motionZ = Math.cos(yaw) * 1.05D;
                p.motionY = Math.max(p.motionY, 0.02D);
                p.fallDistance = 0;
                p.velocityChanged = true;
                rollCooldown = 28;
            }
        }

        // KeyBinding.isPressed consumes a key press, so each press counts as one extra jump.
        while (MC.gameSettings.keyBindJump.isPressed()) {
            if (!p.onGround && jumpsUsed < Math.min(10, Math.max(2, DashRPG.data(p).getInteger("jumps")))) {
                p.motionY = 0.42D;
                p.fallDistance = 0;
                jumpsUsed++;
            }
        }
    }
}
