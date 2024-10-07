package DeCell.GraphicsExtra.Render;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class RenderMisc {

    public static List<Vector2f> Vector2f2MGLVector2f(List<Vector2f> worldVectorList) { // idk how this works but it works (probably)
        List<Vector2f> result = new ArrayList<>();
        for (Vector2f vector : worldVectorList) {
            result.add(new Vector2f(vector.getX() / Global.getSettings().getScreenWidth(), vector.getY() / Global.getSettings().getScreenHeight()));
//            result.add(NormaliseVector(vector));
        }
        return result;
    }

    public static List<Vector2f> WorldCoordToShaderCoord2f(List<Vector2f> worldVectorList) {
        // turns vector to 0-1
        List<Vector2f> result = new ArrayList<>();
        for (Vector2f vector : worldVectorList) {
            result.add(new Vector2f(vector.getX() / Global.getSettings().getScreenWidth(), vector.getY() / Global.getSettings().getScreenHeight()));
        }

        return result;
    }

    public static List<Vector3f> WorldCoordToShaderCoord3f(List<Vector3f> worldVectorList) {
        // turns vector to 0-1
        List<Vector3f> result = new ArrayList<>();
        for (Vector3f vector : worldVectorList) {
            result.add(new Vector3f(vector.getX() / Global.getSettings().getScreenWidth(), vector.getY() / Global.getSettings().getScreenHeight(), vector.getZ()));
        }

        return result;
    }

    public static Vector2f Vector2f2MGLVector2f(Vector2f vector) {
        // turns regular use coordinate space to MOGL one https://learnopengl.com/img/getting-started/ndc.png
        Vector2f result = new Vector2f(vector);

        float w = Global.getSettings().getScreenWidth();
        float h = Global.getSettings().getScreenHeight();

        result = new Vector2f(0.5f - result.getX() / w, 0.5f - result.getY() / h);

        result = new Vector2f(result.getX() * -2f, result.getY() * -2f); // yes i couldve done this at the upper line but im lazy

        return result;
    }

    public static Vector2f worldVectorToScreenVector(Vector2f worldVector, ViewportAPI viewport) {
        return new Vector2f(viewport.convertWorldXtoScreenX(worldVector.x), viewport.convertWorldYtoScreenY(worldVector.y));//screen vector
    }

    public static List<Vector2f> worldVectorToScreenVector(List<Vector2f> worldVectorList, ViewportAPI viewport) {

        List<Vector2f> screenVectorList = new ArrayList<>();

        for (Vector2f screenVector : worldVectorList) {
            screenVectorList.add(worldVectorToScreenVector(screenVector, viewport));
        }

        return screenVectorList;//screen vector
    }

    public static Vector2f screenVectorToWorldVector(Vector2f worldVector, ViewportAPI viewport) {
        return new Vector2f(viewport.convertScreenXToWorldX(worldVector.x), viewport.convertScreenYToWorldY(worldVector.y));//screen vector
    }

    public static List<Vector2f> screenVectorToWorldVector(List<Vector2f> worldVectorList, ViewportAPI viewport) {
        List<Vector2f> screenVectorList = new ArrayList<>();
        for (Vector2f screenVector : worldVectorList) {
            screenVectorList.add(screenVectorToWorldVector(screenVector, viewport));
        }
        return screenVectorList;//screen vector
    }

    /**
     * the order of the return list is <br>
     * 1---2<br>
     * &nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|<br>
     * 3---4<br>
     */
    public static List<Vector2f> LineToCorners(Vector2f from, Vector2f to, float width) {
        List<Vector2f> list = new ArrayList<>();
        float angle = VectorUtils.getAngle(from, to);
        Vector2f leftTop = MathUtils.getPointOnCircumference(to, width / 2, angle + 90);
        list.add(leftTop);
        Vector2f rightTop = MathUtils.getPointOnCircumference(to, width / 2, angle - 90);
        list.add(rightTop);
        Vector2f leftBottom = MathUtils.getPointOnCircumference(from, width / 2, angle + 90);
        list.add(leftBottom);
        Vector2f rightBottom = MathUtils.getPointOnCircumference(from, width / 2, angle - 90);
        list.add(rightBottom);
        return list;
    }

    public static void SetColor(Color color) {
        glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

    public static void Transform(Vector2f loc, ViewportAPI viewport) {
        float mult = viewport.getViewMult();
        glTranslatef(loc.x / mult, loc.y / mult, 0);
    }

    public static void Transform(Vector2f loc) {
        glTranslatef(loc.x, loc.y, 0);
    }

    public static List<Vector2f> GetTextureLocs(SpriteAPI tex, Vector2f center) {

        return new ArrayList<Vector2f>(
                Arrays.asList(
                        new Vector2f(center.x - tex.getWidth() / 2f, center.y + tex.getHeight() / 2f),
                        new Vector2f(center.x + tex.getWidth() / 2f, center.y + tex.getHeight() / 2f),
                        new Vector2f(center.x - tex.getWidth() / 2f, center.y - tex.getHeight() / 2f),
                        new Vector2f(center.x + tex.getWidth() / 2f, center.y - tex.getHeight() / 2f)
                )
        );
    }

    @Deprecated
    public static List<Vector2f> GetTextureLocs(SpriteAPI tex) {
        return new ArrayList<Vector2f>(
                Arrays.asList(
                        new Vector2f(-tex.getWidth() / 2, +tex.getHeight() / 2),
                        new Vector2f(+tex.getWidth() / 2, +tex.getHeight() / 2),
                        new Vector2f(-tex.getWidth() / 2, -tex.getHeight() / 2),
                        new Vector2f(+tex.getWidth() / 2, -tex.getHeight() / 2)
                )
        );
    }

    public static List<Vector4f> Colors2Vectors(List<Color> colors) {
        List<Vector4f> result = new ArrayList<>();
        for (Color color : colors) {
            result.add(
                    new Vector4f(
                            color.getRed() / 255f,
                            color.getGreen() / 255f,
                            color.getBlue() / 255f,
                            color.getAlpha() / 255f
                    )
            );
        }
        return result;
    }


}
