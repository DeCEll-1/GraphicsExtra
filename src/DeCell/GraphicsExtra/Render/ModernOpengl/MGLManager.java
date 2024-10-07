package DeCell.GraphicsExtra.Render.ModernOpengl;

import DeCell.GraphicsExtra.Render.RenderMisc;
import DeCell.GraphicsExtra.Statics;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.SettingsAPI;
import com.fs.starfarer.api.combat.ViewportAPI;
import de.unkrig.commons.nullanalysis.Nullable;
import org.lwjgl.util.vector.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;

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

    //region vars
    Matrix4f matrix = new Matrix4f();
    Vector2f translation = new Vector2f(0, 0);
    Vector2f scaling = new Vector2f(1, 1);
    Vector4f rotation = new Vector4f(0, 0, 0, 0);

    private ScaleSetting setting = ScaleSetting.VIEW_MULT;

    //endregion

    //region functions
    // Function to apply translation
    // must be screen vector
    public void translate(Vector2f vector) {
//        vector = new Vector2f(800, 450);
        vector = RenderMisc.Vector2f2MGLVector2f(vector);
//        vector = new Vector2f(0f, 0f);
        this.translation = vector;
    }

    // Function to apply scaling
    public void scale(Vector2f vector) {
        this.scaling = vector;
    }

    // Function to apply rotation
    // the W value of the vector4f is the angle
    public void rotate(Vector4f angle) {
        this.rotation = angle;
    }

    public void SetScaleSetting(ScaleSetting setting) {
        this.setting = setting;
    }

    //endregion

    public void render(ViewportAPI viewport) {
        shader.bind();

        //region matrixes
        matrix = new Matrix4f(); // reset the matrix
        Matrix4f translateMatrix = new Matrix4f();
        Matrix4f scaleMatrix = new Matrix4f();
        Matrix4f rotationMatrix = new Matrix4f();

        Matrix4f.translate(new Vector3f(translation.getX(), translation.getY(), 0), translateMatrix, translateMatrix);

        Matrix4f.scale(new Vector3f(scaling.getX(), scaling.getY(), 1), scaleMatrix, scaleMatrix);

        // w of vector4f is for the angle and then it gets transformed into radians
        Matrix4f.rotate(rotation.getW() * (3.14159265359f / 180f), new Vector3f(rotation.getX(), rotation.getY(), rotation.getZ()), rotationMatrix, rotationMatrix);
        //endregion

        //region view mult matrix generation
        Matrix4f shaderViewMultMatrix = new Matrix4f();
        if (setting == ScaleSetting.VIEW_MULT) {
            float n = 1f / viewport.getViewMult();
            shaderViewMultMatrix.scale(new Vector3f(n, n, 1f));
        }
        //endregion

        //region model matrix generation

        float width = Global.getSettings().getScreenWidth();
        float height = Global.getSettings().getScreenHeight();

        float aspect_ratio = height / width;

        Matrix4f modelMatrix = new Matrix4f();

        Matrix4f.mul(rotationMatrix, modelMatrix, modelMatrix);

        // https://neevek.net/posts/2017/11/26/opengl-rotating-mapped-texture-in-a-rectangular-viewport.html
        Matrix4f.scale(new Vector3f(1f, aspect_ratio, 1f), modelMatrix, modelMatrix);
        Matrix4f.mul(orthogonal(-1, 1, -aspect_ratio, aspect_ratio), modelMatrix, modelMatrix);
        // \

        Matrix4f.mul(scaleMatrix, modelMatrix, modelMatrix);
        Matrix4f.mul(translateMatrix, modelMatrix, modelMatrix);
        //endregion

        //region uniform settings
        shader.SetVector2f("screenShape" , new Vector2f(width, height));
        shader.SetFloat("t", Statics.t);
        shader.SetMatrix4f("modelMatrix", modelMatrix);
        shader.SetMatrix4f("viewMultMatrix", shaderViewMultMatrix);
        //endregion

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
}