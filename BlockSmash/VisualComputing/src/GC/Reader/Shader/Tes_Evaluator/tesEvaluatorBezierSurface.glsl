#version 430
//author Patrick Pavlenko
//version 22.12.2019
layout(triangles,equal_spacing,ccw) in;
//Triangle Primitive des Tesselator!, gleiche Flaechenabstande und winding order
uniform mat4 projMat;
uniform mat4 mvMat;


void main(void)
{
    float u = gl_TessCoord.x;
    float v = gl_TessCoord.y;
    float w = gl_TessCoord.z;

    // Triangle Bernsteiner
    float b003 = w*w*w;
    float b012 = 3*v*w*w;
    float b021 = 3*v*v*w;
    float b030 = v*v*v;
    float b102 = 3*u*w*w;
    float b111 = 6*u*v*w;
    float b120 = 3*u*v*v;
    float b210 = 3*u*u*v;
    float b201 = 3*u*u*v;
    float b300 = u*u*u;


    // kontrollpunkte eingesendet
    vec3 p0 = (gl_in[0].gl_Position).xyz;
    vec3 p1 = (gl_in[1].gl_Position).xyz;
    vec3 p2 = (gl_in[2].gl_Position).xyz;
    vec3 p3 = (gl_in[3].gl_Position).xyz;
    vec3 p4 = (gl_in[4].gl_Position).xyz;
    vec3 p5 = (gl_in[5].gl_Position).xyz;
    vec3 p6 = (gl_in[6].gl_Position).xyz;
    vec3 p7 = (gl_in[7].gl_Position).xyz;
    vec3 p8 = (gl_in[8].gl_Position).xyz;
    vec3 p9 = (gl_in[9].gl_Position).xyz;


    //momentane Position
    vec3 pos = p0*b003+ p1*b012 + p2*b021 + p3*b030 + p4*b102 + p5*b111 + p6*b120 + p7*b210 + p8*b201 + p9 * b300;


    gl_Position = projMat*mvMat * vec4(pos,1.0);
}
