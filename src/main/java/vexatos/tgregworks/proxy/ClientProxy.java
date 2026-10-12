package vexatos.tgregworks.proxy;

import com.ruling_0.materiallib.api.Material;

import tconstruct.library.client.TConstructClientRegistry;
import vexatos.tgregworks.TGregworks;

/**
 * @author Vexatos
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void addToolRenderMappings() {
        for (Material m : TGregworks.registry.toolMaterials) {
            TConstructClientRegistry
                .addMaterialRenderMapping(TGregworks.registry.matIDs.get(m), "tgregworks", "", true);
        }
    }

    public void registerRenderers() {
        // RenderingRegistry.registerEntityRenderingHandler(TGregDaggerEntity.class, new DaggerRenderCustom());
    }
}
