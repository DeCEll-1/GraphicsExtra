package DeCell.GraphicsExtra.tests.MGL;

import DeCell.GraphicsExtra.Helpers.Helper;
import DeCell.GraphicsExtra.Render.ModernOpengl.MGLManager;
import DeCell.GraphicsExtra.Render.RenderMisc;
import cmu.gui.CMUKitUI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import org.json.JSONException;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

public class GE_MGLTests extends BaseCombatLayeredRenderingPlugin {
    private CombatEngineLayers CURRENT_LAYER = CombatEngineLayers.ABOVE_PLANETS;
    private boolean doOnce = true;
    private MGLManager manager = new MGLManager();

    private void init(ViewportAPI viewport) {

        if (!doOnce) return;
        doOnce = false;

        ShipAPI ship = Global.getCombatEngine().getPlayerShip();
        Vector2f center = null;
        try {
            center = Helper.getShipCenter(ship);
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }

        manager.getShader().loadVertexShader("gfx/test.vert").loadFragShader("gfx/test.frag").link();

        manager.getVertexObjects().init(
                new ArrayList<Vector3f>(
                        Arrays.asList(
                                new Vector3f(-100f, +100f, 0f),
                                new Vector3f(+100f, +100f, 0f),
                                new Vector3f(-100f, -100f, 0f),
                                new Vector3f(+100f, -100f, 0f)
                        )
                )
        ).insertColors(
                new ArrayList<Color>(
                        Arrays.asList(
                                Color.red,
                                Color.green,
                                Color.blue,
                                Color.white
                        )
                ),
                1
        );
    }

    @Override
    public void render(CombatEngineLayers layer, ViewportAPI viewport) {

        try {
            init(viewport);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        CMUKitUI.openGLForMisc(); // gl open

        glPushMatrix();

        ShipAPI ship = Global.getCombatEngine().getPlayerShip();

        manager.translate(RenderMisc.worldVectorToScreenVector(ship.getLocation(), viewport));

//        manager.rotate(new Vector4f(0, 0, 1, ship.getFacing() - 90f));

        manager.render(viewport);

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
