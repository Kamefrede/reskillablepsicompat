package com.kamefrede.reskillablepsicompat.util;

import codersafterdark.reskillable.api.data.LockKey;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementComparision;
import codersafterdark.reskillable.api.requirement.SkillRequirement;
import codersafterdark.reskillable.api.requirement.logic.TrueRequirement;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.base.LevelLockHandler;

import java.util.List;

public class RSPCompatUtil {

    public static void addRequirement(List<Requirement> requirements, Requirement requirement) {
        if (requirement == null || requirement instanceof TrueRequirement) {
            return;
        }
        boolean isSkill = requirement instanceof SkillRequirement;
        for (Requirement req : requirements) {
            RequirementComparision match = req.matches(requirement);
            if (match != RequirementComparision.NOT_EQUAL) {
                if (isSkill && req instanceof SkillRequirement) {
                    addSkillRequirement(requirements, (SkillRequirement) req, (SkillRequirement) requirement);
                } else if (match == RequirementComparision.LESS_THAN) {
                    requirements.remove(req);
                    requirements.add(requirement);
                }
                return;
            }
        }
        requirements.add(requirement);
    }

    private static void addSkillRequirement(List<Requirement> requirements, SkillRequirement requirement, SkillRequirement other) {
        requirements.remove(requirement);
        int level = requirement.getLevel() + other.getLevel();
        int cap = requirement.getSkill().getCap();
        Skill skill = requirement.getSkill();
        if (RSPConfigHandler.clampInheritanceLevel && RSPConfigHandler.enableInheritance) {
            if (level >= cap)
                requirements.add(new SkillRequirement(skill, cap));
            else
                requirements.add(new SkillRequirement(skill, level));
        } else
            requirements.add(new SkillRequirement(skill, level));

    }



    public static void addCadLock(LockKey key, RequirementHolder holder) {
        LevelLockHandler.addLockByKey(key, holder);
    }

}
