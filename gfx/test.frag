#version 330 core

uniform float iTime;
// texture becomes power of 2
// to solve, you take the next power of 2 you can get depending on the image size
// (80, 120) / (128, 128)
// texCoord = texCoord * ((80,120) / (128, 128));
uniform sampler2D tex;
uniform vec2 textureShape;
uniform vec2 nextPOTShape;
uniform vec2 level;

#include "lygia/generative/voronoi.glsl"

in vec2 texCoord;

out vec4 FragColor;

void main() {
    vec2 coord = texCoord.xy * (textureShape / nextPOTShape);

    ////
    vec4 color = vec4(1.);

    // get the cell
    vec4 cell = vec4(voronoi(coord * level, iTime), 1.);

    // cells n.0
    vec2 i_coord = floor(coord * level);
    // cells 0.n
    vec2 f_coord = fract(coord * level);

    // add the n.0 to the cell so we can divide it
    // (the xy of the cell is its center point on its square)
    cell.xy += i_coord;

    // divide it by the amount of cells per row we want
    // so we get the location of the cells center in screen instead
    // of its center in its own square
    cell.xy /= level;

    // get the textures location with the cells location
    vec2 texLoc = vec2(cell.xy);

    color.rgba = texture(tex, texLoc).rgba;

    FragColor = color;
    // FragColor = vec4(1.);
}
