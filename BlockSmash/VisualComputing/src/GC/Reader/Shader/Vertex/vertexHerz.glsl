#version 430
//author Patrick Pavlenko
//version 22.12.2019
// vertex
layout(location=0) in vec3 position;
// texel
layout(location=1) in vec2 textureCord;
layout(binding=0) uniform sampler2D tex;
out vec2 passCord;
uniform mat4 projMat;
uniform mat4 mvMat;

// normale Projektion und MV berechnungen
// Texturweitergabe an fragment
void main(void)
{
    gl_Position = projMat*mvMat*vec4(position,1.0);
    passCord = textureCord;
}


