package vexatos.tgregworks.integration.tictooltips;

import static squeek.tictooltips.helpers.ToolPartHelper.arrowHeads;
import static squeek.tictooltips.helpers.ToolPartHelper.bindings;
import static squeek.tictooltips.helpers.ToolPartHelper.bowLimbs;
import static squeek.tictooltips.helpers.ToolPartHelper.chisels;
import static squeek.tictooltips.helpers.ToolPartHelper.crossbowBodies;
import static squeek.tictooltips.helpers.ToolPartHelper.crossbowLimbs;
import static squeek.tictooltips.helpers.ToolPartHelper.fullWeaponGuards;
import static squeek.tictooltips.helpers.ToolPartHelper.plates;
import static squeek.tictooltips.helpers.ToolPartHelper.rods;
import static squeek.tictooltips.helpers.ToolPartHelper.shards;
import static squeek.tictooltips.helpers.ToolPartHelper.shurikenParts;
import static squeek.tictooltips.helpers.ToolPartHelper.toolHeads;
import static squeek.tictooltips.helpers.ToolPartHelper.toughBindings;
import static squeek.tictooltips.helpers.ToolPartHelper.weaponGuards;
import static squeek.tictooltips.helpers.ToolPartHelper.weaponHeads;
import static squeek.tictooltips.helpers.ToolPartHelper.weaponMiningHeads;
import static squeek.tictooltips.helpers.ToolPartHelper.weaponToolHeads;
import static vexatos.tgregworks.reference.PartTypes.ArrowHead;
import static vexatos.tgregworks.reference.PartTypes.AxeHead;
import static vexatos.tgregworks.reference.PartTypes.Binding;
import static vexatos.tgregworks.reference.PartTypes.BowLimb;
import static vexatos.tgregworks.reference.PartTypes.ChiselHead;
import static vexatos.tgregworks.reference.PartTypes.Chunk;
import static vexatos.tgregworks.reference.PartTypes.Crossbar;
import static vexatos.tgregworks.reference.PartTypes.CrossbowBody;
import static vexatos.tgregworks.reference.PartTypes.CrossbowLimb;
import static vexatos.tgregworks.reference.PartTypes.ExcavatorHead;
import static vexatos.tgregworks.reference.PartTypes.FrypanHead;
import static vexatos.tgregworks.reference.PartTypes.FullGuard;
import static vexatos.tgregworks.reference.PartTypes.HammerHead;
import static vexatos.tgregworks.reference.PartTypes.KnifeBlade;
import static vexatos.tgregworks.reference.PartTypes.LargeGuard;
import static vexatos.tgregworks.reference.PartTypes.LargePlate;
import static vexatos.tgregworks.reference.PartTypes.LargeSwordBlade;
import static vexatos.tgregworks.reference.PartTypes.LumberHead;
import static vexatos.tgregworks.reference.PartTypes.MediumGuard;
import static vexatos.tgregworks.reference.PartTypes.PickaxeHead;
import static vexatos.tgregworks.reference.PartTypes.ScytheHead;
import static vexatos.tgregworks.reference.PartTypes.ShovelHead;
import static vexatos.tgregworks.reference.PartTypes.Shuriken;
import static vexatos.tgregworks.reference.PartTypes.SignHead;
import static vexatos.tgregworks.reference.PartTypes.SwordBlade;
import static vexatos.tgregworks.reference.PartTypes.ToolRod;
import static vexatos.tgregworks.reference.PartTypes.ToughBind;
import static vexatos.tgregworks.reference.PartTypes.ToughRod;

import java.util.List;

import net.minecraft.item.Item;

import cpw.mods.fml.common.Optional;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.reference.Mods;
import vexatos.tgregworks.reference.PartTypes;

/**
 * @author Vexatos
 */
public class IntegrationTiCTooltips {

    @Optional.Method(modid = Mods.TiCTooltips)
    public void postInit() {
        TGregworks.log.info("Adding TiCTooltips integration...");
        try {
            add(toolHeads, PickaxeHead, ShovelHead, ExcavatorHead);
            add(weaponMiningHeads, HammerHead);
            add(weaponToolHeads, AxeHead, ScytheHead, LumberHead);
            add(weaponHeads, SwordBlade, LargeSwordBlade, KnifeBlade, FrypanHead, SignHead);
            add(weaponGuards, Crossbar, MediumGuard, LargeGuard);
            add(fullWeaponGuards, FullGuard);
            add(bindings, Binding);
            add(toughBindings, ToughBind);
            add(rods, ToolRod, ToughRod);
            add(plates, LargePlate);
            add(shards, Chunk);
            add(arrowHeads, ArrowHead);
            add(chisels, ChiselHead);
            add(shurikenParts, Shuriken);
            add(bowLimbs, BowLimb);
            add(crossbowLimbs, CrossbowLimb);
            add(crossbowBodies, CrossbowBody);
            TGregworks.log.info("Successfully added TiCTooltips integration.");
        } catch (Exception e) {
            TGregworks.log.error("Failed adding TiCTooltips integration.");
            e.printStackTrace();
        }
    }

    @Optional.Method(modid = Mods.TiCTooltips)
    private void add(List<Item> toolPartList, PartTypes... parts) {
        try {
            for (PartTypes part : parts) {
                toolPartList.add(TGregworks.registry.toolParts.get(part));
            }
        } catch (Exception e) {
            TGregworks.log.error("Failed adding parts of TiCTooltips integration.");
            e.printStackTrace();
        }
    }
}
