package silent.gems.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.core.util.PlayerHelper;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;

public class TorchBandolier extends ItemSG {

    public final static String AUTO_FILL_OFF = "AutoFillOff";
    public final static String AUTO_FILL_ON = "AutoFillOn";
    public final static int MAX_DAMAGE = 1024;

    public TorchBandolier() {

        setMaxDamage(MAX_DAMAGE);
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.tabTools);
        setUnlocalizedName(Names.TORCH_BANDOLIER);
    }

    public ItemStack absorbTorches(ItemStack stack, EntityPlayer player) {

        if (stack.stackTagCompound == null) {
            stack.stackTagCompound = new NBTTagCompound();
            stack.stackTagCompound.setBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL, true);
        }

        if (stack.stackTagCompound.getBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL)) {
            ItemStack torches;
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                torches = player.inventory.getStackInSlot(i);
                // Is the stack torches? Is this the best way? It's not pretty.
                if (torches != null && Item.getIdFromItem(torches.getItem()) == Block.getIdFromBlock(Blocks.torch)) {
                    int damage = stack.getItemDamage();

                    // Decrease damage value of torch bandolier, reduce stack size of torch stack.
                    if (damage - torches.stackSize < 0) {
                        stack.setItemDamage(0);
                        torches.stackSize -= damage;
                    }
                    else {
                        stack.setItemDamage(damage - torches.stackSize);
                        torches.stackSize = 0;
                    }

                    // If torch stack is empty, get rid of it.
                    if (torches.stackSize <= 0) {
                        player.inventory.setInventorySlotContents(i, null);
                    }
                }
            }
        }

        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

        if (stack.stackTagCompound == null) {
            resetTagCompound(stack);
        }

        // Item description
        list.add(LocalizationHelper.getItemDescription(itemName, 1));
        // Auto-fill mode
        if (stack.stackTagCompound.hasKey(Strings.TORCH_BANDOLIER_AUTO_FILL)
                && stack.stackTagCompound.getBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL)) {
            list.add(EnumChatFormatting.GREEN + LocalizationHelper.getOtherItemKey(itemName, AUTO_FILL_ON));
        }
        else {
            list.add(EnumChatFormatting.RED + LocalizationHelper.getOtherItemKey(itemName, AUTO_FILL_OFF));
        }
        if (stack.getItemDamage() < MAX_DAMAGE) {
            list.add((new StringBuilder()).append(EnumChatFormatting.YELLOW).append(MAX_DAMAGE - stack.getItemDamage()).append(" / ")
                    .append(MAX_DAMAGE).toString());
        }
        // How to use
        list.add(EnumChatFormatting.DARK_GRAY + LocalizationHelper.getItemDescription(itemName, 2));
    }

    @Override
    public void addRecipes() {

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 1, MAX_DAMAGE), true, new Object[] { "lll", "sgs", "lll", 'l',
                Items.leather, 's', "stickWood", 'g', Strings.ORE_DICT_GEM_BASIC }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 1, MAX_DAMAGE), true, new Object[] { "lll", "sgs", "lll", 'l',
            new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE), 's', "stickWood", 'g', Strings.ORE_DICT_GEM_BASIC }));
    }
    
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        
        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, MAX_DAMAGE));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        if (player.isSneaking()) {
            if (stack.stackTagCompound == null) {
                stack.stackTagCompound = new NBTTagCompound();
            }

            boolean autoFill = true;
            if (stack.stackTagCompound.hasKey(Strings.TORCH_BANDOLIER_AUTO_FILL)) {
                autoFill = !stack.stackTagCompound.getBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL);
            }

            stack.stackTagCompound.setBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL, autoFill);

            if (world.isRemote) {
                if (autoFill) {
                    PlayerHelper.addChatMessage(player,
                            EnumChatFormatting.GREEN + LocalizationHelper.getOtherItemKey(itemName, AUTO_FILL_ON));
                }
                else {
                    PlayerHelper.addChatMessage(player,
                            EnumChatFormatting.RED + LocalizationHelper.getOtherItemKey(itemName, AUTO_FILL_OFF));
                }
            }
        }

        return stack;
    }

    /**
     * Place a torch, if possible. Mostly the same code gem tools use to place blocks.
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,
            float hitZ) {

        if (stack.getItemDamage() == MAX_DAMAGE && !player.capabilities.isCreativeMode) {
            return false;
        }

        boolean used = false;

        ItemStack fakeTorchStack = new ItemStack(Blocks.torch);
        Item torch = fakeTorchStack.getItem();

        ForgeDirection d = ForgeDirection.VALID_DIRECTIONS[side];

        int px = x + d.offsetX;
        int py = y + d.offsetY;
        int pz = z + d.offsetZ;

        used = torch.onItemUse(fakeTorchStack, player, world, x, y, z, side, hitX, hitY, hitZ);
        if (used) {
            stack.damageItem(1, player);
        }

        return used;
    }

    private void resetTagCompound(ItemStack stack) {

        if (stack.stackTagCompound == null) {
            stack.stackTagCompound = new NBTTagCompound();
        }

        stack.stackTagCompound.setBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL, true);
    }
}
