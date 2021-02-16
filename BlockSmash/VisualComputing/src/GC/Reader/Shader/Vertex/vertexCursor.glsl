#version 430
//author Patrick Pavlenko
//version 22.12.2019
// vertex
layout(location=0) in vec3 position;
uniform mat4 projMat;
uniform mat4 mvMat;

void main(void)
{
      gl_Position = projMat*mvMat*vec4(position,1.0);
}


