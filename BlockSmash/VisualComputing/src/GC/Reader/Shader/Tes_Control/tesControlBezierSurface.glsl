#version 430
//author Patrick Pavlenko
//version 22.12.2019
uniform mat4 projMat;
uniform mat4 mvMat;
layout(vertices = 10) out; // 10 Kontrollpunkte


void main(void)
{
    // Hohe Tessellierung,fuer kurvige Flaeche des Bezierdreiecks
    gl_TessLevelOuter[0] = 64;
    gl_TessLevelOuter[1] = 64;
    gl_TessLevelOuter[2] = 64;
    gl_TessLevelInner[0] = 64;

    //Welcher Kontrollpunkt zu welcher gl_Position gehoert
    //gl_InvocationID = welcheter Aufruf des Tesselators das ist
    gl_out[gl_InvocationID].gl_Position = gl_in[gl_InvocationID].gl_Position;
}
