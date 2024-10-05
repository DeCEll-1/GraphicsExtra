package DeCell.GraphicsExtra.tests;

import DeCell.GraphicsExtra.tests.MGL.GE_MGLTests;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.input.InputEventAPI;

import java.util.List;

public class GE_EFS extends BaseEveryFrameCombatPlugin {


    public void processInputPreCoreControls(float amount, List<InputEventAPI> events) {
        for (InputEventAPI event : events) {
            if (event.isKeyboardEvent() && event.getEventChar() == 'b') {

                GE_MGLTests plugin = new GE_MGLTests();

                Global.getCombatEngine().addLayeredRenderingPlugin(plugin);

            }
        }
    }

}
