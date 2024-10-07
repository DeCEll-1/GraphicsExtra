#version 330 core
layout(location = 0) in vec3 aPos;
layout(location = 1) in vec4 aColor;

// uniform mat4 modelMatrix;
uniform mat4 modelMatrix;
uniform mat4 transformationMatrix;
uniform mat4 scaleMatrix;
uniform mat4 rotationMatrix;
uniform mat4 viewMultMatrix;
uniform mat4 rotationFixMatrix;
out vec4 ourColor;

void main() {
    // gl_Position = (transformationMatrix * rotationMatrix * scaleMatrix) * viewMultMatrix * vec4(aPos, 1.);
    gl_Position = modelMatrix * viewMultMatrix * vec4(aPos, 1.);
    // gl_Position = modelMatrix * vec4(aPos, 1.);
    // gl_Position = viewMatrix * vec4(aPos, 1.);
    // gl_Position = vec4(aPos, 1.);
    ourColor = vec4(aColor);
}
