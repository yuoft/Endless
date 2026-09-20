#version 150

uniform sampler2D SceneColor;
uniform sampler2D SceneDepth;
uniform sampler2D AccretionTexture;
uniform mat4 InverseProjection;
uniform mat4 SceneProjection;
uniform vec3 HoleCenter;
uniform vec3 DiskNormal;
uniform vec3 DiskAxis;
uniform float HorizonRadius;
uniform float Time;

in vec2 texCoord;
out vec4 fragColor;

/*
 * Adapted from the user-supplied Shadertoy source:
 * "Black hole with gravitational lensing and accretion disc."
 * Jun, 2014.11.24. https://www.shadertoy.com/view/ldjSDc
 * Reference: Bozza, Valerio, "Gravitational lensing by black holes",
 * General Relativity and Gravitation 42.9 (2010): 2269-2300.
 *
 * Retains its alpha = 4*M/rm hyperbola, both disc intersections, inner-ray
 * correction, 2.1 disc radius ratio and rotating polar texture coordinates.
 * Minecraft supplies the camera and scene/depth; iChannel0 is the texture
 * supplied by the user with the original Shadertoy link.
 * This is the source's visual approximation, not a geodesic integrator.
 */
const float PI = 3.14159265359;
const float BH_M = 1.2;
const float R_BAR = 2.7 * BH_M;
const float DISC_R_ORIG = R_BAR * 2.1;
// In HorizonRadius units; keep in sync with GapingVoidShaders.
const float INFLUENCE_RADIUS = 6.0;
const float EPSILON = 0.00001;

