package com.kamefrede.reskillablepsicompat.util;

import com.kamefrede.reskillablepsicompat.RSPCompat;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

@Mod.EventBusSubscriber(modid = RSPCompat.MODID)
public class RSPConfigHandler {
    public static Configuration config;


    public static boolean disableTaking = true;
    public static boolean disableCasting = true;


    public static void init(File configFile) {
        config = new Configuration(configFile);
        config.addCustomCategoryComment(Configuration.CATEGORY_GENERAL, comment);
        config.load();
        load();
    }

    static String comment = "This addon adds compatibility between PSI and Reskillable and it does so in the following way:\n"
            + "The CADs now inherit the skill value of their respective components. For example, if you craft a CAD with an Iron Assembly and "
            + " you've set the Iron Assembly to require 10 magic, the CAD will require 10 Magic + whatever you set psi:cad to require. \n"
            + "To actually set the component requirement values you need to do so in the Reskillable.cfg file.\n"
            + "You can also mix and match skill requirements and advancements to lock the items.\n\n"
            + "Bellow you'll find options to toggle certain kinds of enforcements the CAD must obey.\n";

    public static void load() {
        disableTaking = loadPropBool("Disable taking the CAD", "If this option is enabled the player will not be able to take their CADs from the CAD assembler unless they met the set requirements for it.", disableTaking);
        disableCasting = loadPropBool("Disable casting the CAD", "If this option is enabled the player will not be able to cast a spell unless they meet the CAD's requirements", disableCasting);


        if (config.hasChanged()) {
            config.save();
        }
    }

    public static int loadPropInt(String propName, String desc, int default_) {
        Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
        prop.setComment(desc);

        return prop.getInt(default_);
    }

    public static double loadPropDouble(String propName, String desc, double default_) {
        Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
        prop.setComment(desc);

        return prop.getDouble(default_);
    }

    public static boolean loadPropBool(String propName, String desc, boolean default_) {
        Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
        prop.setComment(desc);

        return prop.getBoolean(default_);
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.getModID().equals(RSPCompat.MODID)) {
            load();
        }
    }
}
