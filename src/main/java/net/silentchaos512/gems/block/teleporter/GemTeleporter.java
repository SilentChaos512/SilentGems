package net.silentchaos512.gems.block.teleporter;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ReturnHomeCharm;
import net.silentchaos512.gems.item.TeleporterLinker;
import net.silentchaos512.gems.lib.Gems;

import javax.annotation.Nullable;
import java.util.List;

public class GemTeleporter extends Block implements ITileEntityProvider {
    @Nullable final Gems gem;
    private final boolean isAnchor;

    public GemTeleporter(@Nullable Gems gem, boolean isAnchor) {
        super(Properties.create(Material.IRON)
                .hardnessAndResistance(15, 2000)
                .sound(SoundType.METAL));
        this.gem = gem;
        this.isAnchor = isAnchor;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new GemTeleporterTileEntity(this.isAnchor);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (this.gem != null) {
            tooltip.add(gem.getSet().getDisplayName());
        }
    }

    @Override
    public ITextComponent getNameTextComponent() {
        if (this.gem == null) return super.getNameTextComponent();
        return new TextComponentTranslation("block.silentgems.teleporter", this.gem.getDisplayName());
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        boolean holdingLinker = !heldItem.isEmpty() && heldItem.getItem() == TeleporterLinker.INSTANCE.get();
        boolean holdingReturnHome = !heldItem.isEmpty() && heldItem.getItem() == ReturnHomeCharm.INSTANCE.get();

        if (world.isRemote)
            return (holdingLinker || holdingReturnHome) || !isAnchor;

        GemTeleporterTileEntity tile = (GemTeleporterTileEntity) world.getTileEntity(pos);
        if (tile == null) {
            SilentGems.LOGGER.warn("Teleporter tile at {} not found!", pos);
            return false;
        }

        return tile.interact(player, heldItem, hand);
    }
}
