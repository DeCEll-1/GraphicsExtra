#version 330 core
layout(location = 0) in vec3 aPos;
layout(location = 1) in vec4 aColor;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
out vec4 ourColor;

void main() {
    gl_Position = viewMatrix * modelMatrix * vec4(aPos, 1.);
    // gl_Position = viewMatrix * vec4(aPos, 1.);
    // gl_Position = vec4(aPos, 1.);
    ourColor = vec4(aColor);
}
