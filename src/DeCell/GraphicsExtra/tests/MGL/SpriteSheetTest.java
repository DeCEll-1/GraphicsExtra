package DeCell.GraphicsExtra.tests.MGL;

import DeCell.GraphicsExtra.Helpers.Helper;
import DeCell.GraphicsExtra.Render.ModernOpengl.MGLManager;
import DeCell.GraphicsExtra.Render.RenderMisc;
import DeCell.GraphicsExtra.Render.SpriteSheets.SpriteSheetData;
import cmu.gui.CMUKitUI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseCombatLayeredRenderingPlugin;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.json.JSONException;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

public class SpriteSheetTest extends BaseCombatLayeredRenderingPlugin {
    private CombatEngineLayers CURRENT_LAYER = CombatEngineLayers.JUST_BELOW_WIDGETS;
    private boolean doOnce = true;
    private MGLManager manager = new MGLManager();
    private String textureName = "";
    private SpriteSheetData SpriteSheet = new SpriteSheetData();

    private void init(ViewportAPI viewport) throws IOException, JSONException {

        if (!doOnce) return;
        doOnce = false;

        Global.getSettings().loadTexture("graphics/spritesheet.png");
        SpriteSheet = SpriteSheetData.GenerateFromJSON("graphics/spritesheet.json", Global.getSettings().getSprite("graphics/spritesheet.png"));

        ShipAPI ship = Global.getCombatEngine().getPlayerShip();

        SpriteAPI tex = ship.getSpriteAPI();
        String check = ship.getHullSpec().getHullId();
        switch (check) {
            case "gremlin":
                textureName = "gremlin.png";
                break;
            case "shade":
                textureName = "phase_assault_ff.png";
                break;
            case "afflictor":
                textureName = "phase_strike_ff.png";
                break;
            case "revenant":
                textureName = "revenant.png";
                break;
            case "ziggurat": // why the FUCK is ziggurats sprite name is zigguraut???
                textureName = "zigguraut.png";
                break;
            default:
                break;
        }

        manager.getShader().loadVertexShader("gfx/default.vert").loadFragShader("gfx/spritesheet.frag").link();

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
        ship.getSpriteAPI().setColor(new Color(0, 0, 0, 0));
        SpriteAPI texture = ship.getSpriteAPI();
        Vector2f offset = null;
        try {
            offset = Helper.getTextureOffset(ship);
            float angle = (float) Math.toRadians(ship.getFacing()-90f);
            float x2 = (float) (offset.x * Math.cos(angle) - offset.y * Math.sin(angle));
            float y2 = (float) (offset.x * Math.sin(angle) + offset.y * Math.cos(angle));
            offset = new Vector2f(x2, y2);
            init(viewport);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        CMUKitUI.openGLForMisc(); // gl open

        glPushMatrix();


        //region transformations and such
        manager.translate(RenderMisc.worldVectorToScreenVector(new Vector2f(ship.getLocation().x - offset.x, ship.getLocation().y - offset.y), viewport));

        manager.rotate(new Vector4f(0, 0, 1, ship.getFacing() - 90f));
        //endregion

        SpriteSheet.UpdateShader(manager.getShader(), textureName);

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