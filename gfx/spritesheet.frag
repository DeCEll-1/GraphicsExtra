#version 330 core

uniform sampler2D sheet;
uniform vec2 size; // = vec2(616., 356.);
uniform vec2 nextPOTSize;
uniform vec2 targetLoc; // = vec2(1., 123.);
uniform vec2 targetSize; // = vec2(210., 232.);

in vec2 texCoord;

out vec4 FragColor;

void main() {
    vec2 uv = texCoord.xy; // * (size / nextPOTSize);
    // vec2 mult = vec2((size / nextPOTSize).x, 1 - (size / nextPOTSize).y);
    // uv *= mult;
    vec4 col = vec4(0.);
    col.a = 1.;

    // vec2 normTarSize = targetSize / size;
    // // normTarSize *= mult;
    // // vec2 tarLocToDivide = vec2(targetLoc.x, (size.y - targetSize.y) - targetLoc.y);
    // // tarLocToDivide *= mult;
    // vec2 normTarLoc = targetLoc / size;
    // // normTarLoc *= mult;

    vec2 normTarSize = targetSize / size;
    vec2 targetLocTemp = vec2(targetLoc.x, (size.y - targetSize.y) - targetLoc.y);
    vec2 normTarLoc = targetLocTemp / size;

    col.g = dot(texture(sheet, uv * normTarSize + normTarLoc).rgb, vec3(1.)); // * normTarSize + normTarLoc
    col.a = .5;
    FragColor = col;
}
