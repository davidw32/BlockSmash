#version 430
//author Patrick Pavlenko
//version 22.12.2019
//Lichtberechnungen Blinn-Phong-Shader
//Entnommen und modizifiert aus: Gordon, V. Scott / clevenger, John (2019): computer graphics programming in opengl with Java. Second Edition. Mercury learning and education: dulles, Virginia.
layout(location=0) in vec3 position;
layout(location=1) in vec2 textureCord;
layout(location=2) in vec3 normals;

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

    varVerPos = (mvMat * vec4(position,1.0)).xyz;

    varLdirection[0] = light01.pos - varVerPos;
    varLdirection[1] = light02.pos - varVerPos;
    varLdirection[2] = light03.pos - varVerPos;

    varN = (invMat * vec4(normals,1.0)).xyz;

    halfvector[0] = normalize(normalize(varLdirection[0])+normalize(-varVerPos)).xyz;
    halfvector[1] = normalize(normalize(varLdirection[1])+normalize(-varVerPos)).xyz;
    halfvector[2] = normalize(normalize(varLdirection[2])+normalize(-varVerPos)).xyz;

    gl_Position = projMat*mvMat*vec4(position,1.0);

}

