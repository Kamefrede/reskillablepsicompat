package com.kamefrede.reskillablepsicompat.cadlocks;

import codersafterdark.reskillable.api.data.ParentLockKey;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.base.LevelLockHandler;
import com.kamefrede.reskillablepsicompat.util.RSPCompatUtil;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADComponent;

import java.util.ArrayList;
import java.util.List;

import static codersafterdark.reskillable.base.LevelLockHandler.getSkillLock;

public class CadLockInheritanceKey implements ParentLockKey {

    private ItemStack stack;

    public CadLockInheritanceKey(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public RequirementHolder getSubRequirements() {
        if (stack.getItem() instanceof ICAD) {
            ICAD icad = (ICAD) stack.getItem();

            List<Requirement> requirements = new ArrayList<>();
            for (EnumCADComponent type : EnumCADComponent.values()) {
                ItemStack component = icad.getComponentInSlot(stack, type);
                if (!component.isEmpty() && component.getItem() instanceof ICADComponent) {
                    List<Requirement> compreq = getSkillLock(component).getRequirements();
                    for (Requirement each : compreq) {
                        RSPCompatUtil.addRequirement(requirements, each);
                    }
                }
            }
            return requirements.isEmpty() ? LevelLockHandler.EMPTY_LOCK : new RequirementHolder(requirements);
        }
        return LevelLockHandler.EMPTY_LOCK;

    }
}
