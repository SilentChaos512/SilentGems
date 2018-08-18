package net.silentchaos512.gems.guide.page;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.lib.soul.EnumSoulElement;
import net.silentchaos512.gems.lib.soul.SoulSkill;
import net.silentchaos512.lib.guidebook.GuideBook;
import net.silentchaos512.lib.guidebook.page.PageTextOnly;

public class PageSoulSkill extends PageTextOnly {
    private final SoulSkill skill;

    public PageSoulSkill(GuideBook book, SoulSkill skill) {
        super(book, 0);
        this.skill = skill;
    }

    @Override
    public String getInfoText() {
        String elementPreference = skill.favoredElements.length == 0 ? "none"
                : skill.isLockedToFavoredElements() ? "lockedTo" : "favors";
        String elements = "";
        for (EnumSoulElement elem : skill.favoredElements) {
            if (!elements.isEmpty())
                elements += ", ";
            elements += elem.getDisplayName();
        }
        elementPreference = book.i18n.translate(
                "guide.silentgems.soulSkillPage.elements." + elementPreference,
                (elementPreference.equals("none") ? new Object[0] : elements));

        String str = book.i18n.translate("guide", "soulSkillPage",
                skill.getLocalizedName(null, 0), Integer.toString(skill.maxLevel),
                Integer.toString(skill.apCost), elementPreference);

        str += "\n\n" + book.i18n.translate(this.getLocalizationKey());
        return doTextReplacements(str);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String getLocalizationKey() {
        return "guide." + book.getModId() + ".chapter." + this.chapter.getIdentifier() + "." + skill.id;
    }
}
