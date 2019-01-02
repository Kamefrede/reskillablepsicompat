package com.kamefrede.reskillablepsicompat.handler;

import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.base.LevelLockHandler;
import codersafterdark.reskillable.network.MessageLockedItem;
import com.kamefrede.reskillablepsicompat.RSPCompat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.api.cad.PostCADCraftEvent;
import vazkii.psi.api.spell.PreSpellCastEvent;

import java.util.ArrayList;
import java.util.List;

import static codersafterdark.reskillable.base.LevelLockHandler.*;

@Mod.EventBusSubscriber(modid = RSPCompat.MODID)
public class RSPCompatLockHandler {

    @SubscribeEvent()
    public static void craftEvent(PostCADCraftEvent event) {
        ICAD icad = (ICAD) event.getCad().getItem();
        ItemStack cad = event.getCad();
        List<RequirementHolder> holders = new ArrayList<>();
        if (LevelLockHandler.getSkillLock(cad) != EMPTY_LOCK) {
            holders.add(LevelLockHandler.getSkillLock(cad));
        }
        for (EnumCADComponent type : EnumCADComponent.values()) {
            ItemStack component = icad.getComponentInSlot(cad, type);
            if (!component.isEmpty() && component.getItem() instanceof ICADComponent) {
                holders.add(LevelLockHandler.getSkillLock(component));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void preSpellCastEvent(PreSpellCastEvent event) {
        ICAD icad = (ICAD) event.getCad().getItem();
        ItemStack cad = event.getCad();
        genericEnforce(event, event.getPlayer(), cad, MessageLockedItem.MSG_ITEM_LOCKED, cad);
    }

    //copypasta from LevelLockHandler just to add a line
    public static void genericEnforce(Event event, EntityPlayer player, ItemStack stack, String lockMessage, ItemStack cad) {
        if (!event.isCancelable() || event.isCanceled() || player == null || stack == null || stack.isEmpty() || (!ConfigHandler.enforceOnCreative && player.isCreative())) {
            return;
        }
        if (ConfigHandler.enforceFakePlayers) {
            if (!canPlayerUseItem(player, stack)) {
                event.setCanceled(true);
                if (!isFake(player)) {
                    player.getCooldownTracker().setCooldown(cad.getItem(), 40);
                    tellPlayer(player, stack, lockMessage);
                }
            }
        } else if (!isFake(player) && !canPlayerUseItem(player, stack)) {
            player.getCooldownTracker().setCooldown(cad.getItem(), 40);
            tellPlayer(player, stack, lockMessage);
            event.setCanceled(true);
        }
    }
}
