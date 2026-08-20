package vexatos.tgregworks.integration;

import net.minecraft.item.ItemStack;

import com.google.common.collect.HashMultimap;
import com.ruling_0.materiallib.api.Material;

import gregtech.api.util.GTOreDictUnificator;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.util.TGregUtils;

/**
 * @author Vexatos
 */
public class TGregRepairRegistry {

    public final HashMultimap<Material, RepairMaterial> repairMaterials = HashMultimap.create();

    public static abstract class RepairMaterial {

        public final int value;

        public RepairMaterial(int value) {
            this.value = value;
        }

        public abstract boolean matches(ItemStack input);
    }

    public static class ShardRepairMaterial extends RepairMaterial {

        public final Material m;

        public ShardRepairMaterial(Material m, int value) {
            super(value);
            this.m = m;
        }

        @Override
        public boolean matches(ItemStack input) {
            if (input.getItem() == TGregworks.registry.toolParts.get(PartTypes.Chunk)) {
                Material material = TGregUtils.getMaterial(input);
                if (material != null && material == this.m) {
                    return true;
                }
            }
            return false;
        }
    }

    private static class OreDictRepairMaterial extends RepairMaterial {

        private final String tag;

        public OreDictRepairMaterial(String tag, int value) {
            super(value);
            this.tag = tag;
        }

        @Override
        public boolean matches(ItemStack input) {
            return GTOreDictUnificator.isItemStackInstanceOf(input, tag);
        }
    }

    public void registerShardRepairMaterial(Material m, int value) {
        repairMaterials.put(m, new ShardRepairMaterial(m, value));
    }

    public void registerOreDictRepairMaterial(Material m, String tag, int value) {
        repairMaterials.put(m, new OreDictRepairMaterial(tag, value));
    }
}
