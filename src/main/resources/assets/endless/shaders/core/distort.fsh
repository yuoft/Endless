#version 150

uniform sampler2D Sampler0;
uniform float time;
uniform float distortStrength;

in vec2 texCoord;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec2 uv = texCoord;
    vec2 center = vec2(0.5, 0.5);
    vec2 dir = uv - center;
    float radius = length(dir);
    float angle = atan(dir.y, dir.x);

    // 扭曲：角度和半径受时间和半径调制
    float t = time * 2.0;
    angle += sin(radius * 20.0 + t) * distortStrength;
    radius += sin(radius * 30.0 + t * 1.5) * distortStrength * 0.1;

    vec2 newUv = center + vec2(cos(angle), sin(angle)) * radius;
    // 边界钳制，防止边缘撕裂
    newUv = clamp(newUv, 0.0, 1.0);

    vec4 texColor = texture(Sampler0, newUv);
    fragColor = texColor * vertexColor;
}