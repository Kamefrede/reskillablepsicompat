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
        boolean isSkill = false;
        if (requirement == null || requirement instanceof TrueRequirement) {
            return;
        }
        for (Requirement req : requirements) {
            RequirementComparision match = req.matches(requirement);
            if (!match.equals(RequirementComparision.NOT_EQUAL) && req instanceof SkillRequirement && requirement instanceof SkillRequirement) {
                isSkill = true;
                addSkillRequirement(requirements, (SkillRequirement) req, (SkillRequirement) requirement);
                break;
            }
            switch (match) {
                case EQUAL_TO:
                    return;
                case LESS_THAN:
                    requirements.remove(req);
                    break;
                case GREATER_THAN:
                    return;
            }
        }
        if (!isSkill)
            requirements.add(requirement);
    }

    private static void addSkillRequirement(List<Requirement> requirements, SkillRequirement requirement, SkillRequirement other) {
        requirements.remove(requirement);
        requirements.add(new SkillRequirement(requirement.getSkill(), requirement.getLevel() + other.getLevel()));
    }



    public static void addCadLock(LockKey key, RequirementHolder holder) {
        LevelLockHandler.addLockByKey(key, holder);
    }

}
