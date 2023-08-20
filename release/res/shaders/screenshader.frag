#version 330 core

in vec2 vPos;
in vec2 vUV;
in vec2 imgSize;

uniform sampler2D uTexSlots[3];

out vec4 FragColor;

void main() {
    vec2 UV = vec2(vUV.x, vUV.y);

    vec3 color  = texture(uTexSlots[0], UV).rgb;
    vec3 normal = texture(uTexSlots[1], UV).rgb;

    FragColor = vec4(color, 1.0f);
}