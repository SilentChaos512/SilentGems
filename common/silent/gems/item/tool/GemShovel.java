package silent.gems.item.tool;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.item.TorchBandolier;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import silent.gems.material.ModMaterials;


public class GemShovel extends ItemSpade {

    private final int gemId;
    private final boolean supercharged;
    private IIcon iconRod, iconHead;

    public static IIcon iconBlank = null;
    public static IIcon[] iconToolDeco = null;
    public static IIcon[] iconToolRod = null;
    public static IIcon[] iconToolHeadL = null;
    public static IIcon[] iconToolHeadM = null;
    public static IIcon[] iconToolHeadR = null;

    public GemShovel(ToolMaterial toolMaterial, int gemId, boolean supercharged) {

        super(toolMaterial);
        this.gemId = gemId;
        this.supercharged = supercharged;
        this.setMaxDamage(toolMaterial.getMaxUses());
        addRecipe(new ItemStack(this), gemId, supercharged);
    }

    public static void addRecipe(ItemStack tool, int gemId, boolean supercharged) {

        ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, gemId + (supercharged ? 16 : 0));

        // Fish tools
        if (gemId == ModMaterials.FISH_GEM_ID) {
            material = new ItemStack(Items.fish);
        }

        if (supercharged) {
            GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { " g ", " s ", " s ", 'g', material, 's',
                    new ItemStack(SRegistry.getItem(Names.CRAFTING_MATERIALS), 1, 0) }));
        }
        else {
            GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { " g ", " s ", " s ", 'g', material, 's', "stickWood" }));
        }
    }

    public int getGemId() {

        return gemId;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {

        if (pass == 0) {
            // Rod
            return iconRod;
        }
        else if (pass == 1) {
            // Rod decoration
            if (supercharged) {
                if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_DECO)) {
                    byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_DECO);
                    if (b >= 0 && b < iconToolDeco.length - 1) {
                        return iconToolDeco[b];
                    }
                }
                return iconToolDeco[iconToolDeco.length - 1];
            }
            return iconBlank;
        }
        else if (pass == 2) {
            // Rod wool
            if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_ROD)) {
                byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_ROD);
                if (b >= 0 && b < iconToolRod.length) {
                    return iconToolRod[b];
                }
            }
            return iconBlank;
        }
        else if (pass == 3) {
            // HeadM
            if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_HEAD_MIDDLE)) {
                byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_HEAD_MIDDLE);
                if (b >= 0 && b < iconToolHeadM.length) {
                    return iconToolHeadM[b];
                }
            }
            return iconHead;
        }
        else if (pass == 4) {
            // HeadL
            if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_HEAD_LEFT)) {
                byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_HEAD_LEFT);
                if (b >= 0 && b < iconToolHeadL.length) {
                    return iconToolHeadL[b];
                }
            }
            return iconBlank;
        }
        else if (pass == 5) {
            // HeadR
            if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_HEAD_RIGHT)) {
                byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_HEAD_RIGHT);
                if (b >= 0 && b < iconToolHeadR.length) {
                    return iconToolHeadR[b];
                }
            }
            return iconBlank;
        }
        else {
            return iconBlank;
        }
    }

    @Override
    public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

        ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, gemId + (supercharged ? 16 : 0));
        if (material.getItem() == stack2.getItem() && material.getItemDamage() == stack2.getItemDamage()) {
            return true;
        }
        else {
            return super.getIsRepairable(stack1, stack2);
        }
    }

    @Override
    public int getRenderPasses(int meta) {

        return 6;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return LocalizationHelper.TOOL_PREFIX + "Shovel" + gemId + (supercharged ? "Plus" : "");
    }
    
    @Override
    public boolean hasEffect(ItemStack stack, int pass) {

        return stack.isItemEnchanted() && pass == 5;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,
            float hitZ) {

        boolean used = false;
        int toolSlot = player.inventory.currentItem;
        int itemSlot = toolSlot + 1;
        ItemStack nextStack = null;

        if (toolSlot < 8) {
            nextStack = player.inventory.getStackInSlot(itemSlot);
            if (nextStack != null) {
                Item item = nextStack.getItem();
                if (item instanceof ItemBlock || item instanceof TorchBandolier) {
                    ForgeDirection d = ForgeDirection.VALID_DIRECTIONS[side];

                    int px = x + d.offsetX;
                    int py = y + d.offsetY;
                    int pz = z + d.offsetZ;
                    int playerX = (int) Math.floor(player.posX);
                    int playerY = (int) Math.floor(player.posY);
                    int playerZ = (int) Math.floor(player.posZ);

                    // Check for overlap with player, except for torches and torch bandolier
                    if (Item.getIdFromItem(item) != Block.getIdFromBlock(Blocks.torch) && item != SRegistry.getItem(Names.TORCH_BANDOLIER)
                            && px == playerX && (py == playerY || py == playerY + 1 || py == playerY - 1) && pz == playerZ) {
                        return false;
                    }

                    used = item.onItemUse(nextStack, player, world, x, y, z, side, hitX, hitY, hitZ);
                    if (nextStack.stackSize < 1) {
                        nextStack = null;
                        player.inventory.setInventorySlotContents(itemSlot, null);
                    }
                }
            }
        }

        return used;
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {

        String s = Strings.RESOURCE_PREFIX + "Shovel";

        if (supercharged) {
            iconRod = iconRegister.registerIcon(s + "_RodOrnate");
        }
        else {
            iconRod = iconRegister.registerIcon(s + "_RodNormal");
        }

        s += gemId;

        iconHead = iconRegister.registerIcon(s);

        // Deco
        String str = Strings.RESOURCE_PREFIX + "ToolDeco";
        iconToolDeco = new IIcon[EnumGem.all().length + 1];
        for (int i = 0; i < EnumGem.all().length; ++i) {
            iconToolDeco[i] = iconRegister.registerIcon(str + i);
        }
        iconToolDeco[iconToolDeco.length - 1] = iconRegister.registerIcon(str);

        // Rod
        str = Strings.RESOURCE_PREFIX + "RodWool";
        iconToolRod = new IIcon[16];
        for (int i = 0; i < 16; ++i) {
            iconToolRod[i] = iconRegister.registerIcon(str + i);
        }

        // Blank texture
        iconBlank = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + "Blank");

        // HeadL
        str = Strings.RESOURCE_PREFIX + "Shovel";
        iconToolHeadL = new IIcon[EnumGem.all().length];
        for (int i = 0; i < EnumGem.all().length; ++i) {
            iconToolHeadL[i] = iconRegister.registerIcon(str + i + "L");
        }
        // HeadM
        iconToolHeadM = new IIcon[EnumGem.all().length];
        for (int i = 0; i < EnumGem.all().length; ++i) {
            iconToolHeadM[i] = iconRegister.registerIcon(str + i);
        }
        // HeadR
        iconToolHeadR = new IIcon[EnumGem.all().length];
        for (int i = 0; i < EnumGem.all().length; ++i) {
            iconToolHeadR[i] = iconRegister.registerIcon(str + i + "R");
        }
    }

    @Override
    public boolean requiresMultipleRenderPasses() {

        return true;
    }
}
