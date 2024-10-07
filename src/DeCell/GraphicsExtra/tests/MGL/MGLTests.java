package DeCell.GraphicsExtra.tests.MGL;

import DeCell.GraphicsExtra.Helpers.Helper;
import DeCell.GraphicsExtra.Render.ModernOpengl.MGLManager;
import DeCell.GraphicsExtra.Render.ModernOpengl.ScaleSetting;
import DeCell.GraphicsExtra.Render.RenderMisc;
import DeCell.GraphicsExtra.Statics;
import cmu.gui.CMUKitUI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.coreui.V;
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

public class MGLTests extends BaseCombatLayeredRenderingPlugin {
    private CombatEngineLayers CURRENT_LAYER = CombatEngineLayers.JUST_BELOW_WIDGETS;
    private boolean doOnce = true;
    private MGLManager manager = new MGLManager();

    private void init(ViewportAPI viewport) {

        if (!doOnce) return;
        doOnce = false;

        ShipAPI ship = Global.getCombatEngine().getPlayerShip();


        SpriteAPI tex = ship.getSpriteAPI();

        manager.getShader().loadVertexShader("gfx/default.vert").loadFragShader("gfx/test.frag").link();

        manager.getVertexObjects().init(
        ).AddVertexAttribute2f(
                0,
                RenderMisc.WorldCoordToShaderCoord2f(
                        new ArrayList<Vector2f>(
                                Arrays.asList(
                                        new Vector2f(-tex.getWidth(), +tex.getHeight()),
                                        new Vector2f(+tex.getWidth(), +tex.getHeight()),
                                        new Vector2f(-tex.getWidth(), -tex.getHeight()),
                                        new Vector2f(+tex.getWidth(), -tex.getHeight())
                                )
                        )
                )
        ).AddVertexAttribute2f(
                1,
                new ArrayList<Vector2f>(
                        Arrays.asList(
                                new Vector2f(0f, 1f),
                                new Vector2f(1f, 1f),
                                new Vector2f(0f, 0f),
                                new Vector2f(1f, 0f)
                        )
                )
        );

        manager.getVertexObjects().size = 4;


//        manager.SetScaleSetting(ScaleSetting.NO_SCALE);
    }

    @Override
    public void render(CombatEngineLayers layer, ViewportAPI viewport) {

        ShipAPI ship = Global.getCombatEngine().getPlayerShip();
        ship.getSpriteAPI().setColor(new Color(0,0,0,0));
        SpriteAPI texture = ship.getSpriteAPI();
        Vector2f center = null;
        try {
            center = Helper.getShipCenter(ship);
            init(viewport);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        CMUKitUI.openGLForMisc(); // gl open

        glPushMatrix();


        //region transformations and such
        manager.translate(RenderMisc.worldVectorToScreenVector(center, viewport));

        manager.rotate(new Vector4f(0, 0, 1, ship.getFacing() - 90f));
        //endregion

        manager.getShader().bind();

        manager.getShader()
                .SetTexture("tex", ship.getSpriteAPI(), ship.getSpriteAPI().getTextureId())
                .SetVector2f("textureShape", new Vector2f(texture.getWidth(), texture.getHeight()))
                .SetVector2f("nextPOTShape", new Vector2f(128, 128))
                .SetVector2f("level", new Vector2f(32, 32));

        manager.getShader().unbind();

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
