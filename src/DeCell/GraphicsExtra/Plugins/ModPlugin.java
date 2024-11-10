package DeCell.GraphicsExtra.Plugins;

import DeCell.GraphicsExtra.Statics;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import org.lwjgl.util.vector.Vector2f;

public class ModPlugin extends BaseModPlugin {

    public void onApplicationLoad() throws Exception {
        float width = Global.getSettings().getScreenWidth();
        float height = Global.getSettings().getScreenHeight();

        Statics.ScreenCenter = new Vector2f(width / 2f, height / 2f);
        Statics.ScreenShape = new Vector2f(width, height);
    }

    // some code

}
