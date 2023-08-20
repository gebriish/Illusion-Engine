#version 330 core
layout (location=0) in vec2 aPos;
layout (location=1) in vec2 aUV;

uniform float CamX;
uniform float CamY;

uniform sampler2D uTexSlots[3];

out vec2 vPos;
out vec2 vUV;
out vec2 imgSize;


void main() {
    vPos = aPos;
    vUV = aUV;

    imgSize = textureSize(uTexSlots[0], 0);
    float xPix = 1/imgSize.x;
    float yPix = 1/imgSize.y;

    float xOffset = (fract(CamX) - 0.5f) * xPix;
    float yOffset = (fract(CamY) - 0.5f) * yPix;

    vec2 vertexPos = vec2(vPos.x + (vPos.x/abs(vPos.x)) * xPix, vPos.y + (vPos.y/abs(vPos.y)) * yPix);
    //vec2 vertexPos = vec2(vPos.x, vPos.y);
    vertexPos.x -= xOffset * 2;
    vertexPos.y -= yOffset * 2;

    gl_Position = vec4(vertexPos,.0f, 1.0f);
}