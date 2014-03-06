package silent.gems.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TorchBandolier extends ItemSG {

    public final static int SLOTS = 16;
    public final static int MAX_DAMAGE = SLOTS * 64;

    public TorchBandolier(int id) {

        super(id);
        setMaxDamage(MAX_DAMAGE);
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

        if (stack.stackTagCompound == null) {
            resetTagCompound(stack);
        }

        // Item description
        list.add(LocalizationHelper.getMessageText(Names.TORCH_BANDOLIER));
        // Auto-fill mode
        if (stack.stackTagCompound.hasKey(Strings.TORCH_BANDOLIER_AUTO_FILL)
                && stack.stackTagCompound.getBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL)) {
            list.add(LocalizationHelper.getMessageText(Names.TORCH_BANDOLIER + "AutoFillOn", EnumChatFormatting.GREEN));
        }
        else {
            list.add(LocalizationHelper.getMessageText(Names.TORCH_BANDOLIER + "AutoFillOff", EnumChatFormatting.RED));
        }
        if (stack.getItemDamage() < MAX_DAMAGE) {
            list.add((new StringBuilder()).append(EnumChatFormatting.YELLOW).append(MAX_DAMAGE - stack.getItemDamage()).append(" / ")
                    .append(MAX_DAMAGE).toString());
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return getUnlocalizedName(Names.TORCH_BANDOLIER);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister reg) {

        itemIcon = reg.registerIcon(Strings.RESOURCE_PREFIX + Names.TORCH_BANDOLIER);
    }

    private void resetTagCompound(ItemStack stack) {

        if (stack.stackTagCompound == null) {
            stack.stackTagCompound = new NBTTagCompound();
        }

        stack.stackTagCompound.setBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL, true);
    }

    /**
     * Absorb torches when player sneak-right-clicks.
     */
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
                    player.addChatMessage(LocalizationHelper.getMessageText(Names.TORCH_BANDOLIER + "AutoFillOn", EnumChatFormatting.GREEN));
                }
                else {
                    player.addChatMessage(LocalizationHelper.getMessageText(Names.TORCH_BANDOLIER + "AutoFillOff", EnumChatFormatting.RED));
                }
            }
        }

        return stack;
    }

    public ItemStack absorbTorches(ItemStack stack, EntityPlayer player) {

        ItemStack torches;
        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            torches = player.inventory.getStackInSlot(i);
            if (torches != null && torches.itemID == Block.torchWood.blockID) {
                int damage = stack.getItemDamage();

                // Decrease damage value of torch bandolier, reduce stack
                // size of torch stack.
                if (damage - torches.stackSize < 0) {
                    stack.damageItem(-damage, player);
                    torches.stackSize -= damage;
                }
                else {
                    stack.damageItem(-torches.stackSize, player);
                    torches.stackSize = 0;
                }

                // If torch stack is empty, get rid of it.
                if (torches.stackSize <= 0) {
                    player.inventory.setInventorySlotContents(i, null);
                }
            }
        }

        return stack;
    }

    /**
     * Place a torch, if possible. Mostly the same code Abyss tools use to place blocks.
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,
            float hitZ) {

        if (stack.getItemDamage() == MAX_DAMAGE) {
            return false;
        }

        boolean used = false;

        ItemStack fakeTorchStack = new ItemStack(Block.torchWood);
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

    @Override
    public void addRecipes() {

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 1, MAX_DAMAGE), true, new Object[] { "lll", "sgs", "lll", 'l',
                Item.leather, 's', "stickWood", 'g', Strings.ORE_DICT_GEM_BASIC }));
    }
}
