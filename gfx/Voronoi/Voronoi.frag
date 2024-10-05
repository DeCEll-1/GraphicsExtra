uniform float width;
uniform float height;
uniform float t;

#include "../lygia/generative/voronoi.glsl"

void main() {
    vec2 u_resolution = vec2(width, height);
    
    // Normalized pixel coordinates (from 0 to 1)
    vec4 color = vec4(0., 0., 0., 1.);
    vec2 uv = gl_FragCoord.xy / u_resolution;
    
    vec3 noise3d = voronoi(vec3(uv * 5., 1. + t));
    
    color.rb += noise3d.r;
    
    // color.gb -= clamp(isOverLine(vec2(0.,0.), vec2(1.,1.), uv, 2.), 0., 1.) +  
    // clamp(isOverLine(vec2(0.,1.), vec2(1.,0.), uv, 2.), 0., 1.)
    // ;
    
    gl_FragColor = color;
}