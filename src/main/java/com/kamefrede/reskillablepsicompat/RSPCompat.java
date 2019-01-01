package com.kamefrede.reskillablepsicompat;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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

}
