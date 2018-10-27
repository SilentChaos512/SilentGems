/*
 * Silent's Gems -- BlockGemLamp
 * Copyright (C) 2018 SilentChaos512
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 3
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.silentchaos512.gems.block;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemBlockMetaSubtypes;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.wit.api.IWitHudInfo;
import org.apache.commons.lang3.NotImplementedException;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class BlockGemLamp extends BlockGemSubtypes implements IAddRecipes, IWitHudInfo {
    public final String nameForLocalization;
    public final boolean lit;
    public final boolean inverted;

    public BlockGemLamp(EnumGem.Set set, boolean lit, boolean inverted) {
        super(set, Material.REDSTONE_LIGHT);
        this.nameForLocalization = nameForSet(set, Names.GEM_LAMP);

        this.lit = lit;
        this.inverted = inverted;

        setHardness(0.3f);
        setResistance(10.0f);
        setLightLevel(lit ? 1 : 0);
    }

    @Override
    String getBlockName() {
        return getBlockName(true);
    }

    private String getBlockName(boolean includeLampType) {
        if (includeLampType)
            return nameForSet(this.getGemSet(), Names.GEM_LAMP + (lit ? "Lit" : "") + (inverted ? "Inverted" : ""));
        return nameForSet(this.getGemSet(), Names.GEM_LAMP);
    }

    public static BlockGemLamp getLamp(EnumGem.Set set, boolean lit, boolean inverted) {
        if (set == EnumGem.Set.CLASSIC) {
            if (lit) {
                if (inverted) {
                    return ModBlocks.gemLampLitInverted;
                } else {
                    return ModBlocks.gemLampLit;
                }
            } else {
                if (inverted) {
                    return ModBlocks.gemLampInverted;
                } else {
                    return ModBlocks.gemLamp;
                }
            }
        } else if (set == EnumGem.Set.DARK) {
            if (lit) {
                if (inverted) {
                    return ModBlocks.gemLampLitInvertedDark;
                } else {
                    return ModBlocks.gemLampLitDark;
                }
            } else {
                if (inverted) {
                    return ModBlocks.gemLampInvertedDark;
                } else {
                    return ModBlocks.gemLampDark;
                }
            }
        } else if (set == EnumGem.Set.LIGHT) {
            if (lit) {
                if (inverted) {
                    return ModBlocks.gemLampLitInvertedLight;
                } else {
                    return ModBlocks.gemLampLitLight;
                }
            } else {
                if (inverted) {
                    return ModBlocks.gemLampInvertedLight;
                } else {
                    return ModBlocks.gemLampLight;
                }
            }
        } else {
            throw new NotImplementedException("Gem set \"" + set + "\" is not recognized!");
        }
    }

    public BlockGemLamp getLamp(boolean isLit) {
        return getLamp(this.getGemSet(), isLit, this.inverted);
    }

    private void setState(World world, BlockPos pos, BlockGemLamp block, EnumGem gem) {
        world.setBlockState(pos, block.getDefaultState().withProperty(EnumGem.VARIANT_GEM, gem), 2);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            boolean powered = world.isBlockPowered(pos);
            EnumGem gem = EnumGem.values()[getMetaFromState(state)];
            setState(world, pos, getLamp(inverted != powered), gem);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!world.isRemote) {
            boolean powered = world.isBlockPowered(pos);
            EnumGem gem = EnumGem.values()[getMetaFromState(state)];
            BlockGemLamp newBlock = getLamp(inverted != powered);

            if (inverted) {
                if (!lit && !powered) {
                    world.scheduleUpdate(pos, this, 4);
                } else if (lit && powered) {
                    setState(world, pos, newBlock, gem);
                }
            } else {
                if (lit && !powered) {
                    world.scheduleUpdate(pos, this, 4);
                } else if (!lit && powered) {
                    setState(world, pos, newBlock, gem);
                }
            }
        }
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (!world.isRemote) {
            boolean powered = world.isBlockPowered(pos);
            if (!powered) {
                EnumGem gem = EnumGem.values()[getMetaFromState(state)];
                setState(world, pos, getLamp(inverted != powered), gem);
            }
        }
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune) {
        BlockGemLamp block = getLamp(this.getGemSet(), inverted, inverted);
        return Item.getItemFromBlock(block);
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(getItemDropped(state, SilentGems.random, 0));
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        if (inverted)
            list.add(SilentGems.i18n.blockSubText(Names.GEM_LAMP, "inverted"));
    }

    @Override
    public List<String> getWitLines(IBlockState state, BlockPos pos, EntityPlayer player, boolean advanced) {
        String line = inverted ? SilentGems.i18n.blockSubText(Names.GEM_LAMP, "inverted") : "";
        line += lit ? (line.isEmpty() ? "" : ", ") + SilentGems.i18n.blockSubText(Names.GEM_LAMP, "lit") : "";
        return line.isEmpty() ? null : Lists.newArrayList(line);
    }

    public static class ItemBlock extends ItemBlockMetaSubtypes {
        public ItemBlock(BlockGemLamp block) {
            super(block);
        }

        @Nonnull
        @Override
        public String getItemStackDisplayName(@Nonnull ItemStack stack) {
            BlockGemLamp block = (BlockGemLamp) Block.getBlockFromItem(stack.getItem());
            String suffix = block.inverted
                    ? " (" + SilentGems.i18n.translate("tile." + SilentGems.MODID + "." + Names.GEM_LAMP + ".inverted") + ")"
                    : "";
            return super.getItemStackDisplayName(stack) + suffix;
        }

        @Nonnull
        @Override
        public String getTranslationKey(ItemStack stack) {
            return "tile." + SilentGems.MODID + "." + ((BlockGemLamp) this.block).getBlockName(false)
                    + (stack.getItemDamage() & 0xF);
        }
    }
}
