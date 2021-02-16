#version 430
//author Patrick Pavlenko
//version 22.12.2019
layout(location=0) in vec3 position; // vertex
layout(location=1) in vec2 textureCord;  //texturkoordinaten
uniform mat4 projMat;
uniform mat4 mvMat;
out vec2 passCord;
layout(binding=0) uniform sampler2D tex;


void main(void)
{
    gl_Position = projMat*mvMat*vec4(position,1.0);
    passCord = textureCord;
}


