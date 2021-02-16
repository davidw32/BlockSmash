#version 430
//author Patrick Pavlenko
//version 22.12.2019
//Lichtberechnungen Blinn-Phong-Shader
//Entnommen und modizifiert aus: Gordon, V. Scott / clevenger, John (2019): computer graphics programming in opengl with Java. Second Edition. Mercury learning and education: dulles, Virginia.
out vec4 color;

uniform mat4 projMat;
uniform mat4 mvMat;
uniform mat4 invMat;
uniform vec4 globalAmbient;
in vec3 varN;
in vec3 varVerPos;
in vec3 varLdirection[3];
in vec3 halfvector[3];

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

uniform PosLight light01;
uniform PosLight light02;
uniform PosLight light03;
uniform Material material;



void main(void)
{

    vec3 nLight1 = normalize(varLdirection[0]); //Normalisierter Lichtvektor
    vec3 nLight2 = normalize(varLdirection[1]);
    vec3 nLight3 = normalize(varLdirection[2]);

    vec3 normal = normalize(varN);
    vec3 view = normalize(-varVerPos);

    float cosTheta1 = dot(nLight1,normal);
    float cosTheta2 = dot(nLight2,normal);
    float cosTheta3 = dot(nLight3,normal);

    vec3 halfvec1 = normalize(halfvector[0]);
    vec3 halfvec2 = normalize(halfvector[1]);
    vec3 halfvec3 = normalize(halfvector[2]);

    float cosPhi1 = dot(halfvec1,normal);
    float cosPhi2 = dot(halfvec2,normal);
    float cosPhi3 = dot(halfvec3,normal);

    vec3 ambient = ((globalAmbient * material.ambient) + (light01.ambient * material.ambient)).xyz;
    ambient += ((globalAmbient * material.ambient) + (light02.ambient * material.ambient)).xyz;
    ambient += ((globalAmbient * material.ambient) + (light03.ambient * material.ambient)).xyz;

    vec3 diffuse = light01.diff.xyz * material.diff.xyz * max(cosTheta1,0.0);
    diffuse += light02.diff.xyz * material.diff.xyz * max(cosTheta2,0.0);
    diffuse += light03.diff.xyz * material.diff.xyz * max(cosTheta3,0.0);

    vec3 specular = light01.spec.xyz * material.spec.xyz * pow(max(cosPhi1,0.0), material.shine*3.0);
    specular += light02.spec.xyz * material.spec.xyz * pow(max(cosPhi2,0.0), material.shine*3.0);
    specular += light03.spec.xyz * material.spec.xyz * pow(max(cosPhi3,0.0), material.shine*3.0);

    color = vec4(ambient+diffuse+specular, 1.0);


}
