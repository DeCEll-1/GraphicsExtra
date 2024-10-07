package DeCell.GraphicsExtra.Render.ModernOpengl2;

import DeCell.GraphicsExtra.Render.ModernOpengl.VertexObjects;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class VertexManager {

    private int vertexArrayID = 0;

    public VertexManager init() {

        vertexArrayID = glGenVertexArrays();
        glBindVertexArray(vertexArrayID);

        glBindVertexArray(0);

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

    public void AddVertexAttribute2f(int pointer, List<Vector2f> vectors) {
        glBindVertexArray(vertexArrayID); // bind the array so were working on it

        float[] colorsArray = Vector2fToArray(vectors);

        int colorBufferId = glGenBuffers(); // generate a buffer id
        glBindBuffer(GL_ARRAY_BUFFER, colorBufferId); // bind our buffer
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colorsArray.length); // make float buffer to add our values into
        colorBuffer.put(colorsArray).flip(); // flip it cuz idk
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW); // input the data
        glVertexAttribPointer(pointer, 4, GL_FLOAT, false, 0, 0); // set the
        glVertexAttribDivisor(pointer, 1);
        glEnableVertexAttribArray(pointer);

        glBindVertexArray(0); // unbind
    }

    public void AddVertexAttribute3f(int pointer, List<Vector3f> vectors) {
        glBindVertexArray(vertexArrayID); // bind the array so were working on it

        float[] colorsArray = Vector3fToArray(vectors);

        int colorBufferId = glGenBuffers(); // generate a buffer id
        glBindBuffer(GL_ARRAY_BUFFER, colorBufferId); // bind our buffer
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colorsArray.length); // make float buffer to add our values into
        colorBuffer.put(colorsArray).flip(); // flip it cuz idk
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW); // input the data
        glVertexAttribPointer(pointer, 4, GL_FLOAT, false, 0, 0); // set the
        glVertexAttribDivisor(pointer, 1);
        glEnableVertexAttribArray(pointer);

        glBindVertexArray(0); // unbind
    }

    public void AddVertexAttribute4f(int pointer, List<Vector4f> vectors) {
        glBindVertexArray(vertexArrayID); // bind the array so were working on it

        float[] colorsArray = Vector4fToArray(vectors);

        int colorBufferId = glGenBuffers(); // generate a buffer id
        glBindBuffer(GL_ARRAY_BUFFER, colorBufferId); // bind our buffer
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colorsArray.length); // make float buffer to add our values into
        colorBuffer.put(colorsArray).flip(); // flip it cuz idk
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW); // input the data
        glVertexAttribPointer(pointer, 4, GL_FLOAT, false, 0, 0); // set the
        glVertexAttribDivisor(pointer, 1);
        glEnableVertexAttribArray(pointer);

        glBindVertexArray(0); // unbind
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

    private float[] Vector4fToArray(List<Vector4f> vertexes) {
        float[] result = new float[vertexes.size() * 4];

        for (int i = 0; i < vertexes.size() * 4; i += 4) {
            result[i] = vertexes.get(i / 4).getX() / 255f;
            result[i + 1] = vertexes.get(i / 4).getY() / 255f;
            result[i + 2] = vertexes.get(i / 4).getZ() / 255f;
            result[i + 3] = vertexes.get(i / 4).getW() / 255f;
        }

        return result;
    }
}
