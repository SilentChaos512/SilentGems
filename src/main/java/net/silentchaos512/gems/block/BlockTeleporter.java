package net.silentchaos512.gems.block;

import com.google.common.collect.Lists;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileTeleporter;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.ChatHelper;
import net.silentchaos512.lib.util.DimensionalPosition;
import net.silentchaos512.wit.api.IWitHudInfo;

import java.util.List;

public class BlockTeleporter extends BlockGemSubtypes implements ITileEntityProvider, IAddRecipes, IWitHudInfo {
    private final boolean isAnchor;
    private final String blockName;

    // For anchors only.
    public BlockTeleporter(String name) {
        super(null, Material.IRON);
        isAnchor = true;
        this.blockName = name;
    }

    public BlockTeleporter(EnumGem.Set set, boolean isAnchor) {
        this(set, isAnchor, Names.TELEPORTER);
    }

    public BlockTeleporter(EnumGem.Set set, boolean isAnchor, String name) {
        super(set, Material.IRON);
        this.isAnchor = isAnchor;
        this.blockName = name;

        setHardness(15.0f);
        setResistance(2000.0f);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        ItemStack[] anyTeleporter = new ItemStack[]{
                new ItemStack(ModBlocks.teleporter, 1, OreDictionary.WILDCARD_VALUE),
                new ItemStack(ModBlocks.teleporterDark, 1, OreDictionary.WILDCARD_VALUE),
                new ItemStack(ModBlocks.teleporterLight, 1, OreDictionary.WILDCARD_VALUE)
        };
        int lastIndex = -1;

        for (int i = 0; i < 16; ++i) {
            EnumGem gem = getGem(i);
            recipes.addShapedOre(getBlockName() + i, new ItemStack(this, 2, i), "cec", " g ", "cec", 'c',
                    ModItems.craftingMaterial.chaosEssenceEnriched, 'e',
                    ModItems.craftingMaterial.enderEssence, 'g', gem.getBlockOreName());
            for (ItemStack stack : anyTeleporter) {
                recipes.addShapelessOre(blockName + "_" + (++lastIndex) + "_recolor", new ItemStack(this, 1, i), stack,
                        gem.getItemOreName());
            }
        }
    }

    @Override
    public List<String> getWitLines(IBlockState state, BlockPos pos, EntityPlayer player, boolean advanced) {
        TileEntity tile = player.world.getTileEntity(pos);
        if (!(tile instanceof TileTeleporter)) return null;

        TileTeleporter teleporter = (TileTeleporter) tile;
        DimensionalPosition destination = teleporter.getDestination();
        return Lists.newArrayList(destination != null ? destination.toString() : "null");
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        boolean holdingLinker = !heldItem.isEmpty() && heldItem.getItem() == ModItems.teleporterLinker;
        boolean holdingReturnHome = !heldItem.isEmpty() && heldItem.getItem() == ModItems.returnHomeCharm;

        if (world.isRemote)
            return (holdingLinker || holdingReturnHome) || !isAnchor;

        TileTeleporter tile = (TileTeleporter) world.getTileEntity(pos);
        if (tile == null) {
            SilentGems.logHelper.warn("Teleporter tile at {} not found!", pos);
            return false;
        }

        // Link teleporters with linker.
        if (holdingLinker) {
            return tile.linkTeleporters(player, world, pos, heldItem, hand);
        }

        // Link return home charm.
        if (holdingReturnHome) {
            return tile.linkReturnHomeCharm(player, world, pos, heldItem, hand);
        }

        // If this is an anchor, we're done.
        if (isAnchor) {
            return false;
        }

        // Destination set?
        if (!tile.isDestinationSet()) {
            ChatHelper.sendMessage(player, SilentGems.i18n.blockSubText(Names.TELEPORTER, "noDestination"));
            return true;
        }

        // Safety checks before teleporting:
        if (!tile.isDestinationSane(player)) {
            ChatHelper.sendMessage(player, SilentGems.i18n.blockSubText(Names.TELEPORTER, "notSane"));
            return true;
        }
        if (!tile.isDestinationSafe(player)) {
            ChatHelper.sendMessage(player, SilentGems.i18n.blockSubText(Names.TELEPORTER, "notSafe"));
            return true;
        }
        if (!tile.isDestinationAllowedIfDumb(player)) {
            ChatHelper.sendMessage(player, SilentGems.i18n.blockSubText(Names.TELEPORTER, "noReceiver"));
            return true;
        }

        // Check available charge, drain if there is enough.
        if (!tile.checkAndDrainChaos(player)) {
            return true;
        }

        // Teleport player
        tile.teleportEntityToDestination(player);

        // Play sounds
        float pitch = 0.7f + 0.3f * SilentGems.random.nextFloat();
        for (BlockPos p : new BlockPos[]{pos, tile.getDestination().toBlockPos()}) {
            world.playSound(null, p, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 1.0f,
                    pitch);
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileTeleporter(isAnchor);
    }

    @Override
    String getBlockName() {
        return nameForSet(this.getGemSet(), blockName);
    }
}
