package DeCell.GraphicsExtra.tests;

import cmu.gui.CMUKitUI;
import com.fs.starfarer.api.combat.BaseCombatLayeredRenderingPlugin;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.ViewportAPI;

import java.util.EnumSet;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

public class GE_RenderingPreset extends BaseCombatLayeredRenderingPlugin {

    private CombatEngineLayers CURRENT_LAYER = CombatEngineLayers.ABOVE_PLANETS;

    @Override
    public void render(CombatEngineLayers layer, ViewportAPI viewport) {
        CMUKitUI.openGLForMisc(); // gl open

        glPushMatrix();



        glPopMatrix();

        CMUKitUI.closeGLForMisc(); // gl Close
    }

    public void ChangeRenderLayerInPlugin(CombatEngineLayers LAYER) {
        this.CURRENT_LAYER = LAYER;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public float getRenderRadius() {
        return Float.MAX_VALUE;
    }

    @Override
    public EnumSet<CombatEngineLayers> getActiveLayers() {
        return EnumSet.of(CURRENT_LAYER);
    }


}
