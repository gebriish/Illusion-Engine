#version 330 core

in vec2 vPos;
in vec4 vCol;
in vec2 vUV;
in float texID;

uniform sampler2D uTextures[16];

layout (location = 0) out vec4 FragColor;
layout (location = 1) out vec4 NormColor;

void main() {

    vec4 FinalColor;

    if(texID > 0){
        vec4 texCol = texture(uTextures[int(texID)], vUV);

        if(texCol.a != 1)
            discard;
        FinalColor = vCol * texCol;
    }
    else
    {
        FinalColor = vCol;
    }

    FragColor = FinalColor;
    NormColor = vec4(0, 0, 1, 1);
}