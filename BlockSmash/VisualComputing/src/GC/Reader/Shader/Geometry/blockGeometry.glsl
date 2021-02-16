#version 430
//author Patrick Pavlenko
//version 22.12.2019

// Als Triangle Primitive eingesendet
layout(triangles) in;
uniform mat4 projMat;
// Als Pointprimitive ausgesendet
layout(points,max_vertices=1) out;
// Von Vertex nach Geometry muss Variable als array deklariert werden ( Fuer einzelne Points?)
in vec3 varN[];
in float explodePass[];



void main(void)
{

    // 3 GL_POINTS erstellt aus den Eckpunkten des Triangles ( Jeder sein eigener Primitive)
    vec3 v1 = ((gl_in[0].gl_Position.xyz));
    vec3 v2 = ((gl_in[1].gl_Position.xyz));
    vec3 v3 = ((gl_in[2].gl_Position.xyz));

    gl_Position = projMat * vec4(v1,1.0);
    EmitVertex();

    EndPrimitive();

    gl_Position = projMat * vec4(v2,1.0);
    EmitVertex();

    EndPrimitive();

    gl_Position = projMat * vec4(v3,1.0);
    EmitVertex();

    EndPrimitive();
}
