package silent.gems.client.model;

import silent.gems.lib.Strings;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelSG {

    protected IModelCustom model;
    
    public ModelSG(String modelName) {
        
        model = AdvancedModelLoader.loadModel(modelName);
    }
    
    public ModelSG(String modelName, boolean addPrefix) {
        
        if (addPrefix) {
            model = AdvancedModelLoader.loadModel(Strings.MODEL_LOCATION + modelName);
        }
        else {
            model = AdvancedModelLoader.loadModel(modelName);
        }
    }
    
    public void render() {
        
        model.renderAll();
    }
}
