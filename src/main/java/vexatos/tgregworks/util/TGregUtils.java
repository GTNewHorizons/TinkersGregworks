package vexatos.tgregworks.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.material.LegacyNameDomain;
import gregtech.api.material.MaterialUtils;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.reference.PartTypes;

/**
 * @author Vexatos
 */
public class TGregUtils {

    public static NBTTagCompound getTagCompound(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return stack.getTagCompound();
        }
        NBTTagCompound data = new NBTTagCompound();
        stack.setTagCompound(data);
        return data;
    }

    public static NBTTagCompound getCompoundTag(NBTTagCompound tag, String key) {
        if (tag.hasKey(key)) {
            return tag.getCompoundTag(key);
        }
        NBTTagCompound data = new NBTTagCompound();
        tag.setTag(key, data);
        return data;
    }

    public static NBTTagCompound getCompoundTag(ItemStack stack, String key) {
        return getCompoundTag(getTagCompound(stack), key);
    }

    public static int getMaterialID(ItemStack stack) {
        NBTTagCompound data = getTagCompound(stack);
        if (!data.hasKey("material")) {
            return -1;
        }
        Integer matID = TGregworks.registry.matIDs.get(LegacyNameDomain.lookup(data.getString("material")));
        return matID != null ? matID : 0;
    }

    public static ItemStack newItemStack(Material m, PartTypes p, int amount) {
        ItemStack stack = new ItemStack(
            TGregworks.registry.toolParts.get(p),
            amount,
            TGregworks.registry.matIDs.get(m));
        NBTTagCompound data = TGregUtils.getTagCompound(stack);
        data.setString("material", MaterialUtils.internalName(m));
        stack.setTagCompound(data);
        return stack;
    }

    /// The material's molten fluid at `amount`, or null when its molten slot is unset. Not
    /// [MaterialUtils#molten]: its shape fallback answers for materials that
    /// [vexatos.tgregworks.integration.TGregRegistry#registerFluids] gives no tool-part fluid type.
    public static FluidStack getMolten(Material m, long amount) {
        Fluid molten = MaterialUtils.moltenOf(m);
        return molten == null ? null : new FluidStack(molten, (int) amount);
    }

    /// The material named by `stack`'s NBT, or null when it carries no material tag or an unregistered name.
    public static Material getMaterial(ItemStack stack) {
        NBTTagCompound data = getTagCompound(stack);
        return data.hasKey("material") ? LegacyNameDomain.lookup(data.getString("material")) : null;
    }

    /// The material's tint as `[r, g, b, a]`. Opaque white for a null material or one that declares no tint.
    public static short[] getRGBa(Material m) {
        short[] rgba = MaterialUtils.rgba(m);
        return rgba != null ? rgba : new short[] { 255, 255, 255, 255 };
    }
}
