#version 330 core
layout(location = 0) in vec2 aPos;
layout(location = 1) in vec2 aTexPos;

uniform mat4 modelMatrix;
uniform mat4 viewMultMatrix;

out vec2 texCoord;

void main() {
    gl_Position = modelMatrix * viewMultMatrix * vec4(aPos, 1., 1.);
    texCoord = aTexPos;
}
