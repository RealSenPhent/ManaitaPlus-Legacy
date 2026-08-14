#version 400

#define M_PI 3.1415926535897932384626433832795

#moj_import <fog.glsl>

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float time;
uniform float yaw;
uniform float pitch;
uniform float externalScale;
uniform float opacity;
uniform mat2 cosmicuvs[10];
uniform vec4 cosmicColor0;

in float vertexDistance;
in vec4 vertexColor;
in vec3 fPos;

out vec4 fragColor;

float hash(vec2 p) { return fract(sin(dot(p, vec2(127.1,311.7)))*43758.5453); }

void main(void) {
    if (vertexColor.r==1) return;
    vec4 mask = vertexColor;
    vec4 col = mask;

    // 下落方向（全局稳定）
    vec3 rainDir = vec3(0.0, -1.0, 0.0);
    float sb = sin(pitch), cb = cos(pitch);
    rainDir = vec3(rainDir.x, rainDir.y*cb - rainDir.z*sb, rainDir.y*sb + rainDir.z*cb);
    float sa = sin(-yaw), ca = cos(-yaw);
    rainDir = normalize(vec3(rainDir.z*sa + rainDir.x*ca, rainDir.y, rainDir.z*ca - rainDir.x*sa));

    // 轻微空间扭曲
    float r = length(fPos.xz);
    float a = atan(fPos.z, fPos.x);
    vec2 warp = vec2(cos(a), sin(a)) * sin(r * 2.0 - time * 3.0) * 0.03 * externalScale;
    vec3 fPosWarped = fPos + vec3(warp.x, 0.0, warp.y);

    // 屏幕空间偏导数（用于十字星芒）
    vec2 screenDx = dFdx(fPosWarped.xy);
    vec2 screenDy = dFdy(fPosWarped.xy);
    vec2 screenRight = normalize(screenDx);
    vec2 screenUp = normalize(screenDy);

    float cellSize = 0.25 / max(externalScale, 0.1);
    float speed = 1.2 * externalScale;
    float yRange = 2.5 / max(externalScale, 0.3);
    float pointRadius = 0.012;        // 光晕基准大小
    float brightnessMul = 2.5;        // 整体亮度

    vec2 grid = fPosWarped.xz / cellSize;
    vec2 cell = floor(grid);

    for (int dx = -1; dx <= 1; ++dx)
    for (int dz = -1; dz <= 1; ++dz) {
        vec2 thisCell = cell + vec2(float(dx), float(dz));
        for (int di = 0; di < 4; ++di) {
            float h0 = hash(thisCell + vec2(0.0, float(di)));
            float h1 = hash(thisCell + vec2(1.0, float(di)));
            float h2 = hash(thisCell + vec2(2.0, float(di)));
            float h3 = hash(thisCell + vec2(3.0, float(di)));

            vec2 offset = vec2(h0, h1) * cellSize;
            float yPhase = fract(h2 + time * speed * 0.12);
            float centerY = fPosWarped.y - mod(fPosWarped.y - yPhase * yRange, yRange) + yRange * 0.5;
            vec3 center = vec3(thisCell.x * cellSize + offset.x, centerY, thisCell.y * cellSize + offset.y);

            // 方向微弧
            vec3 localDir = rainDir;
            if (h1 > 0.5) {
                vec3 perp = abs(rainDir.y) < 0.9 ? vec3(0,1,0) : vec3(1,0,0);
                vec3 side = normalize(cross(rainDir, perp));
                float offPhase = yPhase * 6.283 + time;
                localDir = normalize(rainDir + side * sin(offPhase) * 0.08);
            }

            vec3 up = localDir;
            vec3 right = normalize(cross(up, vec3(0,1,0)));
            if (length(right) < 0.01) right = normalize(cross(up, vec3(1,0,0)));
            vec3 forward = cross(right, up);

            float count = mix(2.0, 3.0, h3);
            for (int s = 0; s < int(count); ++s) {
                float hs = hash(thisCell + vec2(0.5 + float(s), float(di) + 0.5));
                float scatter = 0.04;
                vec3 localOffset = vec3(
                    (hs - 0.5) * scatter,
                    (hash(thisCell + vec2(1.5, float(s))) - 0.5) * scatter * 1.5,
                    (hash(thisCell + vec2(2.5, float(s))) - 0.5) * scatter
                );
                vec3 pointPos = center + right * localOffset.x + up * localOffset.y + forward * localOffset.z;

                vec3 delta = fPosWarped - pointPos;
                float d = length(delta);

                float fps = pointRadius;
                float fps2 = fps * fps;

                // ⚠️ 不再使用核心亮点，仅保留 halo + glow + rays
                float halo = (fps * 0.3) / (sqrt(d) + fps * 0.2);
                float glow = (fps2 * 0.5) / (d*d + fps2 * 0.25);

                // 十字星芒
                vec2 deltaScreen = vec2(dot(delta, right), dot(delta, up));
                float dx = dot(deltaScreen, screenRight);
                float dy = dot(deltaScreen, screenUp);
                float rayWidthK = 1.0 / (fps2 * 0.09);
                float rayLenK   = 1.0 / (fps * 4.0);
                float hStreak = exp(-dy*dy*rayWidthK) * exp(-abs(dx)*rayLenK);
                float vStreak = exp(-dx*dx*rayWidthK) * exp(-abs(dy)*rayLenK);
                float rays = (hStreak + vStreak) * 0.4;

                float shade = (halo + glow + rays) * brightnessMul;

                float fade = 1.0 - abs(yPhase - 0.5) * 2.0;
                fade = smoothstep(0.0, 0.2, fade);
                shade *= fade;

                // 色彩：不使用白色核心，保留淡彩色
                float id = h2 * 6.283;
                vec3 neon = 0.5 + 0.5 * cos(time * 2.0 + id + vec3(0.0, 2.0, 4.0));
                float warm = smoothstep(0.9, 1.0, h3);
                neon = mix(neon, vec3(1.0, 0.8, 0.4), warm * 0.4);

                vec3 rainColor = neon * shade;
                float viewDist = length(center);
                rainColor *= 1.0 / (1.0 + viewDist * 0.3);

                col.rgb += rainColor;
            }
        }
    }

    // 色调映射：轻微压缩，防止过亮
    col.rgb = tanh(col.rgb * 1.2);
    col.rgb = clamp(col.rgb, 0.0, 1.0);

    col.a *= mask.a * opacity;
    col = clamp(col, 0.0, 1.0);
    fragColor = ColorModulator * linear_fog(col * 1.25, vertexDistance, FogStart, FogEnd, FogColor);
}