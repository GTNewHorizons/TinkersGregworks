package vexatos.tgregworks.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.ruling_0.materiallib.api.Material;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.material.LegacyNameDomain;
import gregtech.api.material.MaterialUtils;
import mantle.items.abstracts.CraftingItem;
import tconstruct.library.util.IToolPart;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.util.TGregUtils;

/**
 * @author Vexatos
 */
public class ItemTGregPart extends CraftingItem implements IToolPart {

    private PartTypes type;

    public ItemTGregPart(PartTypes p) {
        super(
            toolMaterialNames.toArray(new String[toolMaterialNames.size()]),
            buildTextureNames(p),
            "parts/",
            "tinker",
            TGregworks.tab);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.type = p;
    }

    public PartTypes getType() {
        return this.type;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Material m = TGregUtils.getMaterial(stack);
        String matName = m == null ? StatCollector.translateToLocal("tgregworks.materials.unknown")
            : MaterialUtils.localName(m);

        String name = StatCollector.translateToLocal(
            "tgregworks.toolpart." + type.getPartName()
                .replace(" ", "_")
                .toLowerCase());
        matName = name.replaceAll("%%material", matName);

        if (stack.getItemDamage() == 0) {
            matName = matName + StatCollector.translateToLocal("tgregworks.tool.deprecated");
        }

        return matName;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        Material m = TGregUtils.getMaterial(stack);
        return m == null ? "Unknown" : MaterialUtils.localName(m);
    }

    @Override
    public String getUnlocalizedName() {
        return type.getPartName();
    }

    private static String[] buildTextureNames(PartTypes p) {
        String[] names = new String[1];
        names[0] = p.getTextureName();
        return names;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[1];

        for (int i = 0; i < this.icons.length; ++i) {
            if (!(textureNames[i].equals(""))) {
                this.icons[i] = iconRegister.registerIcon(modTexPrefix + ":" + folder + textureNames[i]);
            }
        }
    }

    public static ArrayList<String> toolMaterialNames = TGregworks.registry.toolMaterialNames;

    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(Item b, CreativeTabs tab, List list) {
        for (Material m : TGregworks.registry.toolMaterials) {
            ItemStack stack = new ItemStack(b, 1, TGregworks.registry.matIDs.get(m));
            NBTTagCompound data = TGregUtils.getTagCompound(stack);
            data.setString("material", MaterialUtils.internalName(m));
            stack.setTagCompound(data);
            list.add(stack);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass) {
        // int meta = getDamage(stack);
        // NBTTagCompound data = getTagCompound(stack);
        // if(!data.hasKey("material")) {
        // return icons[meta];
        // }
        return icons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return icons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        NBTTagCompound data = TGregUtils.getTagCompound(stack);
        if (!data.hasKey("material")) {
            return super.getColorFromItemStack(stack, pass);
        }
        int colour;
        int[] rgba = toIntArray(getRGBa(stack));
        colour = (rgba[0] << 16) | (rgba[1] << 8) | (rgba[2]);
        return colour;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
        NBTTagCompound data = TGregUtils.getTagCompound(stack);
        if (!data.hasKey("material")) {
            return;
        }
        Material m = LegacyNameDomain.lookup(data.getString("material"));
        if (m != null) {
            Integer matID = TGregworks.registry.matIDs.get(m);
            if (matID != null && matID != stack.getItemDamage()) {
                stack.setItemDamage(matID);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean par4) {
        super.addInformation(stack, player, tooltip, par4);
        if (stack.getItemDamage() == 0) {
            tooltip.add(
                EnumChatFormatting.GRAY.toString() + EnumChatFormatting.ITALIC.toString()
                    + "Put this into your inventory to update it.");
        }
    }

    private int[] toIntArray(short[] r) {
        int[] i = new int[r.length];
        for (int j = 0; j < r.length; j++) {
            i[j] = r[j];
        }
        return i;
    }

    /**
     * @return the Color Modulation the Material is going to be rendered with.
     */
    public static short[] getRGBa(ItemStack stack) {
        return TGregUtils.getRGBa(TGregUtils.getMaterial(stack));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconIndex(ItemStack stack) {
        return getIcon(stack, 0);
    }

    @Override
    public int getMaterialID(ItemStack stack) {
        return TGregUtils.getMaterialID(stack);
    }
}
