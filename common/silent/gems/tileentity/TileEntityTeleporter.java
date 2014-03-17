package silent.gems.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;


public class TileEntityTeleporter extends TileEntity {
    
    public int destX, destY, destZ, destD;

    @Override
    public void writeToNBT(NBTTagCompound par1) {

        super.writeToNBT(par1);
        par1.setInteger("destX", destX);
        par1.setInteger("destY", destY);
        par1.setInteger("destZ", destZ);
        par1.setInteger("destD", destD);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1) {

        super.readFromNBT(par1);
        destX = par1.getInteger("destX");
        destY = par1.getInteger("destY");
        destZ = par1.getInteger("destZ");
        destD = par1.getInteger("destD");
    }
}
