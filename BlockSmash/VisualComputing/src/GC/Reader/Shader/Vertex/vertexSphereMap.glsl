#version 430
//author Patrick Pavlenko
//version 22.12.2019
layout(location=0) in vec3 position; // vertex
layout(location=1) in vec2 textureCord;  //texturkoordinaten
layout(binding=0) uniform sampler2D tex; // texel
out vec2 passCord;
out vec2 gradient;
in vec2 translationVec;
uniform mat4 projMat;
uniform mat4 mvMat;


void main(void)
{
    // normale PMV berechnung
    //Gradient weiter an fragment senden
    // texturweitergabe an fragment
    gl_Position = projMat*mvMat*vec4(position,1.0);
    gradient = translationVec;
    passCord = textureCord;
}



