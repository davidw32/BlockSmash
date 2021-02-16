#version 430
//author Patrick Pavlenko
//version 22.12.2019
out vec4 color;
in vec2 passCord;
layout (binding=0) uniform sampler2D tex;
// Textur wird hier benutzt


void main(void)
{
    color = texture(tex,passCord);
}
