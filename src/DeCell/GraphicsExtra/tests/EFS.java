package DeCell.GraphicsExtra.tests;

import DeCell.GraphicsExtra.Statics;
import DeCell.GraphicsExtra.tests.MGL.SpriteSheetTest;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.input.InputEventAPI;

import java.util.List;

public class EFS extends BaseEveryFrameCombatPlugin {
    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        if (Global.getCombatEngine().isPaused()) return;
        Statics.t += amount;
    }

    @Override
    public void processInputPreCoreControls(float amount, List<InputEventAPI> events) {
        for (InputEventAPI event : events) {
            if (event.isKeyboardEvent() && event.getEventChar() == 'b') {

                if (!Global.getSettings().getBoolean("GE_DebugMode")){
                    return;
                }

                SpriteSheetTest plugin = new SpriteSheetTest();
//                ImplosionParticleRenderer plugin = new ImplosionParticleRenderer();

                plugin.init(null);

                Global.getCombatEngine().addLayeredRenderingPlugin(plugin);

            }
        }
    }

}
