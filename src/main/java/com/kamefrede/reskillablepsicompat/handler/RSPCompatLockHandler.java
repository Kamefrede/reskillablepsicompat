package com.kamefrede.reskillablepsicompat.handler;

import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.base.LevelLockHandler;
import codersafterdark.reskillable.network.MessageLockedItem;
import com.kamefrede.reskillablepsicompat.RSPCompat;
import com.kamefrede.reskillablepsicompat.util.RSPConfigHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.psi.api.cad.CADTakeEvent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.PreSpellCastEvent;

import java.util.function.Consumer;

import static codersafterdark.reskillable.base.LevelLockHandler.*;

@Mod.EventBusSubscriber(modid = RSPCompat.MODID)
public class RSPCompatLockHandler {


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void preSpellCastEvent(PreSpellCastEvent event) {
        if (!event.getPlayer().world.isRemote)
            genericEnforce(event::setCanceled, event::setCancellationMessage, event.getPlayer(), event.getCad(), MessageLockedItem.MSG_ITEM_LOCKED, "rspcompat.error.castspell");


    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void takeCadEvent(CADTakeEvent event) {
        if (!event.getCad().isEmpty() && event.getCad().getItem() instanceof ICAD && RSPConfigHandler.disableTaking)
            genericEnforce(event::setCanceled, event::setCancellationMessage, event.getPlayer(), event.getCad(), MessageLockedItem.MSG_ITEM_LOCKED, "rspcompat.error.takecad");
    }

    //copypasta from LevelLockHandler just to add a line

    public static void genericEnforce(Consumer<Boolean> cancel, Consumer<String> cancellationMessage, EntityPlayer player, ItemStack stack, String lockMessage, String cancelMessage) {
        if (player == null || (!ConfigHandler.enforceOnCreative && player.capabilities.isCreativeMode))
            return;

        if (ConfigHandler.enforceFakePlayers) {
            if (!canPlayerUseItem(player, stack)) {
                cancel.accept(true);
                cancellationMessage.accept(cancelMessage);
                if (!isFake(player)) {
                    player.getCooldownTracker().setCooldown(stack.getItem(), 40);
                    tellPlayer(player, stack, lockMessage);
                }
            }
        } else if (!isFake(player) && !canPlayerUseItem(player, stack)) {
            player.getCooldownTracker().setCooldown(stack.getItem(), 40);
            tellPlayer(player, stack, lockMessage);
            cancel.accept(true);
            cancellationMessage.accept(cancelMessage);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void rightClickItemOverride(PlayerInteractEvent.RightClickItem event) {
        if (!event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof ICAD && event.getEntityPlayer() != null && !LevelLockHandler.canPlayerUseItem(event.getEntityPlayer(), event.getItemStack()))
            event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void rightClickItemOverrideBoogaloo(PlayerInteractEvent.RightClickItem event) {
        if (event.isCanceled() && !event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof ICAD && event.getEntityPlayer() != null && !LevelLockHandler.canPlayerUseItem(event.getEntityPlayer(), event.getItemStack()))
            event.setCanceled(false);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void rightClickBlockOverride(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof ICAD && event.getEntityPlayer() != null && !LevelLockHandler.canPlayerUseItem(event.getEntityPlayer(), event.getItemStack()))
            event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void rightClickBlockOverrideBoogaloo(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCanceled() && !event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof ICAD && event.getEntityPlayer() != null && !LevelLockHandler.canPlayerUseItem(event.getEntityPlayer(), event.getItemStack()))
            event.setCanceled(false);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void entityInteractOverride(PlayerInteractEvent.EntityInteract event) {
        if (!event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof ICAD && event.getEntityPlayer() != null && !LevelLockHandler.canPlayerUseItem(event.getEntityPlayer(), event.getItemStack()))
            event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void entityInteractOverrideBoogaloo(PlayerInteractEvent.EntityInteract event) {
        if (event.isCanceled() && !event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof ICAD && event.getEntityPlayer() != null && !LevelLockHandler.canPlayerUseItem(event.getEntityPlayer(), event.getItemStack()))
            event.setCanceled(false);
    }




}
