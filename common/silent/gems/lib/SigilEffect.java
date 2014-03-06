package silent.gems.lib;

import java.util.ArrayList;

import silent.gems.configuration.Config;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.core.util.LogHelper;
import silent.gems.core.util.NBTHelper;
import silent.gems.core.util.PlayerHelper;
import silent.gems.item.tool.Sigil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class SigilEffect {
    
    public final int id;
    public final String name;
    public final int maxPower;
    public final int maxSpeed;
    public final char rune;

    /**
     * All instantiated SigilEffects are added to this collection.
     */
    public static final ArrayList<SigilEffect> all = new ArrayList<SigilEffect>();

    /**
     * Increments each time a new SigilEffect is constructed and assigned to id.
     */
    private static int currentID = -1;

    public static final SigilEffect nada = new SigilEffect("Empty", 1, 1, '_');
    public static final SigilEffect fireball = new SigilEffect("Fireball", 4, 4, 'O');
    public static final SigilEffect icebolt = new SigilEffect("Icebolt", 4, 4, 'O');
    public static final SigilEffect lightning = new SigilEffect("Lightning", 4, 4, 'O');
    public static final SigilEffect earthquake = new SigilEffect("Earthquake", 4, 4, 'O');

    public static final SigilEffect teleport = new SigilEffect("Teleport", 1, 2, 'T');

    public static final SigilEffect healing = new SigilEffect("Healing", 3, 3, 'S');
    public static final SigilEffect regen = new SigilEffect("Regeneration", 2, 2, 'S');
    public static final SigilEffect remedy = new SigilEffect("Remedy", 1, 4, 'S');

    public static final SigilEffect swiftness = new SigilEffect("Swiftness", 2, 4, 'S');
    public static final SigilEffect jump = new SigilEffect("Jump", 2, 4, 'S');
    public static final SigilEffect haste = new SigilEffect("Haste", 2, 4, 'S');
    public static final SigilEffect strength = new SigilEffect("Strength", 2, 3, 'O');
    public static final SigilEffect resistance = new SigilEffect("Resistance", 2, 3, 'D');
    public static final SigilEffect fireResist = new SigilEffect("FireResistance", 2, 3, 'D');
    public static final SigilEffect cloak = new SigilEffect("Cloak", 2, 2, 'T');
    
    public static final String LOCALIZATION_PREFIX = "sigil.silentabyss:";

    public SigilEffect(String name, int maxPower, int maxSpeed, char rune) {

        this.id = ++currentID;
        this.name = name;
        this.maxPower = maxPower;
        this.maxSpeed = maxSpeed;
        this.rune = rune;

        all.add(this);
    }

    public boolean execute(ItemStack stack, World world, EntityPlayer player) {
        
        if (!(stack.getItem() instanceof Sigil)) {
            LogHelper.severe("SigilEffect.execute: received an ItemStack that is not a sigil!");
            return false;
        }
        
        Sigil sigil = (Sigil) stack.getItem();
        int power = sigil.getPowerLevel(stack);
        int time = Config.SIGIL_SUPPORT_DURATION.value * power;
        int damage = Config.SIGIL_PROJECTILE_DAMAGE.value;
        damage += damage * power / 4;
        int color = sigil.getColor(stack);
        
        if (id == fireball.id) {
//            EntityProjectileMagic p = new EntityProjectileMagic(world, player).setType(1).setColor(color).setDamage(damage);
//            world.spawnEntityInWorld(p);
//            world.playSoundAtEntity(player, "mob.ghast.fireball", 0.5f, 0.5f / (world.rand.nextFloat() * 0.4f + 0.8f));
        }
        else if (id == icebolt.id) {
//            EntityProjectileMagic p = new EntityProjectileMagic(world, player).setType(2).setColor(color).setDamage(damage);
//            world.spawnEntityInWorld(p);
        }
        else if (id == lightning.id) {
//            EntityProjectileMagic p = new EntityProjectileMagic(world, player).setType(3).setColor(color).setDamage(damage);
//            world.spawnEntityInWorld(p);
//            world.playSoundAtEntity(player, "ambient.weather.thunder", 0.5f, 1.6f / (world.rand.nextFloat() * 0.4f + 0.8f));
        }
        else if (id == earthquake.id) {
//            EntityProjectileMagic p = new EntityProjectileMagic(world, player).setType(4).setColor(color).setDamage(damage);
//            world.spawnEntityInWorld(p);
        }
        else if (id == teleport.id) {
            teleportPlayer(stack, player);
            world.playSoundAtEntity(player, "mob.endermen.portal", 1.0f, 1.0f);
        }
        else if (id == healing.id) {
            int k = 6 * power;
            player.heal(k);
        }
        else if (id == regen.id) {
            player.addPotionEffect(new PotionEffect(Potion.regeneration.id, time, 0, true));
        }
        else if (id == remedy.id) {
            player.curePotionEffects(new ItemStack(Item.bucketMilk));
        }
        else if (id == swiftness.id) {
            player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, time, power - 1, true));
        }
        else if (id == jump.id) {
            player.addPotionEffect(new PotionEffect(Potion.jump.id, time, power - 1, true));
        }
        else if (id == haste.id) {
            player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, time, power - 1, true));
        }
        else if (id == strength.id) {
            player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, time, power - 1, true));
        }
        else if (id == resistance.id) {
            player.addPotionEffect(new PotionEffect(Potion.resistance.id, time, power - 1, true));
        }
        else if (id == fireResist.id) {
            player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, time, 0, true));
        }
        else if (id == cloak.id) {
            player.addPotionEffect(new PotionEffect(Potion.invisibility.id, time, 0, true));
        }
        else {
            complain(this, player);
            return false;
        }
        
        return true;
    }
    
    private void complain(SigilEffect effect, EntityPlayer player) {
        
        String s = String.format("Sigil effect %s %i is not implemented! :(", effect.name, effect.id);
        player.addChatMessage(s);
        LogHelper.warning(s);
    }
    
    private void teleportPlayer(ItemStack stack, EntityPlayer player) {
        
        if (stack.stackTagCompound == null) {
            return;
        }
        
        NBTTagCompound tags = stack.stackTagCompound;
        if (!NBTHelper.hasValidXYZD(tags) || tags.getInteger("Y") <= 0) {
            LogHelper.warning("Invalid location for teleport effect");
            PlayerHelper.addChatMessage(player, Strings.TELEPORTER_INVALID, true);
            return;
        }
        
        int dx = tags.getInteger("X");
        int dy = tags.getInteger("Y");
        int dz = tags.getInteger("Z");
        int dd = tags.getInteger("D");
        
        // Dismount and teleport mount
        // This doesn't work very well.
        if (player.ridingEntity != null) {
            Entity mount = player.ridingEntity;
            player.mountEntity((Entity) null);
            if (dd != mount.dimension) {
                mount.travelToDimension(dd);
            }
            mount.setLocationAndAngles(dx + 0.5, dy + 1.0, dz + 0.5, mount.rotationYaw, mount.rotationPitch);
        }
        
        // Teleport player
        if (dd != player.dimension) {
            player.travelToDimension(dd);
        }
        player.setPositionAndUpdate(dx + 0.5, dy + 1.0, dz + 0.5);
    }
    
    public String getLocalizedName() {
        
        return LocalizationHelper.getLocalizedString(LOCALIZATION_PREFIX + name);
    }
}
