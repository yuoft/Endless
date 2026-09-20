#version 150

uniform sampler2D AccretionTexture;
uniform vec3  HoleCenter;
uniform vec3  DiskNormal;
uniform vec3  DiskAxis;
uniform float HorizonRadius;
uniform float Time;

in vec4 vertexColor;
in vec2 texCoord0;
in vec3 viewPos;
in vec2 quadUV;
out vec4 fragColor;

const float PI = 3.14159265359;
const float EPSILON = 0.0001;

// ===== 这里每个常量只声明一次 =====
const float DISC_OUTER_RATIO = 2.5;
const float DISC_INNER_RATIO = 1.2;

bool occludedByHorizon(vec3 p, vec3 eye, vec3 bh, float r_bar) {
    // 盘面点比黑洞中心更靠近相机 => 近侧，永远可见
    if (length(p - eye) < length(bh - eye)) return false;

    // 远侧才做球体遮挡检查
    vec3 d = p - eye;
    float d2 = dot(d, d);
    if (d2 < EPSILON) return false;
    float t = dot(bh - eye, d) / d2;
    if (t <= 0.0 || t >= 1.0) return false;
    vec3 closest = eye + d * t;
    return dot(closest - bh, closest - bh) < r_bar * r_bar;
}

void main() {
    vec3 ray = normalize(viewPos);
    vec3 eye = vec3(0.0);

    float r_bar    = HorizonRadius;
    float disc_in  = r_bar * DISC_INNER_RATIO;
    float disc_out = r_bar * DISC_OUTER_RATIO;

    vec3  h2e = eye - HoleCenter;
    float rm  = length(cross(ray, h2e));

    // ===== 视界阴影 =====
    if (rm < r_bar) {
        fragColor = vec4(0.0, 0.0, 0.0, vertexColor.a);
        return;
    }

    vec3 color = vec3(0.0);

    // ===== 射线与吸积盘平面求交 =====
    float denom = dot(ray, DiskNormal);
    if (abs(denom) > EPSILON) {
        float tPlane = dot(HoleCenter, DiskNormal) / denom;
        if (tPlane > 0.0) {
            vec3  hit = ray * tPlane;
            vec3  rel = hit - HoleCenter;
            float d   = length(rel);

            if (d >= disc_in && d <= disc_out) {
                if (!occludedByHorizon(hit, eye, HoleCenter, r_bar)) {
                    vec3  radial    = rel / d;
                    vec3  bitangent = normalize(cross(DiskAxis, DiskNormal));
                    float angle     = atan(dot(radial, DiskAxis), dot(radial, bitangent));
                    float rNorm     = clamp((d - disc_in) / (disc_out - disc_in), 0.0, 1.0);

                    vec2 uv = vec2(angle / (2.0 * PI) - Time * 0.3, rNorm);
                    vec3 tex = texture(AccretionTexture, uv).rgb;

                    // 内白外纹
                    float heat = 1.0 - rNorm;
                    vec3  whiteHot = vec3(1.0, 0.97, 0.9);
                    color = mix(tex * 1.5, whiteHot, heat * heat * heat);
                    color *= 0.5 + heat * 2.5;
                }
            }
        }
    }

    // ===== 四边形边缘淡出 =====
    float influence = r_bar * 3;
    float edgeAlpha = 1.0 - smoothstep(influence * 0.6, influence, rm);

    float accLen = length(color);
    float finalAlpha = clamp(accLen, 0.0, 1.0) * edgeAlpha * vertexColor.a;
    if (finalAlpha < 0.01) discard;

    fragColor = vec4(color, finalAlpha);
}