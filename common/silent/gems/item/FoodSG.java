package silent.gems.item;

import java.util.List;

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
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FoodSG extends ItemSG {

    public final static String[] names = { Names.POTATO_STICK, Names.SUGAR_COOKIE };
    public final static int[] foodLevel = { 8, 2 };
    public final static float[] saturationLevel = { 0.8f, 0.4f };
    public final static boolean[] alwaysEdible = { false, true };

    public FoodSG(int id) {

        super(id);
        icons = new Icon[names.length];
        setMaxStackSize(64);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(CreativeTabs.tabFood);
        setUnlocalizedName(Names.FOOD);
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {

        int d = stack.getItemDamage();
        --stack.stackSize;
        player.getFoodStats().addStats(foodLevel[d], saturationLevel[d]);
        world.playSoundAtEntity(player, "random.burp", 0.5f, world.rand.nextFloat() * 0.1f + 0.9f);

        if (d == 0) {
            player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, Config.FOOD_SUPPORT_DURATION.value, 0, true));
        }
        else if (d == 1) {
            player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, Config.FOOD_SUPPORT_DURATION.value, 0, true));
            player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, Config.FOOD_SUPPORT_DURATION.value, 0, true));
        }

        return stack;
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
    }
}
