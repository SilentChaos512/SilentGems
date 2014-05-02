package silent.gems.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import silent.gems.configuration.Config;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.core.util.RecipeHelper;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FoodSG extends ItemSG {

    public final static String[] names = { Names.POTATO_STICK, Names.SUGAR_COOKIE, Names.SECRET_DONUT };
    public final static int[] foodLevel = { 8, 2, 6 };
    public final static float[] saturationLevel = { 0.8f, 0.4f, 0.8f };
    public final static boolean[] alwaysEdible = { false, true, false };
    
    //public final static HashMap<Integer, Float> secretDonutEffects = new HashMap<Integer, Float>();
    public final static ArrayList<SecretDonutEffect> secretDonutEffects = new ArrayList<SecretDonutEffect>();
    
    public final static int SECRET_DONUT_CHANCE = 20;

    public FoodSG(int id) {

        super(id);
        icons = new Icon[names.length];
        setMaxStackSize(64);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(CreativeTabs.tabFood);
        setUnlocalizedName(Names.FOOD);
        
        // Add secret donut effects.
        secretDonutEffects.clear();
        secretDonutEffects.add(new SecretDonutEffect(Potion.blindness.id, 0.5f));
        secretDonutEffects.add(new SecretDonutEffect(Potion.confusion.id, 0.75f));
        secretDonutEffects.add(new SecretDonutEffect(Potion.damageBoost.id, 1.5f));
        secretDonutEffects.add(new SecretDonutEffect(Potion.digSlowdown.id, 2.0f));
        secretDonutEffects.add(new SecretDonutEffect(Potion.digSpeed.id, 2.0f));
        secretDonutEffects.add(new SecretDonutEffect(Potion.fireResistance.id, 4.0f));
        secretDonutEffects.add(new SecretDonutEffect(Potion.hunger.id, 0.5f));
        secretDonutEffects.add(new SecretDonutEffect(Potion.invisibility.id, 0.33f));
        secretDonutEffects.add(new SecretDonutEffect(Potion.jump.id, 1.0f));
        secretDonutEffects.add(new SecretDonutEffect(Potion.moveSlowdown.id, 2.0f));
        secretDonutEffects.add(new SecretDonutEffect(Potion.moveSpeed.id, 2.0f));
        secretDonutEffects.add(new SecretDonutEffect(Potion.nightVision.id, 0.66f));
        secretDonutEffects.add(new SecretDonutEffect(Potion.poison.id, 0.5f));
        secretDonutEffects.add(new SecretDonutEffect(Potion.regeneration.id, 0.33f));
        secretDonutEffects.add(new SecretDonutEffect(Potion.resistance.id, 0.33f));
        secretDonutEffects.add(new SecretDonutEffect(Potion.waterBreathing.id, 2.0f));
        secretDonutEffects.add(new SecretDonutEffect(Potion.weakness.id, 1.5f));
        secretDonutEffects.add(new SecretDonutEffect(Potion.wither.id, 0.33f));
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {

        int d = stack.getItemDamage();
        --stack.stackSize;
        player.getFoodStats().addStats(foodLevel[d], saturationLevel[d]);
        world.playSoundAtEntity(player, "random.burp", 0.5f, world.rand.nextFloat() * 0.1f + 0.9f);

        if (!world.isRemote) {
            if (d == 0) {
                // Potato on a stick
                player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, Config.FOOD_SUPPORT_DURATION.value, 0, true));
            }
            else if (d == 1) {
                // Sugar cookie
                player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, Config.FOOD_SUPPORT_DURATION.value, 0, true));
                player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, Config.FOOD_SUPPORT_DURATION.value, 0, true));
            }
            else if (d == 2) {
                // Secret donut
                // 100% chance of first effect.
                addSecretDonutEffect(world, player);
                // Smaller chance of a second effect.
                if (world.rand.nextInt(100) < SECRET_DONUT_CHANCE) {
                    addSecretDonutEffect(world, player);
                    // Even smaller chance of third effect.
                    if (world.rand.nextInt(100) < SECRET_DONUT_CHANCE) {
                        addSecretDonutEffect(world, player);
                    }
                }
            }
        }

        return stack;
    }
    
    private void addSecretDonutEffect(World world, EntityPlayer player) {

        SecretDonutEffect effect = secretDonutEffects.get(world.rand.nextInt(secretDonutEffects.size()));
        player.addPotionEffect(new PotionEffect(effect.potionId, (int) (Config.FOOD_SUPPORT_DURATION.value * effect.durationMulti), 0, true));
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {

        if (stack.getItemDamage() == 1) {
            return 16;
        }
        else {
            return 32;
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {

        return EnumAction.eat;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        if (player.canEat(alwaysEdible[stack.getItemDamage()])) {
            player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        }

        return stack;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

        list.add(LocalizationHelper.getMessageText(Names.FOOD, EnumChatFormatting.DARK_GRAY));
        list.add(LocalizationHelper.getMessageText(names[stack.getItemDamage()]));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return getUnlocalizedName(names[stack.getItemDamage()]);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister) {

        for (int i = 0; i < names.length; ++i) {
            icons[i] = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + names[i]);
        }
    }

    @Override
    public void addRecipes() {

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 1, 0), " p", "s ", 'p', Item.bakedPotato, 's', "stickWood"));
        GameRegistry.addShapedRecipe(new ItemStack(this, 8, 1), " s ", "www", " s ", 's', Item.sugar, 'w', Item.wheat);
        RecipeHelper.addSurround(new ItemStack(this, 8, 2), new ItemStack(Block.mushroomRed), Item.wheat);
    }
    
    public static class SecretDonutEffect {
        
        public int potionId;
        public float durationMulti;
        
        public SecretDonutEffect(int potionId, float durationMulti) {
            
            this.potionId = potionId;
            this.durationMulti = durationMulti;
        }
    }
}
