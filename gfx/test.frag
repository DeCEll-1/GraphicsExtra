#version 330 core

uniform float t;
uniform sampler2D tex;

in vec2 texCoord;

out vec4 FragColor;

void main() {
    FragColor = vec4(texture(tex, texCoord).bgr, 1.);
    // FragColor = vec4(1.);
}
