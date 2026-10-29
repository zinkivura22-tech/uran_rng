package ua.dashrpg;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.fml.common.FMLCommonHandler;

@Mod(modid = DashRPG.MODID, name = DashRPG.NAME, version = DashRPG.VERSION)
public class DashRPG {
    public static final String MODID = "dashrpg";
    public static final String NAME = "Dash RPG";
    public static final String VERSION = "0.1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new ClientEvents());
        }
    }

    public static NBTTagCompound data(EntityPlayer player) {
        NBTTagCompound root = player.getEntityData();
        if (!root.hasKey("DashRPG")) {
            NBTTagCompound d = new NBTTagCompound();
            d.setInteger("points", 5);
            d.setInteger("jumps", 2);
            d.setInteger("damage", 0);
            d.setInteger("health", 0);
            d.setInteger("armor", 0);
            d.setInteger("mining", 0);
            d.setInteger("dodge", 0);
            root.setTag("DashRPG", d);
        }
        return root.getCompoundTag("DashRPG");
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer && !event.getEntityLiving().world.isRemote) {
            EntityPlayer p = (EntityPlayer) event.getEntityLiving();
            int dodge = data(p).getInteger("dodge");
            // Maximum 60% chance; a successful dodge cancels this hit.
            int chance = Math.min(60, dodge * 5);
            if (chance > 0 && p.getRNG().nextInt(100) < chance) {
                event.setAmount(0.0F);
            }
        }
        if (event.getSource().getTrueSource() instanceof EntityPlayer && !event.getEntityLiving().world.isRemote) {
            EntityPlayer attacker = (EntityPlayer) event.getSource().getTrueSource();
            int level = data(attacker).getInteger("damage");
            event.setAmount(event.getAmount() * (1.0F + Math.min(10, level) * 0.15F));
        }
    }

    @SubscribeEvent
    public void onBreakSpeed(BreakSpeed event) {
        if (event.getEntityPlayer() != null && !event.getEntityPlayer().world.isRemote) {
            int level = data(event.getEntityPlayer()).getInteger("mining");
            event.setNewSpeed(event.getNewSpeed() * (1.0F + Math.min(10, level) * 0.12F));
        }
    }

    @SubscribeEvent
    public void onClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        NBTTagCompound oldData = event.getOriginal().getEntityData();
        if (oldData.hasKey("DashRPG")) {
            event.getEntityPlayer().getEntityData().setTag("DashRPG",
                    oldData.getCompoundTag("DashRPG").copy());
        }
    }
}
