package net.silentchaos512.gems.network.message;

import java.util.UUID;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.lib.soul.SoulSkill;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.gems.network.Message;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.lib.util.StackHelper;

public class MessageSoulSync extends Message {

  public long uuidMost;
  public long uuidLeast;
  public int xp;
  public int level;
  public int ap;
  public String skillLearned;

  public MessageSoulSync() {

  }

  /**
   * @param uuid
   *          The UUID of the tool soul.
   * @param xp
   *          The soul's current XP.
   * @param level
   *          The soul's current level.
   * @param skillLearned
   *          The skill learned when a level up occurred, or null otherwise.
   */
  public MessageSoulSync(UUID uuid, int xp, int level, int ap, SoulSkill skillLearned) {

    this.uuidMost = uuid.getMostSignificantBits();
    this.uuidLeast = uuid.getLeastSignificantBits();
    this.xp = xp;
    this.level = level;
    this.ap = ap;
    this.skillLearned = skillLearned == null ? "" : skillLearned.id;
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
          soul.addOrLevelSkill(skill, StackHelper.empty(), null);
        }
      }
    });

    return null;
  }
}
