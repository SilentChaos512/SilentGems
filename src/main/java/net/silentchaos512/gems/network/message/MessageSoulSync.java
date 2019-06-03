package net.silentchaos512.gems.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.lib.soul.SoulSkill;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.gems.network.Message;
import net.silentchaos512.gems.util.SoulManager;

import java.util.UUID;

public class MessageSoulSync extends Message {
    public long uuidMost;
    public long uuidLeast;
    public int xp;
    public int level;
    public int ap;
    public String skillLearned;
    public int skillLevel;

    public MessageSoulSync() {
    }

    /**
     * @param uuid         The UUID of the tool soul.
     * @param xp           The soul's current XP.
     * @param level        The soul's current level.
     * @param skillLearned The skill learned when a level up occurred, or null otherwise.
     */
    public MessageSoulSync(UUID uuid, int xp, int level, int ap, SoulSkill skillLearned, int skillLevel) {
        this.uuidMost = uuid.getMostSignificantBits();
        this.uuidLeast = uuid.getLeastSignificantBits();
        this.xp = xp;
        this.level = level;
        this.ap = ap;
        this.skillLearned = skillLearned == null ? "" : skillLearned.id;
        this.skillLevel = skillLevel;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage handleMessage(MessageContext context) {
        ClientTickHandler.scheduledActions.add(() -> {
            UUID uuid = new UUID(uuidMost, uuidLeast);
            ToolSoul soul = SoulManager.getSoulByUuid(uuid);
            if (soul != null) {
                soul.setXp(xp);
                soul.setLevel(level);
                soul.setActionPoints(ap);
                if (skillLearned != null && !skillLearned.isEmpty()) {
                    SoulSkill skill = SoulSkill.getById(skillLearned);
                    soul.setSkillLevel(skill, skillLevel, ItemStack.EMPTY,
                            Minecraft.getMinecraft().player);
                }
            }
        });

        return null;
    }
}
