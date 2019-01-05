package com.kamefrede.reskillablepsicompat.handler;

import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.network.MessageLockedItem;
import com.kamefrede.reskillablepsicompat.RSPCompat;
import com.kamefrede.reskillablepsicompat.util.RSPConfigHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.psi.api.cad.CADTakeEvent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.PreSpellCastEvent;

import static codersafterdark.reskillable.base.LevelLockHandler.*;

@Mod.EventBusSubscriber(modid = RSPCompat.MODID)
public class RSPCompatLockHandler {

    private static final String TAG_CHECKED = "militaryGradeCertified";


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void preSpellCastEvent(PreSpellCastEvent event) {
        genericEnforce(event, event.getPlayer(), event.getCad(), MessageLockedItem.MSG_ITEM_LOCKED);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void takeCadEvent(CADTakeEvent event) {
        if (!event.getCad().isEmpty() && event.getCad().getItem() instanceof ICAD)
            takeEnforce(event, event.getPlayer(), event.getCad(), MessageLockedItem.MSG_ITEM_LOCKED);
    }

    //copypasta from LevelLockHandler just to add a line
    public static void genericEnforce(Event event, EntityPlayer player, ItemStack stack, String lockMessage) {
        if (!event.isCancelable() || event.isCanceled() || player == null || stack == null || stack.isEmpty() || (!ConfigHandler.enforceOnCreative && player.isCreative())) {
            return;
        }
        if (ConfigHandler.enforceFakePlayers) {
            if (!canPlayerUseItem(player, stack)) {
                event.setCanceled(true);
                if (!isFake(player)) {
                    player.getCooldownTracker().setCooldown(stack.getItem(), 40);
                    tellPlayer(player, stack, lockMessage);
                }
            }
        } else if (!isFake(player) && !canPlayerUseItem(player, stack)) {
            player.getCooldownTracker().setCooldown(stack.getItem(), 40);
            tellPlayer(player, stack, lockMessage);
            event.setCanceled(true);
        }
    }


    public static void takeEnforce(Event event, EntityPlayer player, ItemStack stack, String lockMessage) {
        if (RSPConfigHandler.disableTaking) {
            genericEnforce(event, player, stack, lockMessage);
        }
    }
}
