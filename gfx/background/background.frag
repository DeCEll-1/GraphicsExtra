#version 330 core

uniform vec2 shape;
uniform float t;

out vec4 FragColor;

void main() {
    vec2 uv = gl_FragCoord.xy / shape.xy;

    FragColor = vec4(uv, 1., 1.);
}
