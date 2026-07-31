package vexatos.tgregworks.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.ruling_0.materiallib.api.Material;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import tconstruct.TConstruct;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.CastingRecipe;
import tconstruct.library.crafting.FluidType;
import tconstruct.library.crafting.LiquidCasting;
import tconstruct.library.crafting.PatternBuilder;
import tconstruct.library.crafting.ToolBuilder;
import tconstruct.library.tools.DualMaterialToolPart;
import tconstruct.library.tools.ToolCore;
import tconstruct.library.tools.ToolMaterial;
import tconstruct.library.util.IToolPart;
import tconstruct.smeltery.TinkerSmeltery;
import tconstruct.tools.TinkerTools;
import tconstruct.util.config.PHConstruct;
import tconstruct.weaponry.TinkerWeaponry;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.integration.recipe.tconstruct.TGregAlternateBoltRecipe;
import vexatos.tgregworks.integration.recipe.tconstruct.TGregAmmoRecipe;
import vexatos.tgregworks.integration.recipe.tconstruct.TGregBowRecipe;
import vexatos.tgregworks.integration.recipe.tconstruct.TGregFluidType;
import vexatos.tgregworks.integration.recipe.tconstruct.TGregToolRecipe;
import vexatos.tgregworks.integration.smeltery.CastLegacy;
import vexatos.tgregworks.item.ItemTGregPart;
import vexatos.tgregworks.reference.Config;
import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.reference.Pattern.MetalPatterns;
import vexatos.tgregworks.util.TGregUtils;

/**
 * @author SlimeKnights, Vexatos
 */
public class TGregRecipeRegistry {

    private HashMap<PartTypes, ItemTGregPart> partMap = new HashMap<PartTypes, ItemTGregPart>();

    public boolean addReverseSmelting = false;
    public boolean addShardToIngotSmelting = false;
    public boolean addIngotToShard = false;
    public boolean addMoltenToShard = false;
    public boolean addShardToToolPart = false;
    public boolean addExtruderRecipes = false;
    public boolean addSolidifierRecipes = false;
    public boolean addFluidExtractorRecipes = false;
    public boolean addShardExtractorRecipes = false;
    public boolean addShardRepair = true;
    public boolean addIngotRepair = false;
    public boolean addGemToolPartRecipes = true;
    public boolean addCastExtruderRecipes = true;
    public boolean addCastSolidifierRecipes = false;
    public boolean useNonGTFluidsForBolts = true;
    public boolean useNonGTToolRodsForBolts = true;
    public float energyMultiplier = 0F;

