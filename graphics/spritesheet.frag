#ifdef GL_ES
precision mediump float;
#endif

#iChannel0 "spritesheet.png"
vec2 size = vec2(1024., 1024.);
vec2 targetLoc = vec2(177., 1.);
vec2 targetSize = vec2(72., 120.);

void mainImage(out vec4 fragColor, in vec2 fragCoord) {
    vec2 uv = fragCoord.xy / iResolution.xy;
    uv.x *= iResolution.x / iResolution.y;
    vec4 col = vec4(1.);

    vec2 normTarSize = targetSize / size;
    targetLoc.y = (size.y - targetSize.y) - targetLoc.y;
    vec2 normTarLoc = targetLoc / size;

    col.rgba = texture(iChannel0, uv * normTarSize + normTarLoc);

    fragColor = col;
}
