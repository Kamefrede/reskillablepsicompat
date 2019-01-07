package com.kamefrede.reskillablepsicompat;

import codersafterdark.reskillable.base.LevelLockHandler;
import com.kamefrede.reskillablepsicompat.cadlocks.CadLockInheritanceKey;
import com.kamefrede.reskillablepsicompat.cadlocks.CadLockNonInheritanceKey;
import com.kamefrede.reskillablepsicompat.util.RSPConfigHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(modid = RSPCompat.MODID, name = RSPCompat.NAME, version = RSPCompat.VERSION, dependencies = "required-after:psi@[r1.1-63,);required-after:reskillable;required-after:forge;", useMetadata = true)
public class RSPCompat {
    public static final String MODID = "rspcompat";
    public static final String NAME = "Reskillable PSI Compat";
    public static final String VERSION = "%VERSION%";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Mod.Instance
    public static RSPCompat INSTANCE;

    @Mod.EventHandler
    @Optional.Method(modid = "rpsideas")
    public void easterEggInit(FMLInitializationEvent event) {
        LOGGER.info("Hey PSIdeas, let's take over the world!");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (RSPConfigHandler.enableInheritance)
            LevelLockHandler.registerLockKey(ItemStack.class, CadLockInheritanceKey.class);
        else
            LevelLockHandler.registerLockKey(ItemStack.class, CadLockNonInheritanceKey.class);

    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        RSPConfigHandler.init(event.getSuggestedConfigurationFile());
    }



}
