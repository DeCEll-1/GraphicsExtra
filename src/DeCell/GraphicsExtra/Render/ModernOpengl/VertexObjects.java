package DeCell.GraphicsExtra.Render.ModernOpengl;


import DeCell.GraphicsExtra.Render.RenderMisc;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;

public class VertexObjects {

    private int vertexBufferId;
    private int colorBufferId;
    private int vertexArrayID = 0;
    public int size = 0;


    public VertexObjects init(List<Vector3f> tempLocs)  {

        List<Vector3f> locs = RenderMisc.Vector2f2_0_1_Vector2f(tempLocs);

        size = locs.size();

        vertexArrayID = glGenVertexArrays();
        glBindVertexArray(vertexArrayID);

        float[] vertices = Vector3fToArray(locs);

        vertexBufferId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferId);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length );
        vertexBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        glBindVertexArray(0);

        return this;
    }

    public VertexObjects insertColors(List<Color> colors, int pointer){

        glBindVertexArray(vertexArrayID); // bind the array so were working on it

        float[] colorsArray = ColorsToArray(colors);

        colorBufferId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorBufferId);
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colorsArray.length);
        colorBuffer.put(colorsArray).flip();
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(pointer, 4, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(pointer);

        glBindVertexArray(0); // unbind cuz weh

        return this;
    }

    public void bind() {
        glBindVertexArray(vertexArrayID);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void dispose() { // why not
        glDeleteVertexArrays(vertexArrayID);
    }

    private float[] Vector2fToArray(List<Vector2f> vertexes) {
        float[] result = new float[vertexes.size() * 2];

        for (int i = 0; i < vertexes.size() * 2; i += 2) {
            result[i] = vertexes.get(i / 2).getX();
            result[i + 1] = vertexes.get(i / 2).getY();
        }

        return result;
    }

    private float[] Vector3fToArray(List<Vector3f> vertexes) {
        float[] result = new float[vertexes.size() * 3];

        for (int i = 0; i < vertexes.size() * 3; i += 3) {
            result[i] = vertexes.get(i / 3).getX();
            result[i + 1] = vertexes.get(i / 3).getY();
            result[i + 2] = vertexes.get(i / 3).getZ();
        }

        return result;
    }

    private float[] ColorsToArray(List<Color> colors) {
        float[] result = new float[colors.size() * 4];

        for (int i = 0; i < colors.size() * 4; i += 4) {
            result[i] = colors.get(i / 4).getRed() / 255f;
            result[i + 1] = colors.get(i / 4).getGreen() / 255f;
            result[i + 2] = colors.get(i / 4).getBlue() / 255f;
            result[i + 3] = colors.get(i / 4).getAlpha() / 255f;
        }

        return result;
    }


}
