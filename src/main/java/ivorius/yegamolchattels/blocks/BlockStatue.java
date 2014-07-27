/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 **************************************************************************************************/

package ivorius.yegamolchattels.blocks;

import ivorius.ivtoolkit.blocks.IvBlockMultiblock;
import ivorius.ivtoolkit.blocks.IvTileEntityMultiBlock;
import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.items.ItemStatue;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStatue extends IvBlockMultiblock
{
    public BlockStatue(Material material)
    {
        super(material);
    }

    @Override
    public void parentBlockHarvestItem(World world, IvTileEntityMultiBlock tileEntity, int x, int y, int z, Block block, int metadata)
    {
        if (tileEntity instanceof TileEntityStatue)
        {
            Entity statueEntity = ((TileEntityStatue) tileEntity).getStatueEntity();

            if (statueEntity != null)
            {
                ItemStack stack = new ItemStack(Item.getItemFromBlock(YGCBlocks.statue));
                ItemStatue.setStatueEntity(stack, statueEntity);

                dropBlockAsItem(world, x, y, z, stack);
            }
        }
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        super.onNeighborBlockChange(world, x, y, z, block);

        if (world.isBlockIndirectlyGettingPowered(x, y, z))
        {
            IvTileEntityMultiBlock parent = getValidatedTotalParent(this, world, x, y, z);

            if (parent instanceof TileEntityStatue)
            {
                if (((TileEntityStatue) parent).letStatueComeAlive())
                    world.setBlock(x, y, z, Blocks.air, 0, 3);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        IvTileEntityMultiBlock parent = getValidatedTotalParent(this, world, x, y, z);

        if (parent instanceof TileEntityStatue)
        {
            TileEntityStatue tileEntityStatue = (TileEntityStatue) parent;
            if (tileEntityStatue.tryEquipping(player.getHeldItem()))
            {
                return true;
            }
            else
            {
                tileEntityStatue.dropEquipment();
                return true;
            }
        }

        return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        IvTileEntityMultiBlock parent = getValidatedTotalParent(this, blockAccess, x, y, z);

        if (parent instanceof TileEntityStatue)
        {
            TileEntityStatue tileEntityStatue = (TileEntityStatue) parent;
            TileEntityStatue.BlockFragment fragment = tileEntityStatue.getStatueBlock();
            return fragment.getBlock().getIcon(side, fragment.getMetadata());
        }

        return super.getIcon(blockAccess, x, y, z, side);
    }

    @Override
    public IIcon getIcon(int side, int meta)
    {
        return Blocks.stone.getIcon(0, 0);
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {

    }

    @Override
    public String getItemIconName()
    {
        return YeGamolChattels.textureBase + getTextureName();
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int i)
    {
        return new TileEntityStatue();
    }
}
