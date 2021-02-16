#version 430

uniform mat4 projMat;
uniform mat4 mvMat;


void main(void)
{
    //Kontrollpunkte
    const vec4 vertices[] =
    vec4[] (
    vec4( 0.0, 0.0,  0.0, 1.0), // b003
    vec4( 0.2, 0.0,  0.3, 1.0), // b012
    vec4( 0.3, 0.0,  0.6, 1.0), // b021
    vec4( 0.5, 0.0,  1.0, 1.0), // b030
    vec4( 0.3, 0.0,  0.0, 1.0), // b102
    vec4( 0.5, 1.0,  0.5, 1.0), // b111
    vec4( 0.8, 0.0,  0.6, 1.0), // b120
    vec4( 0.4, 0.0,  0.3, 1.0), // b210
    vec4( 0.6, 0.0,  0.0, 1.0), // b201
    vec4( 1.2, 0.0,  0.0, 1.0)); // b300

    // Welcher Kontrollpunkt genutzt werden soll, x-ten ( gl_VertexID) Vertex
    gl_Position = (vertices[gl_VertexID]);
}


