package DeCell.GraphicsExtra.Render.ModernOpengl;

import DeCell.GraphicsExtra.Render.RenderMisc;
import DeCell.GraphicsExtra.Render.Shader;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.SettingsAPI;
import com.fs.starfarer.api.combat.ViewportAPI;
import de.unkrig.commons.nullanalysis.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

public class MGLManager {
    private Shader shader = new Shader();

    public Shader getShader() {
        return shader;
    }

    private VertexObjects vertexObject = new VertexObjects();

    public VertexObjects getVertexObjects() {
        return vertexObject;
    }

    Matrix4f matrix = new Matrix4f();
    List<Vector2f> translate = new ArrayList<>();
    List<Vector2f> scale = new ArrayList<>();
    List<Vector4f> rotate = new ArrayList<>();

    // Function to apply translation
    // must be screen vector
    public void translate(Vector2f vector) {
//        vector = new Vector2f(800, 450);
        vector = RenderMisc.Vector2f2MGLVector2f(vector);
//        vector = new Vector2f(0f, 0f);
        this.translate.add(vector);
    }

    // Function to apply scaling
    public void scale(Vector2f vector) {
        this.scale.add(vector);
    }

    // Function to apply rotation
    // the W value of the vector4f is the angle
    public void rotate(Vector4f angle) {
        this.rotate.add(angle);
    }

    public void render(ViewportAPI viewport) {
        float viewMult = viewport.getViewMult();
        shader.bind();
        matrix = new Matrix4f(); // reset the matrix



        Matrix4f translateMatrix = new Matrix4f();
        Matrix4f scaleMatrix = new Matrix4f();
        Matrix4f rotationMatrix = new Matrix4f();
        for (Vector2f vector : translate) {
            Matrix4f.translate(new Vector3f(vector.getX(), vector.getY(), 0), translateMatrix, translateMatrix);
        }
        for (Vector2f vector : scale) {
            Matrix4f.scale(new Vector3f(vector.getX(), vector.getY(), 1), scaleMatrix, scaleMatrix);
        }
        for (Vector4f angle : rotate) {
            Matrix4f.rotate(angle.getW() * (3.14159265359f / 180f), new Vector3f(angle.getX(), angle.getY(), angle.getZ()), rotationMatrix, rotationMatrix);
        }


        translate = new ArrayList<>();
        scale = new ArrayList<>();
        rotate = new ArrayList<>();


        Matrix4f shaderViewMultMatrix = new Matrix4f();
        float n = 1f / viewport.getViewMult();
        shaderViewMultMatrix.scale(new Vector3f(n, n, 1f));

        float width = Global.getSettings().getScreenWidth();
        float height = Global.getSettings().getScreenHeight();

        float aspect_ratio = height / width;
    //
//        Matrix4f.scale(new Vector3f(1f, aspect_ratio, 1f), shaderViewMultMatrix, shaderViewMultMatrix);
//
//        Matrix4f.mul(orthogonal(-1, 1, -aspect_ratio, aspect_ratio), shaderViewMultMatrix, shaderViewMultMatrix);
    //

    //
        Matrix4f modelMatrix = new Matrix4f();


        Matrix4f.mul(rotationMatrix, modelMatrix, modelMatrix);

        Matrix4f.scale(new Vector3f(1f, aspect_ratio, 1f), modelMatrix, modelMatrix);
        Matrix4f.mul(orthogonal(-1, 1, -aspect_ratio, aspect_ratio), modelMatrix, modelMatrix);

        Matrix4f.mul(scaleMatrix, modelMatrix, modelMatrix);
        Matrix4f.mul(translateMatrix, modelMatrix, modelMatrix);
    //

    //
//        Matrix4f rotationFixMatrix = new Matrix4f();
//
//        Matrix4f.scale(new Vector3f(1f, aspect_ratio, 1f), scaleMatrix, scaleMatrix);
//
//        Matrix4f.mul(orthogonal(-1, 1, -aspect_ratio, aspect_ratio), scaleMatrix, scaleMatrix);
    //
        shader.SetMatrix4f("modelMatrix", modelMatrix);
        shader.SetMatrix4f("transformationMatrix", translateMatrix);
        shader.SetMatrix4f("rotationMatrix", rotationMatrix);
        shader.SetMatrix4f("scaleMatrix", scaleMatrix);
//        shader.SetMatrix4f("rotationFixMatrix", rotationFixMatrix); // https://neevek.net/posts/2017/11/26/opengl-rotating-mapped-texture-in-a-rectangular-viewport.html
        shader.SetMatrix4f("viewMultMatrix", shaderViewMultMatrix);

        vertexObject.bind();
        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexObject.size);
        vertexObject.unbind();
        shader.unbind();
    }


    private Matrix4f orthogonal(float left, float right, float bottom, float top) {
        Matrix4f matrix = new Matrix4f();

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

    private static Matrix4f createGameOrthoMatrix(ViewportAPI viewport, @Nullable Matrix4f result) { // https://discord.com/channels/187635036525166592/824910699415207937/1272632882279747707
        if (result == null) result = new Matrix4f();
        SettingsAPI settings = Global.getSettings();
        float width = Display.getWidth() * Display.getPixelScaleFactor();
        float height = Display.getHeight() * Display.getPixelScaleFactor();
        float scale = viewport.getViewMult();
        result.m00 = 2.0f / width;
        result.m11 = 2.0f / height;
        result.m30 = result.m00 * (-viewport.getCenter().getX() / scale);
        result.m00 /= scale;
        result.m31 = result.m11 * (-viewport.getCenter().getY() / scale);
        result.m11 /= scale;
        return result;

    }

    public static Matrix4f createModelMatrix(Vector3f location, Quaternion rotate, Vector3f scale, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();
        float dqx = rotate.x + rotate.x;
        float dqy = rotate.y + rotate.y;
        float dqz = rotate.z + rotate.z;
        float q00 = dqx * rotate.x;
        float q11 = dqy * rotate.y;
        float q22 = dqz * rotate.z;
        float q01 = dqx * rotate.y;
        float q02 = dqx * rotate.z;
        float q03 = dqx * rotate.w;
        float q12 = dqy * rotate.z;
        float q13 = dqy * rotate.w;
        float q23 = dqz * rotate.w;
        result.m00 = scale.x - (q11 + q22) * scale.x;
        result.m01 = (q01 + q23) * scale.x;
        result.m02 = (q02 - q13) * scale.x;
        result.m10 = (q01 - q23) * scale.y;
        result.m11 = scale.y - (q22 + q00) * scale.y;
        result.m12 = (q12 + q03) * scale.y;
        result.m20 = (q02 + q13) * scale.z;
        result.m21 = (q12 - q03) * scale.z;
        result.m22 = scale.z - (q11 + q00) * scale.z;
        result.m30 = location.x;
        result.m31 = location.y;
        result.m32 = location.z;
        return result;
    }


}