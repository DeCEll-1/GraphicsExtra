package DeCell.GraphicsExtra.Render;

import com.fs.starfarer.api.combat.ViewportAPI;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Matrixes {

    public static Matrix4f getModelView(ViewportAPI viewport, Vector2f loc, Vector4f angle) {
        float viewMult = viewport.getViewMult();
        Matrix4f matrix = new Matrix4f();

        matrix.setIdentity();

        //view
        matrix.translate(new Vector3f(viewport.getVisibleWidth() / (2f * viewMult), viewport.getVisibleHeight() / (2f * viewMult), 0f));
        matrix.scale(new Vector3f(1f / viewport.getViewMult(), 1f / viewport.getViewMult(), 1f));
        matrix.translate(new Vector3f(-viewport.getCenter().x, -viewport.getCenter().y, 0f));

        //model
        matrix.translate(new Vector3f(loc.x, loc.y, 0f));
        matrix.rotate((float) Math.toRadians(angle.w), new Vector3f(angle.x, angle.y, angle.z));

        return matrix;
    }

    public static Matrix4f orthogonal(float right, float top) {
        Matrix4f matrix = new Matrix4f();

        float left = 0f;
        float bottom = 0f;
        float zNear = -100f;
        float zFar = 100f;

        matrix.m00 = 2f / (right - left);

        matrix.m11 = 2f / (top - bottom);
        matrix.m22 = 2f / (zNear - zFar);

        matrix.m30 = -(right + left) / (right - left);
        matrix.m31 = -(top + bottom) / (top - bottom);
        matrix.m32 = -(zFar + zNear) / (zFar - zNear);

        matrix.m33 = 1f;

        return matrix;
    }

}
