package DeCell.GraphicsExtra.Render.ModernOpengl;

import DeCell.GraphicsExtra.Helpers.Logger;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader {//https://youtu.be/q_dS3JuoeDw
    //some copy pastes from hex shields
    private int ProgramID;//program id
    private int VertexShaderID;//vertex shader id
    private int FragmentShaderID;//fragment shader id

    //region setup
    public Shader() {
        ProgramID = glCreateProgram();
    }

    public Shader loadVertexShader(String vertexShaderFilePath) {
        try {
            String vert = Global.getSettings().loadText(vertexShaderFilePath);
            vert = ShaderInclude(vert, vertexShaderFilePath);

            boolean doesContainInclude = false;
            if (vert.contains("#include")) {
                doesContainInclude = true;
                Logger.log(Shader.class, "-/-/-/ COULD NOT LOAD SHADER PROPERLY /-/-/-\nSHADER: \n" + vert + "\nSHADER PATH: \n" + vertexShaderFilePath);
            }

            createVertexShader(vert);
        } catch (Exception ignore) {
        } finally {
            return this;
        }
    }

    public Shader createVertexShader(String shaderCode) {
        // Create the shader and set the source
        VertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(VertexShaderID, shaderCode);

        // Compile the shader
        glCompileShader(VertexShaderID);

        // Check for errors
        if (glGetShaderi(VertexShaderID, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("Error creating vertex shader\n"
                    + glGetShaderInfoLog(VertexShaderID, glGetShaderi(VertexShaderID, GL_INFO_LOG_LENGTH)));

        // Attach the shader
        glAttachShader(ProgramID, VertexShaderID);

        return this;
    }

    public Shader loadFragShader(String fragShaderFilePath) {
        try {
            String frag = Global.getSettings().loadText(fragShaderFilePath);

            frag = ShaderInclude(frag, fragShaderFilePath);
            boolean doesContainInclude = false;
            if (frag.contains("#include")) {
                doesContainInclude = true;
                Logger.log(Shader.class, "-/-/-/ COULD NOT LOAD SHADER PROPERLY /-/-/-\nSHADER: \n" + frag + "\nSHADER PATH: \n" + fragShaderFilePath);
            }


            createFragmentShader(frag);
        } catch (Exception ignore) {
        } finally {
            return this;
        }
    }

    public Shader createFragmentShader(String shaderCode) {
        // Create the shader and set the source
        FragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(FragmentShaderID, shaderCode);

        // Compile the shader
        glCompileShader(FragmentShaderID);

        // Check for errors
        if (glGetShaderi(FragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("Error creating fragment shader\n"
                    + glGetShaderInfoLog(FragmentShaderID, glGetShaderi(FragmentShaderID, GL_INFO_LOG_LENGTH)));

        // Attach the shader
        glAttachShader(ProgramID, FragmentShaderID);

        return this;
    }
    //endregion

    //region start / end
    public Shader link() {
        glLinkProgram(ProgramID);
        if (glGetProgrami(ProgramID, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Unable to link shader program: " + glGetProgramInfoLog(ProgramID, 1024));
        }

        return this;
    }

    public void bind() {
        glUseProgram(ProgramID);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void dispose() {
        // Unbind the program
        unbind();

        // Detach the shaders
        glDetachShader(ProgramID, VertexShaderID);
        glDetachShader(ProgramID, FragmentShaderID);

        // Delete the shaders
        glDeleteShader(VertexShaderID);
        glDeleteShader(FragmentShaderID);

        // Delete the program
        glDeleteProgram(ProgramID);
    }

    public int getProgramID() {
        return ProgramID;
    }
    //endregion

    //region frag variable setting
    private Map<String, Integer> variables = new HashMap<>();

    public Shader SetFloat(String variableName, float value) {
        if (variables.containsKey(variableName)) {
            glUniform1f(variables.get(variableName), value);
        } else {
            variables.put(variableName, glGetUniformLocation(ProgramID, variableName));
            SetFloat(variableName, value);
        }
        return this;
    }

    public Shader SetVector2f(String variableName, Vector2f vec2f) {
        if (variables.containsKey(variableName)) {
            glUniform2f(variables.get(variableName), vec2f.x, vec2f.y);
        } else {
            variables.put(variableName, glGetUniformLocation(ProgramID, variableName));
            SetVector2f(variableName, vec2f);
        }
        return this;
    }

    public Shader SetVector3f(String variableName, Vector3f vec3f) {
        if (variables.containsKey(variableName)) {
            glUniform3f(variables.get(variableName), vec3f.x, vec3f.y, vec3f.z);
        } else {
            variables.put(variableName, glGetUniformLocation(ProgramID, variableName));
            SetVector3f(variableName, vec3f);
        }
        return this;
    }

    public Shader SetColor(String variableName, Color color) {
        SetVector4f(variableName, new Vector4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f));
        return this;
    }

    public Shader SetVector4f(String variableName, Vector4f vec4f) {
        if (variables.containsKey(variableName)) {
            glUniform4f(variables.get(variableName), vec4f.x, vec4f.y, vec4f.z, vec4f.w);
        } else {
            variables.put(variableName, glGetUniformLocation(ProgramID, variableName));
            SetVector4f(variableName, vec4f);
        }
        return this;
    }

    public Shader SetMatrix4f(String variableName, Matrix4f matrix4f) {
        if (variables.containsKey(variableName)) {
            FloatBuffer fb = BufferUtils.createFloatBuffer(16);
            matrix4f.store(fb);
            fb.flip();
            glUniformMatrix4(variables.get(variableName), false, fb);
        } else {
            variables.put(variableName, glGetUniformLocation(ProgramID, variableName));
            int location = GL20.glGetUniformLocation(this.ProgramID, variableName);
            SetMatrix4f(variableName, matrix4f);
        }
        return this;
    }

    public Shader SetMatrix4f(String variableName, FloatBuffer fb) {
        if (variables.containsKey(variableName)) {
            glUniformMatrix4(variables.get(variableName), false, fb);
        } else {
            variables.put(variableName, glGetUniformLocation(ProgramID, variableName));
            int location = GL20.glGetUniformLocation(this.ProgramID, variableName);
            SetMatrix4f(variableName, fb);
        }
        return this;
    }

    public Shader SetTexture(String variableName, SpriteAPI texture, int textureID) {
        if (variables.containsKey(variableName)) {
            glUniform1i(variables.get(variableName), textureID);
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureID);

            texture.bindTexture();
        } else {
            variables.put(variableName, glGetUniformLocation(ProgramID, variableName));
            SetTexture(variableName, texture, textureID);
        }
        return this;
    }

    public Shader SetTexture(String variableName, int texture, int textureID) {
        if (variables.containsKey(variableName)) {
            glUniform1i(variables.get(variableName), textureID);
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureID);

            glBindTexture(GL_TEXTURE_2D, texture);

        } else {
            variables.put(variableName, glGetUniformLocation(ProgramID, variableName));
            SetTexture(variableName, texture, textureID);
        }
        return this;
    }

    //endregion

    //region Shader Preproccessing

    //GET  a shader file
    // get İTS includes
    // add the İNCLUDED SHİT to the FUCKİNG LİST
    // then from the fucking LİST find the included SHİT from THERE
    // then WHAT THE FUCK DO İ DO THEN AAAAAAAAAAAAAAAAAAAAA

    /*
     * while code haves include
     *
     * get code from include
     *
     * if the gotten code haves include, get that code from include, repeat
     *
     *
     *
     *
     *
     * */


    private static String ShaderInclude(String code, String codeSourcePath) {
        try {//https://stackoverflow.com/a/3205019/21149029
            while (code.contains("#include")) {

                String[] returns = getCodeToInclude(code, codeSourcePath);
                //since java doesnt have (type, type) returns i have to do this, fuck java

                String includeToReplace = returns[0];

                String codeToInclude = returns[1];

                String importedShaderPath = returns[2];

                if (codeToInclude.contains("#include")) {
                    codeToInclude = ShaderInclude(codeToInclude, importedShaderPath);
                }

                code = code.replaceFirst(includeToReplace, codeToInclude);
            }

            return code;

        } catch (Exception ex) {
            Logger.log(Shader.class, ex.toString());
        } finally {
            return code;
        }
    }

    private static String[] getCodeToInclude(String code, String codeSourcePath) {
        try {

            int importStartLoc = code.indexOf("#include") + 8; // +8 because of "#include"

            int importEndLoc = code.indexOf("\n", importStartLoc);

            String includeFilePath = code.substring(importStartLoc, importEndLoc)
                    .replaceAll("\"", "")
                    .replaceAll("\\\\", "/")
                    .replaceAll(" ", "");

            List<String> sourcePath = new ArrayList<>();
            sourcePath.addAll(Arrays.asList(codeSourcePath.split("/")));
            if (sourcePath.get(sourcePath.size() - 1).contains(".") && sourcePath.size() > 1) {
                sourcePath.remove(sourcePath.size() - 1);
            }

            String importedShaderPath = relativePathToAbsolutePath(combinePath(sourcePath), includeFilePath);

            final String codeToInclude = Global.getSettings().loadText(importedShaderPath);

            final String includeToReplace = code.substring(importStartLoc - 8, importEndLoc);

            return new String[]{
                    includeToReplace, codeToInclude, importedShaderPath
            };
        } catch (Exception ex) {
            Logger.log(Shader.class, ex.toString());
        }
        return null;
    }

    private static String relativePathToAbsolutePath(String absolutePath, String relativePath) {
        String result = "";

        List<String> absolutes = new ArrayList<>();
        absolutes.addAll(Arrays.asList(absolutePath.split("/")));
        List<String> relatives = new ArrayList<>();
        relatives.addAll(Arrays.asList(relativePath.split("/")));
//        if (relatives.size() == 1) {
//            relatives.add(0, "..");
//        }

        for (String pathFragment : relatives) {
            if (Objects.equals(pathFragment, "..")) {
                int indexToRemove = absolutes.size() - 1;
                absolutes.remove(indexToRemove);
            } else {
                absolutes.add(pathFragment);
            }
        }

        result = combinePath(absolutes);

        return result;
    }

    private static String combinePath(List<String> path) {
        String result = "";

        for (String pathFragment : path) {
            result += pathFragment + "/";
        }

        return result.replaceFirst(".$", ""); // https://stackoverflow.com/a/18460535/21149029 remove the last /
    }


    //endregion

}
