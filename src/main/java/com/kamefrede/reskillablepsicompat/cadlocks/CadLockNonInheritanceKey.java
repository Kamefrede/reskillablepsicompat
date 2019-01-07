package com.kamefrede.reskillablepsicompat.cadlocks;

import codersafterdark.reskillable.api.data.ParentLockKey;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.base.LevelLockHandler;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADComponent;

import java.util.ArrayList;
import java.util.List;

import static codersafterdark.reskillable.base.LevelLockHandler.getSkillLock;

public class CadLockNonInheritanceKey implements ParentLockKey {
    private ItemStack stack;

    public CadLockNonInheritanceKey(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public RequirementHolder getSubRequirements() {
        if (stack.getItem() instanceof ICAD) {
            ICAD icad = (ICAD) stack.getItem();

            List<RequirementHolder> holders = new ArrayList<>();
            for (EnumCADComponent type : EnumCADComponent.values()) {
                ItemStack component = icad.getComponentInSlot(stack, type);
                if (!component.isEmpty() && component.getItem() instanceof ICADComponent) {
                    holders.add(getSkillLock(component));
                }
            }
            return holders.isEmpty() ? LevelLockHandler.EMPTY_LOCK : new RequirementHolder(holders.toArray(new RequirementHolder[0]));
        }
        return LevelLockHandler.EMPTY_LOCK;

    }
}
