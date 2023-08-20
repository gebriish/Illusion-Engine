#version 330 core
layout (location=0) in vec2 aPos;
layout (location=1) in vec4 aCol;
layout (location=2) in vec2 aUV;
layout (location=3) in float atexID;

uniform mat4 camera;

out vec2 vPos;
out vec4 vCol;
out vec2 vUV;
out float texID;

void main() {
    vPos = aPos;
    vCol = aCol;
    vUV = aUV;
    texID = atexID;

    gl_Position = camera * vec4(vPos.x, vPos.y, 0.0f, 1.0f);
}