    public void addGregTechPartRecipes() {
        addGemToolPartRecipes = TGregworks.config.getBoolean(
            "gemToolPartRecipes",
            Config.concat(Config.Category.Enable, Config.Category.Recipes),
            true,
            "Enable recipes for tool parts made of gems");
        energyMultiplier = TGregworks.config.getFloat(
            "energyUsageMultiplier",
            Config.concat(Config.Category.General),
            1F,
            0F,
            4500F,
            "Energy usage multiplier for the extruder and solidifier. Base EU/t is either 30 or 120");

        addReverseSmelting = TGregworks.config.getBoolean(
            "reverseSmelting",
            Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.AlloySmelter),
            true,
            "Enable smelting tool parts in an alloy smelter to get shards back");
        addShardToIngotSmelting = TGregworks.config.getBoolean(
            "shardToIngotSmelting",
            Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.AlloySmelter),
            true,
            "Enable smelting two shards into one ingot in an alloy smelter");

        addExtruderRecipes = TGregworks.config.getBoolean(
            "extruderRecipes",
            Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Extruder),
            true,
            "Enable tool part recipes in the extruder");
        addShardToToolPart = TGregworks.config.getBoolean(
            "shardToToolPartRecipe",
            Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Extruder),
            true,
            "Enable creating tool parts from shards in the extruder (if 'extruderRecipes' is enabled)");
        addIngotToShard = TGregworks.config.getBoolean(
            "ingotToShardRecipe",
            Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Extruder),
            true,
            "Enable creating shards from ingots in the extruder");
        addCastExtruderRecipes = TGregworks.config.getBoolean(
            "castExtruderRecipes",
            Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Extruder),
            CastLegacy.metalPattern != null,
            "Enable creating tool part casts in the extruder");

        addSolidifierRecipes = TGregworks.config.getBoolean(
            "solidifierRecipes",
            Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Solidifier),
            false,
            "Enable tool part recipes in the fluid solidifier");
        addMoltenToShard = TGregworks.config.getBoolean(
            "moltenToShardRecipe",
            Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Solidifier),
            false,
            "Enable creating shards from molten material in the fluid solidifier");
        addCastSolidifierRecipes = TGregworks.config.getBoolean(
            "castSolidifierRecipes",
            Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Solidifier),
            false,
            "Enable creating tool part casts in the fluid solidifier");
        useNonGTFluidsForBolts = TGregworks.config.getBoolean(
            "useNonGTFluidsForBolts",
            Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Solidifier),
            true,
            "Register Fluid Solidifier recipes for bolts with non-GT fluids.");
        useNonGTToolRodsForBolts = TGregworks.config.getBoolean(
            "useNonGTToolRodsForBolts",
            Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Solidifier),
            true,
            "Register Fluid Solidifier recipes for bolts with tool rods from non-GT materials.");

        addFluidExtractorRecipes = TGregworks.config.getBoolean(
            "fluidExtractorRecipes",
            Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Extractor),
            false,
            "Enable extracting the molten material out of tool parts in the fluid extractor");
        addShardExtractorRecipes = TGregworks.config.getBoolean(
            "shardExtractorRecipes",
            Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Extractor),
            false,
            "Enable extracting the molten material out of shards in the fluid extractor");

        // Make sure eu/t isn't 0 or the higher end materials eu/t does not exceed ultimate voltage
        if (energyMultiplier < 0 || (120 * energyMultiplier) > 524288) {
            TGregworks.log
                .error("Invalid energy multiplier found in config: " + energyMultiplier + ". Reverting back to 1.");
            energyMultiplier = 1;
        }
        for (Material m : TGregworks.registry.toolMaterials) {
            final int powerRequired = getPowerRequired(m);
            final int durability = MaterialUtils.durability(m);
            for (PartTypes p : PartTypes.VALUES) {
                ItemStack input = TGregUtils.newItemStack(m, p, 1);
                ItemStack pattern = p.getPatternItem();
                if (pattern != null) {
                    int price = p.getPrice();
                    // GregTech_API.sRecipeAdder.addAlloySmelterRecipe(GTOreDictUnificator.get(OrePrefixes.ingot, m,
                    // p.price), p.pattern, input, 80 * p.price, 30);
                    ItemStack stack = GTOreDictUnificator.get(
                        OrePrefixes.ingot,
                        m,
                        price % 2 != 0 ? (price / 2) + 1 : MathHelper.ceiling_double_int(price / 2D));
                    if (addGemToolPartRecipes && stack == null) {
                        stack = GTOreDictUnificator.get(
                            OrePrefixes.gem,
                            m,
                            price % 2 != 0 ? (price / 2) + 1 : MathHelper.ceiling_double_int(price / 2D));
                    }
                    if (stack != null) {
                        if (addExtruderRecipes) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(stack.copy(), pattern.copy())
                                .itemOutputs(input.copy())
                                .duration(Math.max(80, durability * price))
                                .eut(powerRequired)
                                .addTo(RecipeMaps.extruderRecipes);
                        }
                        {
                            FluidStack molten = TGregUtils.getMolten(m, (GTValues.L / 2) * p.getPrice());
                            if (molten != null && molten.getFluid() != null) {
                                if (addSolidifierRecipes) {
                                    GTValues.RA.stdBuilder()
                                        .itemInputs(pattern.copy())
                                        .fluidInputs(molten.copy())
                                        .itemOutputs(input.copy())
                                        .duration(Math.max(80, durability * price))
                                        .eut(powerRequired)
                                        .addTo(RecipeMaps.fluidSolidifierRecipes);
                                }
                                if (addFluidExtractorRecipes) {
                                    GTValues.RA.stdBuilder()
                                        .itemInputs(input.copy())
                                        .fluidOutputs(molten.copy())
                                        .duration(Math.max(80, durability * price))
                                        .eut(powerRequired)
                                        .addTo(RecipeMaps.fluidExtractionRecipes);
                                }
                            }
                            // GregTech_API.sRecipeAdder.addAlloySmelterRecipe(getChunk(m, p.price), p.pattern, input,
                            // 80 * p.price, 30);
                        }
                        stack = getChunk(m, price);
                        if (stack != null) {
                            if (addExtruderRecipes && addShardToToolPart) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(stack.copy(), pattern.copy())
                                    .itemOutputs(input.copy())
                                    .duration(80 + (durability * price))
                                    .eut(powerRequired)
                                    .addTo(RecipeMaps.extruderRecipes);
                            }
                            if (addReverseSmelting) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(input.copy(), new ItemStack(TGregworks.shardCast, 0, 0))
                                    .itemOutputs(stack.copy())
                                    .duration(80 + (durability * price))
                                    .eut(powerRequired)
                                    .addTo(RecipeMaps.alloySmelterRecipes);
                            }
                        }
                    }
                }
            }
            ItemStack stack = getChunk(m, 2);
            ItemStack ingotStack = GTOreDictUnificator.get(OrePrefixes.ingot, m, 1);
            if (addGemToolPartRecipes && ingotStack == null) {
                ingotStack = GTOreDictUnificator.get(OrePrefixes.gem, m, 1);
            }
            if (stack != null && ingotStack != null) {
                if (addIngotToShard) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(ingotStack, new ItemStack(TGregworks.shardCast, 0, 0))
                        .itemOutputs(stack.copy())
                        .duration(Math.max(160, durability))
                        .eut(powerRequired)
                        .addTo(RecipeMaps.extruderRecipes);
                }
                ItemStack halfStack = stack.copy();
                halfStack.stackSize = 1;
                FluidStack molten = TGregUtils.getMolten(m, GTValues.L / 2);
                if (molten != null && molten.getFluid() != null) {
                    if (addMoltenToShard) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(new ItemStack(TGregworks.shardCast, 0, 0))
                            .fluidInputs(molten.copy())
                            .itemOutputs(halfStack.copy())
                            .duration(Math.max(160, durability))
                            .eut(powerRequired)
                            .addTo(RecipeMaps.fluidSolidifierRecipes);
                    }
                    if (addShardExtractorRecipes) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(halfStack.copy())
                            .fluidOutputs(molten.copy())
                            .duration(Math.max(160, durability))
                            .eut(powerRequired)
                            .addTo(RecipeMaps.fluidExtractionRecipes);
                    }
                }
                if (addShardToIngotSmelting) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            stack.copy(),
                            new ItemStack(MetalPatterns.ingot.getPatternItem(), 0, MetalPatterns.ingot.ordinal()))
                        .itemOutputs(ingotStack.copy())
                        .duration(Math.max(160, durability))
                        .eut(powerRequired)
                        .addTo(RecipeMaps.alloySmelterRecipes);
                }
            }
        }

        if (TGregworks.config.getBoolean(
            "tinkersconstructcastrecipe",
            Config.concat(Config.Category.Enable, Config.Category.Recipes),
            true,
            "Enable the Shard Cast recipe using Tinkers' Construct shards")) {
            ItemStack brassstack = GTOreDictUnificator.get(OrePrefixes.plate, Materials.Brass, 1);
            if (TinkerTools.toolShard != null) {
                /*
                 * ArrayList list = new ArrayList(); TinkerTools.toolShard.getSubItems(TinkerTools.toolShard,
                 * TinkerTools.toolShard.getCreativeTab(), list); for(Object o : list) { if(o instanceof ItemStack) {
                 * GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes.add(new GT_Recipe(alustack, (ItemStack) o, 30, 800, new
                 * ItemStack(TGregworks.shardCast, 1, 0))); } }
                 */
                if (TinkerTools.blankPattern != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(TinkerTools.blankPattern, 1, 1),
                            new ItemStack(TinkerTools.toolShard, 1, TinkerTools.MaterialID.Obsidian))
                        .itemOutputs(new ItemStack(TGregworks.shardCast, 1, 0))
                        .duration(800)
                        .eut(Math.round(30 * energyMultiplier))
                        .addTo(RecipeMaps.extruderRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(TinkerTools.blankPattern, 1, 2),
                            new ItemStack(TinkerTools.toolShard, 1, TinkerTools.MaterialID.Obsidian))
                        .itemOutputs(new ItemStack(TGregworks.shardCast, 1, 0))
                        .duration(800)
                        .eut(Math.round(30 * energyMultiplier))
                        .addTo(RecipeMaps.extruderRecipes);
                }
                if (brassstack != null && !addCastExtruderRecipes) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            brassstack,
                            new ItemStack(TinkerTools.toolShard, 1, TinkerTools.MaterialID.Obsidian))
                        .itemOutputs(new ItemStack(TGregworks.shardCast, 1, 0))
                        .duration(800)
                        .eut(Math.round(30 * energyMultiplier))
                        .addTo(RecipeMaps.extruderRecipes);
                }
            }
        }
        if (TGregworks.config.getBoolean(
            "gregtechcastrecipe",
            Config.concat(Config.Category.Enable, Config.Category.Recipes),
            true,
            "Enable the GregTech style Shard Cast recipe")) {
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    new ItemStack(TGregworks.shardCast, 1, 0),
                    " CH",
                    " PF",
                    "   ",
                    'C',
                    ToolDictNames.craftingToolHardHammer.name(),
                    'H',
                    ToolDictNames.craftingToolKnife.name(),
                    'F',
                    ToolDictNames.craftingToolFile.name(),
                    'P',
                    "plateBrass"));
        }
    }

    private int getPowerRequired(Material m) {
        return Math.round(MaterialUtils.toolQuality(m) < 3 ? (30 * energyMultiplier) : (120 * energyMultiplier));
    }

    private ItemStack getChunk(Material m, int amount) {
        return TGregUtils.newItemStack(m, PartTypes.Chunk, amount);
    }

    public void registerRepairMaterials() {
        addShardRepair = TGregworks.config.getBoolean(
            "addShardRepair",
            Config.concat(Config.Category.Enable, Config.Category.Recipes),
            true,
            "Allow repairing TGregworks tools with shards");
        addIngotRepair = TGregworks.config.getBoolean(
            "addIngotRepair",
            Config.concat(Config.Category.Enable, Config.Category.Recipes),
            false,
            "Allow repairing TGregworks tools with ingots");
        for (Material m : TGregworks.registry.toolMaterials) {
            Integer matID = TGregworks.registry.matIDs.get(m);
            if (matID != null) {
                ToolMaterial mat = TConstructRegistry.getMaterial(matID);
                if (mat != null) {
                    if (addShardRepair) {
                        ItemStack shard = TGregUtils.newItemStack(m, PartTypes.Chunk, 1);
                        if (PatternBuilder.instance.materialSets.containsKey(mat.materialName)) {
                            PatternBuilder.instance.registerMaterial(shard, 1, mat.materialName);
                        } else {
                            ItemStack rod = TGregUtils.newItemStack(m, PartTypes.ToolRod, 1);

                            // register the material
                            PatternBuilder.instance.registerFullMaterial(shard, 1, mat.materialName, shard, rod, matID);
                        }
                        TGregworks.repair.registerShardRepairMaterial(m, 1);
                    }
                    if (addIngotRepair) {
                        ArrayList<ItemStack> ingots = GTOreDictUnificator.getOres(OrePrefixes.ingot, m);
                        if (!ingots.isEmpty()) {
                            TGregworks.repair.registerOreDictRepairMaterial(m, OrePrefixes.ingot.oreDictName(m), 2);
                        } else if (addGemToolPartRecipes) {
                            ingots.addAll(GTOreDictUnificator.getOres(OrePrefixes.gem, m));
                            TGregworks.repair.registerOreDictRepairMaterial(m, OrePrefixes.gem.oreDictName(m), 2);
                        }
                        for (ItemStack ingot : ingots) {
                            if (ingot != null && ingot.getItem() != null) {
                                if (PatternBuilder.instance.materialSets.containsKey(mat.materialName)) {
                                    PatternBuilder.instance.registerMaterial(ingot, 1, mat.materialName);
                                } else {
                                    ItemStack rod = TGregUtils.newItemStack(m, PartTypes.ToolRod, 1);

                                    // register the material
                                    PatternBuilder.instance
                                        .registerFullMaterial(ingot, 2, mat.materialName, ingot, rod, matID);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void registerBoltRecipes() {
        if (!TConstruct.pulsar.isPulseLoaded("Tinkers' Weaponry")) {
            return;
        }
        if (PHConstruct.alternativeBoltRecipe) {
            GameRegistry.addRecipe(new TGregAlternateBoltRecipe());
        }
        if (!TConstruct.pulsar.isPulseLoaded("Tinkers' Smeltery")) {
            return;
        }
        LiquidCasting tb = TConstructRegistry.getTableCasting();

        // any fluid that is a toolpart material can be used
        for (Map.Entry<String, FluidType> fluidEntry : FluidType.fluidTypes.entrySet()) {
            if (!fluidEntry.getValue().isToolpart) {
                continue;
            }
            FluidStack liquid = new FluidStack(fluidEntry.getValue().fluid, TConstruct.ingotLiquidValue);
            int arrowheadMaterialID;
            if (fluidEntry.getValue() instanceof TGregFluidType) {
                arrowheadMaterialID = ((TGregFluidType) fluidEntry.getValue()).matID;
            } else {
                // get a casting recipe for it D:
                CastingRecipe recipe = tb.getCastingRecipe(liquid, new ItemStack(TinkerSmeltery.metalPattern, 1, 2)); // pickaxe

                if (recipe == null) {
                    continue;
                } else {
                    arrowheadMaterialID = recipe.getResult()
                        .getItemDamage();
                }
            }
            for (Integer toolRodMaterialID : TConstructRegistry.toolMaterials.keySet()) {
                ItemStack toolRod;
                if (TGregworks.registry.materialIDMap.containsKey(toolRodMaterialID)) {
                    toolRod = TGregUtils
                        .newItemStack(TGregworks.registry.materialIDMap.get(toolRodMaterialID), PartTypes.ToolRod, 1);
                } else {
                    toolRod = new ItemStack(TinkerTools.toolRod, 1, toolRodMaterialID);
                    if (((IToolPart) TinkerTools.toolRod).getMaterialID(toolRod) == -1) {
                        continue;
                    }
                }
                if (!TGregworks.registry.materialIDMap.containsKey(toolRodMaterialID)
                    && !(fluidEntry.getValue() instanceof TGregFluidType)) {
                    continue;
                }
                if ((useNonGTToolRodsForBolts || TGregworks.registry.materialIDMap.containsKey(toolRodMaterialID))
                    && (useNonGTFluidsForBolts || fluidEntry.getValue() instanceof TGregFluidType)) {
                    this.addBoltRecipe(toolRod, liquid.copy(), toolRodMaterialID, arrowheadMaterialID);
                }
            }
        }

        // Remove broken dynamically added recipes.
        ArrayList<CastingRecipe> castingRecipes = TConstructRegistry.getTableCasting()
            .getCastingRecipes();
        ArrayList<CastingRecipe> toRemove = new ArrayList<CastingRecipe>();
        for (CastingRecipe cr : castingRecipes) {
            if (cr != null && cr.cast != null
                && cr.cast.getItem() == TinkerTools.toolRod
                && TGregworks.registry.materialIDMap
                    .containsKey(((IToolPart) TinkerTools.toolRod).getMaterialID(cr.cast))) {
                toRemove.add(cr);
            }
        }
        castingRecipes.removeAll(toRemove);
    }

    private int getPowerRequired(ToolMaterial toolMaterial) {
        return Math.round(toolMaterial.harvestLevel < 3 ? (30 * energyMultiplier) : (120 * energyMultiplier));
    }

    private void addBoltRecipe(ItemStack toolRod, FluidStack fluid, int toolRodMaterialID, int arrowheadMaterialID) {
        ToolMaterial toolRodMaterial = TConstructRegistry.toolMaterials.get(toolRodMaterialID);
        ToolMaterial arrowheadMaterial = TConstructRegistry.toolMaterials.get(arrowheadMaterialID);
        if (toolRodMaterial != null && arrowheadMaterial != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(toolRod)
                .fluidInputs(fluid)
                .itemOutputs(
                    DualMaterialToolPart
                        .createDualMaterial(TinkerWeaponry.partBolt, toolRodMaterialID, arrowheadMaterialID))
                .duration(80 + (toolRodMaterial.durability + arrowheadMaterial.durability) * 2)
                .eut(Math.max(getPowerRequired(toolRodMaterial), getPowerRequired(arrowheadMaterial)))
                .addTo(RecipeMaps.fluidSolidifierRecipes);
        }
    }

    public void addRecipesForToolBuilder() {

        for (PartTypes p : PartTypes.VALUES) {
            partMap.put(p, TGregworks.registry.toolParts.get(p));
        }

        addTGregToolRecipe(TinkerTools.pickaxe, PartTypes.PickaxeHead, PartTypes.ToolRod, PartTypes.Binding);
        addTGregToolRecipe(TinkerTools.shovel, PartTypes.ShovelHead, PartTypes.ToolRod);
        addTGregToolRecipe(TinkerTools.hatchet, PartTypes.AxeHead, PartTypes.ToolRod);
        addTGregToolRecipe(TinkerTools.mattock, PartTypes.AxeHead, PartTypes.ToolRod, PartTypes.ShovelHead);
        addTGregToolRecipe(TinkerTools.chisel, PartTypes.ChiselHead, PartTypes.ToolRod);

        addTGregToolRecipe(TinkerTools.broadsword, PartTypes.SwordBlade, PartTypes.ToolRod, PartTypes.LargeGuard);
        addTGregToolRecipe(TinkerTools.longsword, PartTypes.SwordBlade, PartTypes.ToolRod, PartTypes.MediumGuard);
        addTGregToolRecipe(TinkerTools.rapier, PartTypes.SwordBlade, PartTypes.ToolRod, PartTypes.Crossbar);
        addTGregToolRecipe(TinkerTools.dagger, PartTypes.KnifeBlade, PartTypes.ToolRod, PartTypes.Crossbar);
        addTGregToolRecipe(TinkerTools.cutlass, PartTypes.SwordBlade, PartTypes.ToolRod, PartTypes.FullGuard);
        addTGregToolRecipe(TinkerTools.frypan, PartTypes.FrypanHead, PartTypes.ToolRod);
        addTGregToolRecipe(TinkerTools.battlesign, PartTypes.SignHead, PartTypes.ToolRod);

        addTGregToolRecipe(
            TinkerTools.scythe,
            PartTypes.ScytheHead,
            PartTypes.ToughRod,
            PartTypes.ToughBind,
            PartTypes.ToughRod);
        addTGregToolRecipe(
            TinkerTools.lumberaxe,
            PartTypes.LumberHead,
            PartTypes.ToughRod,
            PartTypes.LargePlate,
            PartTypes.ToughBind);
        addTGregToolRecipe(
            TinkerTools.cleaver,
            PartTypes.LargeSwordBlade,
            PartTypes.ToughRod,
            PartTypes.LargePlate,
            PartTypes.ToughRod);
        addTGregToolRecipe(
            TinkerTools.excavator,
            PartTypes.ExcavatorHead,
            PartTypes.ToughRod,
            PartTypes.LargePlate,
            PartTypes.ToughBind);
        addTGregToolRecipe(
            TinkerTools.hammer,
            PartTypes.HammerHead,
            PartTypes.ToughRod,
            PartTypes.LargePlate,
            PartTypes.LargePlate);
        addTGregToolRecipe(
            TinkerTools.battleaxe,
            PartTypes.LumberHead,
            PartTypes.ToughRod,
            PartTypes.LumberHead,
            PartTypes.ToughBind);

        if (TConstruct.pulsar.isPulseLoaded("Tinkers' Weaponry")) {
            ToolBuilder.addCustomToolRecipe(
                new TGregBowRecipe(
                    partMap.get(PartTypes.BowLimb),
                    TinkerWeaponry.bowstring,
                    partMap.get(PartTypes.BowLimb),
                    TinkerWeaponry.shortbow));
            ToolBuilder.addCustomToolRecipe(
                new TGregBowRecipe(
                    partMap.get(PartTypes.BowLimb),
                    TinkerWeaponry.bowstring,
                    partMap.get(PartTypes.BowLimb),
                    partMap.get(PartTypes.LargePlate),
                    TinkerWeaponry.longbow));
            ToolBuilder.addCustomToolRecipe(
                new TGregBowRecipe(
                    partMap.get(PartTypes.CrossbowLimb),
                    partMap.get(PartTypes.CrossbowBody),
                    TinkerWeaponry.bowstring,
                    partMap.get(PartTypes.ToughBind),
                    TinkerWeaponry.crossbow));

            {
                TGregAmmoRecipe arrowRecipe = new TGregAmmoRecipe(
                    partMap.get(PartTypes.ArrowHead),
                    partMap.get(PartTypes.ToolRod),
                    TinkerWeaponry.fletching,
                    TinkerWeaponry.arrowAmmo);
                arrowRecipe.addHandleItem(TinkerWeaponry.partArrowShaft);
                ToolBuilder.addCustomToolRecipe(arrowRecipe);
            }
            ToolBuilder.addCustomToolRecipe(
                new TGregToolRecipe(
                    partMap.get(PartTypes.Shuriken),
                    partMap.get(PartTypes.Shuriken),
                    partMap.get(PartTypes.Shuriken),
                    partMap.get(PartTypes.Shuriken),
                    TinkerWeaponry.shuriken));
            addTGregToolRecipe(TinkerWeaponry.throwingknife, PartTypes.KnifeBlade, PartTypes.ToolRod);
            addTGregToolRecipe(TinkerWeaponry.javelin, PartTypes.ArrowHead, PartTypes.ToughRod, PartTypes.ToughRod);
        }
    }

    private void addTGregToolRecipe(ToolCore output, PartTypes head, PartTypes handle) {
        ToolBuilder.addCustomToolRecipe(new TGregToolRecipe(partMap.get(head), partMap.get(handle), output));
    }

    private void addTGregToolRecipe(ToolCore output, PartTypes head, PartTypes handle, PartTypes accessory) {
        ToolBuilder.addCustomToolRecipe(
            new TGregToolRecipe(partMap.get(head), partMap.get(handle), partMap.get(accessory), output));
    }

    private void addTGregToolRecipe(ToolCore output, PartTypes head, PartTypes handle, PartTypes accessory,
        PartTypes extra) {
        ToolBuilder.addCustomToolRecipe(
            new TGregToolRecipe(
                partMap.get(head),
                partMap.get(handle),
                partMap.get(accessory),
                partMap.get(extra),
                output));
    }

    public void registerCastRecipes() {
        Material[] castingMaterials = new Material[] { Materials.Brass, Materials.Gold };

        if (addCastExtruderRecipes) {
            for (PartTypes p : PartTypes.VALUES) {
                ItemStack stack = p.getPatternItem();
                if (stack != null && stack.getItem() != null) {
                    for (Material m : castingMaterials) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                GTOreDictUnificator.get(OrePrefixes.plate, m, 1),
                                new ItemStack(p.getCounterpart(), 0, Short.MAX_VALUE))
                            .itemOutputs(stack.copy())
                            .duration(800)
                            .eut(getPowerRequired(m))
                            .addTo(RecipeMaps.extruderRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                GTOreDictUnificator.get(OrePrefixes.plate, m, 1),
                                new ItemStack(TGregworks.registry.toolParts.get(p), 0, Short.MAX_VALUE))
                            .itemOutputs(stack.copy())
                            .duration(800)
                            .eut(getPowerRequired(m))
                            .addTo(RecipeMaps.extruderRecipes);
                    }
                }
            }
            for (Material m : castingMaterials) {
                FluidStack molten = TGregUtils.getMolten(m, GTValues.L);
                if (molten != null && molten.getFluid() != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTOreDictUnificator.get(OrePrefixes.plate, m, 1),
                            new ItemStack(TGregworks.registry.toolParts.get(PartTypes.Chunk), 0, Short.MAX_VALUE))
                        .itemOutputs(new ItemStack(TGregworks.shardCast, 1, 0))
                        .duration(800)
                        .eut(getPowerRequired(m))
                        .addTo(RecipeMaps.extruderRecipes);
                }
            }
        }
        if (addCastSolidifierRecipes) {
            for (PartTypes p : PartTypes.VALUES) {
                ItemStack stack = p.getPatternItem();
                if (stack != null && stack.getItem() != null && p.getCounterpart() != null) {
                    stack.stackSize = 1;
                    for (Material m : castingMaterials) {
                        FluidStack molten = TGregUtils.getMolten(m, GTValues.L);
                        if (molten != null && molten.getFluid() != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(new ItemStack(p.getCounterpart(), 0, Short.MAX_VALUE))
                                .fluidInputs(molten.copy())
                                .itemOutputs(stack.copy())
                                .duration(800)
                                .eut(getPowerRequired(m))
                                .addTo(RecipeMaps.fluidSolidifierRecipes);
                            GTValues.RA.stdBuilder()
                                .itemInputs(new ItemStack(TGregworks.registry.toolParts.get(p), 0, Short.MAX_VALUE))
                                .fluidInputs(molten.copy())
                                .itemOutputs(stack.copy())
                                .duration(800)
                                .eut(getPowerRequired(m))
                                .addTo(RecipeMaps.fluidSolidifierRecipes);
                        }
                    }
                }
            }
            for (Material m : castingMaterials) {
                FluidStack molten = TGregUtils.getMolten(m, GTValues.L);
                if (molten != null && molten.getFluid() != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(TGregworks.registry.toolParts.get(PartTypes.Chunk), 0, Short.MAX_VALUE))
                        .fluidInputs(molten.copy())
                        .itemOutputs(new ItemStack(TGregworks.shardCast, 1, 0))
                        .duration(800)
                        .eut(getPowerRequired(m))
                        .addTo(RecipeMaps.fluidSolidifierRecipes);
                }
            }
        }
    }
}
