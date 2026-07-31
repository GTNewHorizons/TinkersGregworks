package vexatos.tgregworks.integration.recipe.tconstruct;

import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;

import com.ruling_0.materiallib.api.Material;

import tconstruct.library.crafting.FluidType;

/**
 * @author Vexatos
 */
public class TGregFluidType extends FluidType {

    public final int matID;
    public final Material material;

    public TGregFluidType(Material m, Block block, int meta, int baseTemperature, Fluid fluid, boolean isToolpart,
        int matID) {
        super(block, meta, baseTemperature, fluid, isToolpart);
        this.material = m;
        this.matID = matID;
    }
}