float sceneDistance(vec2 uv) {
    float depth = texture(SceneDepth, uv).r;
    if (depth >= 0.999999) return 1e20;
    vec4 view = InverseProjection * vec4(uv * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    return length(view.xyz / view.w) * R_BAR / HorizonRadius;
}

float sceneViewDepth(vec2 uv) {
    float depth = texture(SceneDepth, uv).r;
    if (depth >= 0.999999) return 1e20;
    vec4 view = InverseProjection * vec4(uv * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    return -view.z / view.w * R_BAR / HorizonRadius;
}

vec3 discColor(vec3 position, float discRadius) {
    float distance = length(position);
    if (distance >= discRadius || distance < EPSILON) return vec3(0.0);
    vec3 radial = position / distance;
    vec2 uv = vec2(atan(dot(radial, DiskAxis), dot(radial, cross(DiskAxis, DiskNormal)))
                   / (2.0 * PI) - Time * 0.3,
                   (distance - R_BAR) / (discRadius - R_BAR));
    // Preserve the source's repeating polar UVs. 1-smoothstep replaces its
    // undefined reversed edges without changing the intended inward fade.
    vec3 material = texture(AccretionTexture, uv).rgb;
    return 3.0 * material * (1.0 - smoothstep(R_BAR, discRadius, distance));
}

// Stable quadratic solver, including the linear limit at a tangent asymptote.
int roots(float a, float b, float c, out vec2 result) {
    result = vec2(0.0);
    float scale = max(max(abs(a), abs(b)), abs(c));
    if (scale < EPSILON) return 0;
    a /= scale;
    b /= scale;
    c /= scale;
    if (abs(a) < 0.0000001) {
        if (abs(b) < 0.0000001) return 0;
        result.x = -c / b;
        return 1;
    }
    float discriminant = b * b - 4.0 * a * c;
    if (discriminant < 0.0) return 0;
    float delta = sqrt(discriminant);
    float q = -0.5 * (b + (b < 0.0 ? -delta : delta));
    if (abs(q) < 0.0000001) {
        result.x = -b / (2.0 * a);
        return 1;
    }
    result = vec2(q / a, c / q);
    return delta < 0.0000001 ? 1 : 2;
}

vec3 discIntersection(float distance, vec3 line, vec3 xAxis, vec3 yAxis,
                      float c, float eyeY, float impact, float discRadius,
                      vec3 eye, vec3 ray, float geometryDistance) {
    if (abs(distance) >= discRadius) return vec3(0.0);
    vec3 position = line * distance;
    float x = c + dot(position, xAxis);
    float y = dot(position, yAxis);
    float rayDistance = dot(position - eye, ray);
    if (x < 0.0 || y < eyeY || rayDistance < 0.0 || rayDistance >= geometryDistance) return vec3(0.0);
    // The original source only exposes the incoming disc branch inside r_bar.
    if (!((y < 0.0 && abs(distance) > R_BAR) || impact > R_BAR)) return vec3(0.0);
    return discColor(position, discRadius);
}

void main() {
    vec4 original = texture(SceneColor, texCoord);
    vec4 farPoint = InverseProjection * vec4(texCoord * 2.0 - 1.0, 1.0, 1.0);
    vec3 ray = normalize(farPoint.xyz / farPoint.w);
    vec3 eye = -HoleCenter * R_BAR / HorizonRadius;
    float eyeDistance2 = dot(eye, eye);
    if (eyeDistance2 < R_BAR * R_BAR) {
        fragColor = vec4(0.0, 0.0, 0.0, original.a);
        return;
    }

    float closestTime = -dot(eye, ray);
    float impact = length(cross(ray, eye));
    float influence = INFLUENCE_RADIUS * R_BAR;
    float discriminant = influence * influence - impact * impact;
    if (closestTime <= 0.0 || discriminant <= 0.0) { fragColor = original; return; }
    float entry = max(0.0, closestTime - sqrt(discriminant));
    float geometryDistance = sceneDistance(texCoord);
    if (geometryDistance <= entry) { fragColor = original; return; }

    float shadowFront = closestTime - sqrt(max(0.0, R_BAR * R_BAR - impact * impact));
    float shadowWidth = max(fwidth(impact), EPSILON);
    float shadow = (1.0 - smoothstep(R_BAR - shadowWidth, R_BAR + shadowWidth, impact))
                 * step(shadowFront, geometryDistance);

    vec3 light = vec3(0.0);
    vec3 outgoing = ray;
    float alpha = 4.0 * BH_M / max(impact, EPSILON);
    float discRadius = DISC_R_ORIG;
    if (impact < R_BAR) {
        // Source's inner-ray correction. Camera up is +Y in view space.
        alpha *= 1.0 - clamp(abs(DiskNormal.y), 0.0, 1.0);
        discRadius *= 1.25;
    }
    // Captured central rays need a bounded tan(alpha/2), including rm == 0.
    alpha = clamp(alpha, 0.0, 2.8);
    float k = tan(alpha * 0.5);
    vec3 closest = eye + ray * closestTime;
    if (impact > EPSILON && k > EPSILON) {
        vec3 coordOrigin = closest + ray * (impact * k);
        float c = length(coordOrigin);
        vec3 xAxis = -coordOrigin / c;
        vec3 yAxis = normalize(ray - k * closest / impact);
        vec3 zAxis = cross(xAxis, yAxis);
        vec3 line = cross(zAxis, DiskNormal);
        float lineLength = length(line);
        outgoing = normalize(k * xAxis + yAxis);
        if (lineLength > EPSILON) {
            line /= lineLength;
            float b2 = c * c / (1.0 + k * k);
            float a2 = k * k * b2;
            // Same hyperbola x*x/a2 - y*y/b2 = 1 as the Shadertoy.
            // Parameterize the intersection line as position = line * distance:
            // x = c + distance*lx, y = distance*ly. This avoids dividing by
            // dot(xAxis,line), including the original denom == 0 special case.
            float lx = dot(xAxis, line);
            float ly = dot(yAxis, line);
            vec2 hits;
            int count = roots(b2 * lx * lx - a2 * ly * ly,
                              2.0 * b2 * c * lx, b2 * (c * c - a2), hits);
            float eyeY = dot(eye - coordOrigin, yAxis);
            if (count > 0) light += discIntersection(hits.x, line, xAxis, yAxis, c, eyeY,
                                                   impact, discRadius, eye, ray, geometryDistance);
            if (count > 1) light += discIntersection(hits.y, line, xAxis, yAxis, c, eyeY,
                                                   impact, discRadius, eye, ray, geometryDistance);
        }
    } else {
        // Straight-ray limit for the source's zero-deflection inner correction.
        float denominator = dot(ray, DiskNormal);
        if (abs(denominator) > EPSILON) {
            float t = -dot(eye, DiskNormal) / denominator;
            vec3 position = eye + ray * t;
            if (t >= 0.0 && t < min(closestTime, geometryDistance) && length(position) > R_BAR) {
                light = discColor(position, discRadius);
            }
        }
    }

    vec2 lensedUV = texCoord;
    vec4 projected = SceneProjection * vec4(outgoing, 0.0);
    if (impact >= R_BAR && geometryDistance * -ray.z > eye.z && projected.w > EPSILON) {
        vec2 candidate = projected.xy / projected.w * 0.5 + 0.5;
        float border = min(min(candidate.x, candidate.y), min(1.0 - candidate.x, 1.0 - candidate.y));
        float edgeFade = smoothstep(0.0, 0.06, border);
        float outerFade = 1.0 - smoothstep(4.0, INFLUENCE_RADIUS, impact / R_BAR);
        lensedUV = mix(texCoord, clamp(candidate, vec2(0.001), vec2(0.999)), edgeFade * outerFade);
        // Do not drag a foreground wall into the distorted background.
        if (sceneViewDepth(lensedUV) < eye.z) lensedUV = texCoord;
    }
    vec3 background = texture(SceneColor, lensedUV).rgb * (1.0 - shadow);
    // Keep scene colors intact; map only the Shadertoy's additive HDR emission.
    fragColor = vec4(background + (1.0 - exp(-light)), original.a);
}
