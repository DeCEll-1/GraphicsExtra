package DeCell.GraphicsExtra.tests;

import DeCell.GraphicsExtra.tests.MGL.MGLTests;
import cmu.plugins.renderers.ImplosionParticleRenderer;
import cmu.plugins.renderers.PaintMaskRenderer;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.input.InputEventAPI;

import java.util.List;

public class EFS extends BaseEveryFrameCombatPlugin {


    public void processInputPreCoreControls(float amount, List<InputEventAPI> events) {
        for (InputEventAPI event : events) {
            if (event.isKeyboardEvent() && event.getEventChar() == 'b') {

                MGLTests plugin = new MGLTests();
//                ImplosionParticleRenderer plugin = new ImplosionParticleRenderer();

                plugin.init(null);

                Global.getCombatEngine().addLayeredRenderingPlugin(plugin);

            }
        }
    }

}
