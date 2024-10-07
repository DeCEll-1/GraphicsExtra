#version 330 core

uniform float t;
uniform sampler2D tex;
uniform vec2 textureShape;
uniform vec2 nextPOTShape;

in vec2 texCoord;

out vec4 FragColor;

void main() {
    // texture becomes power of 2
    // to solve, you take the next power of 2 you can get depending on the image size
    // (80, 120) / (128, 128)
    // texCoord = texCoord * ((80,120) / (128, 128));

    vec2 coord = texCoord.xy * (textureShape / nextPOTShape);

    FragColor = texture(tex, coord).rgba;
    // FragColor = vec4(1.);
}
