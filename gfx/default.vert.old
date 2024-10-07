#version 110

attribute vec2 texCoord; // Assuming you have texture coordinates as an attribute

void main() {
    // THANK YOU https://youtu.be/zr7k7kaokSk?t=1193 İ LOVE YOU
    gl_Position = ftransform();
    
    // Pass texture coordinates to fragment shader
    gl_TexCoord[0] = vec4(texCoord, 0.0, 1.0);
}