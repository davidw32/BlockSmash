#version 430
//author Patrick Pavlenko
//version 22.12.2019
// Sobald Block mit Cursor kollidiert wird dieser Shader hier fuer den Block genutzt
// vertex
layout(location=0) in vec3 position;
//texturkoordinaten
layout(location=1) in vec2 textureCord;
// normals
layout(location=2) in vec3 normals;

uniform mat4 projMat;
uniform mat4 mvMat;
uniform mat4 invMat;


void main(void)
{
    gl_Position =  mvMat*vec4(position,1.0);

}

