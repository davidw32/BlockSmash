#version 430
//author Patrick Pavlenko
//version 22.12.2019
uniform mat4 projMat;
uniform mat4 mvMat;
layout(vertices = 1) out; // 1 Kontrollpunkt (jedoch nicht genutzt,damit wir der control shader nur 1x ausgefuehrt)

void main(void)
{
    // niedrige Tesselierungen ist genuegend fuer gerade Flaeche
    gl_TessLevelOuter[0] = 8;
    gl_TessLevelOuter[1] = 8;
    gl_TessLevelOuter[2] = 8;
    gl_TessLevelOuter[3] = 8;
    gl_TessLevelInner[0] = 8;
    gl_TessLevelInner[1] = 8;
}
