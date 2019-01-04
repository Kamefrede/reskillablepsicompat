package com.kamefrede.reskillablepsicompat.util;

import codersafterdark.reskillable.api.data.LockKey;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementComparision;
import codersafterdark.reskillable.api.requirement.SkillRequirement;
import codersafterdark.reskillable.api.requirement.logic.TrueRequirement;
import codersafterdark.reskillable.base.LevelLockHandler;

import java.util.List;

public class RSPCompatUtil {

    public static void addRequirement(List<Requirement> requirements, Requirement requirement) {
        if (requirement == null || requirement instanceof TrueRequirement) {
            return;
        }
        for (int i = 0; i < requirements.size(); i++) {
            RequirementComparision match = requirements.get(i).matches(requirement);

            if (!match.equals(RequirementComparision.NOT_EQUAL) && requirement instanceof SkillRequirement && requirements.get(i) instanceof SkillRequirement) {
                SkillRequirement req = (SkillRequirement) requirements.get(i);
                SkillRequirement other = (SkillRequirement) requirement;
                requirements.remove(i);
                requirements.add(new SkillRequirement(req.getSkill(), req.getLevel() + other.getLevel()));
            }
        }
        requirements.add(requirement);
    }

    public static void addCadLock(LockKey key, RequirementHolder holder) {
        LevelLockHandler.addLockByKey(key, holder);
    }

}
