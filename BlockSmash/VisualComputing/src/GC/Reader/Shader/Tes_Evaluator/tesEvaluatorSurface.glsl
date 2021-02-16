#version 430
//author Patrick Pavlenko
//version 22.12.2019
layout(quads,equal_spacing,ccw) in;

uniform mat4 projMat;
uniform mat4 mvMat;
uniform mat4 invMat;
uniform vec4 globalAmbient;

struct PosLight //ADS Light
{
    vec4 ambient;
    vec4 diff;
    vec4 spec;
    vec3 pos;
};

struct Material // ADS Material
{
    vec4 ambient;
    vec4 diff;
    vec4 spec;
    float shine;
};

// 3 Lichter
uniform PosLight light01;
uniform PosLight light02;
uniform PosLight light03;
uniform Material material;

out vec3 varN;
out vec3 varVerPos;
out vec3 varLdirection[3];
out vec3 halfvector[3];

void main(void)
{

    // Lichtberechnungen
    vec3 normals = vec3(gl_TessCoord.x,1,gl_TessCoord.y);
    varVerPos = (mvMat * vec4(gl_TessCoord.x,0,gl_TessCoord.y,1.0)).xyz;

    varLdirection[0] = light01.pos - varVerPos;
    varLdirection[1] = light02.pos - varVerPos;
    varLdirection[2] = light03.pos - varVerPos;

    varN = (invMat * vec4(normals,1.0)).xyz;

    halfvector[0] = normalize(normalize(varLdirection[0])+normalize(-varVerPos)).xyz;
    halfvector[1] = normalize(normalize(varLdirection[1])+normalize(-varVerPos)).xyz;
    halfvector[2] = normalize(normalize(varLdirection[2])+normalize(-varVerPos)).xyz;

    gl_Position = projMat * mvMat * vec4(gl_TessCoord.x,0,gl_TessCoord.y,1);
}
