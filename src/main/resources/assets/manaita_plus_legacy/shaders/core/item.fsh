#version 150

#define M_PI 3.1415926535897932384626433832795

#moj_import <fog.glsl>

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

uniform float time1;
uniform float yaw;
uniform float pitch;
uniform float externalScale;
uniform float opacity;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec3 fPos;               // 模型空间片段位置

out vec4 fragColor;

float hash3D(vec3 p) {
    return fract(sin(dot(p, vec3(127.1, 311.7, 74.7))) * 43758.5453);
}

// ========== 正确的 Simplex 噪声函数（3D）==========
vec3 mod289(vec3 x) { return x - floor(x * (1.0 / 289.0)) * 289.0; }
vec4 mod289(vec4 x) { return x - floor(x * (1.0 / 289.0)) * 289.0; }
vec4 permute(vec4 x) { return mod289(((x*34.0)+1.0)*x); }
vec4 taylorInvSqrt(vec4 r) { return 1.79284291400159 - 0.85373472095314 * r; }
float snoise(vec3 v) {
    const vec2 C = vec2(1.0/6.0, 1.0/3.0);
    const vec4 D = vec4(0.0, 0.5, 1.0, 2.0);

    vec3 i  = floor(v + dot(v, C.yyy));
    vec3 x0 = v - i + dot(i, C.xxx);

    vec3 g = step(x0.yzx, x0.xyz);
    vec3 l = 1.0 - g;
    vec3 i1 = min(g.xyz, l.zxy);
    vec3 i2 = max(g.xyz, l.zxy);

    vec3 x1 = x0 - i1 + C.xxx;
    vec3 x2 = x0 - i2 + C.yyy;
    vec3 x3 = x0 - D.yyy;

    i = mod289(i);
    vec4 p = permute( permute( permute( i.z + vec4(0.0, i1.z, i2.z, 1.0 )) + i.y + vec4(0.0, i1.y, i2.y, 1.0 )) + i.x + vec4(0.0, i1.x, i2.x, 1.0 ));
    float n_ = 0.142857142857;
    vec3  ns = n_ * D.wyz - D.xzx;

    vec4 j = p - 49.0 * floor(p * ns.z * ns.z);

    vec4 x_ = floor(j * ns.z);
    vec4 y_ = floor(j - 7.0 * x_);

    vec4 x = x_ * ns.x + ns.yyyy;
    vec4 y = y_ * ns.x + ns.yyyy;
    vec4 h = 1.0 - abs(x) - abs(y);
    vec4 b0 = vec4(x.xy, y.xy);
    vec4 b1 = vec4(x.zw, y.zw);

    vec4 s0 = floor(b0) * 2.0 + 1.0;
    vec4 s1 = floor(b1) * 2.0 + 1.0;
    vec4 sh = -step(h, vec4(0.0));      // ✅ 修正：vec4 sh

    vec4 a0 = b0.xzyw + s0.xzyw * sh.xxyy;
    vec4 a1 = b1.xzyw + s1.xzyw * sh.zzww;

    vec3 g0 = vec3(a0.xy, h.x);
    vec3 g1 = vec3(a0.zw, h.y);
    vec3 g2 = vec3(a1.xy, h.z);
    vec3 g3 = vec3(a1.zw, h.w);

    vec4 norm = taylorInvSqrt(vec4(dot(g0, g0), dot(g1, g1), dot(g2, g2), dot(g3, g3)));
    g0 *= norm.x;
    g1 *= norm.y;
    g2 *= norm.z;
    g3 *= norm.w;

    vec4 m = max(0.6 - vec4(dot(x0, x0), dot(x1, x1), dot(x2, x2), dot(x3, x3)), 0.0);
    m = m * m;
    return 42.0 * dot(m * m, vec4(dot(g0, x0), dot(g1, x1), dot(g2, x2), dot(g3, x3)));
}
void main(void) {
    vec4 mask = texture(Sampler0, texCoord0.xy);
	float time = fract(time1 * 0.0025) * 100.0;  // 保持在 0~1000 之间

    // 可选：限制 externalScale 变化范围
    float safeScale = clamp(externalScale, 0.5, 2.0);

    // 背景底色（不变）
    float pulse = fract(time * 0.12);
    vec4 col = vec4(0.0, 0.0, 0.0, 1.0);
    col.r = 0.05 + 0.04 * sin(pulse * M_PI * 2.0);
    col.g = 0.06 + 0.05 * cos(pulse * M_PI * 2.0 + 2.0);
    col.b = 0.18 + 0.12 * sin(pulse * M_PI * 2.0 + 4.0);

    vec3 dir = normalize(-fPos);

    // 视角旋转（不变）
    float sb = sin(pitch);
    float cb = cos(pitch);
    dir = vec3(dir.x, dir.y * cb - dir.z * sb, dir.y * sb + dir.z * cb);
    float sa = sin(-yaw);
    float ca = cos(-yaw);
    dir = vec3(dir.z * sa + dir.x * ca, dir.y, dir.z * ca - dir.x * sa);

    vec3 nebulaColor = vec3(0.0);

    for (int i = 0; i < 5; i++) {
        float fi = float(i);
        vec3 axis = normalize(vec3(sin(fi * 12.9898), cos(fi * 78.233), sin(fi * 45.164)));
        float angle = mod(fi * 2.399, 2.0 * M_PI);
        float c = cos(angle);
        float s = sin(angle);
        vec3 rotatedDir = dir * c + cross(axis, dir) * s + axis * dot(axis, dir) * (1.0 - c);

        float scale = 2.5 + fi * 1.2;
        vec3 samplePos = rotatedDir * scale * safeScale + time * 0.03 * (fi + 1.0);


        float n = snoise(samplePos * 1.5);

        // 将噪声映射到 [0,1] 并增强对比度
        n = n * 0.5 + 0.5;
        n = pow(n, 2.0);

        vec3 layerColor;
        if (i == 0) layerColor = vec3(0.7, 0.2, 0.9);
        else if (i == 1) layerColor = vec3(0.2, 0.6, 1.0);
        else if (i == 2) layerColor = vec3(1.0, 0.4, 0.3);
        else if (i == 3) layerColor = vec3(0.3, 1.0, 0.5);
        else layerColor = vec3(0.9, 0.2, 0.5);

        nebulaColor += layerColor * n * 0.4;
    }

    // 星光粒子：原版色调 + 往复位移 + 微弱闪光
    float starDensity = 40.0;
    vec3 starSample = dir * 3.0 + time * 0.002;
    float star = 0.0;

    for (int j = 0; j < 6; j++) {
        vec3 offset = vec3(float(j) * 0.7);
        vec3 samplePos = starSample + offset;
        vec3 cell = floor(samplePos * starDensity);

        // 随机方向（用于位移）
        vec3 randDir = normalize(vec3(
            hash3D(cell + vec3(0.1, 0.0, 0.0)) * 2.0 - 1.0,
            hash3D(cell + vec3(0.0, 0.1, 0.0)) * 2.0 - 1.0,
            hash3D(cell + vec3(0.0, 0.0, 0.1)) * 2.0 - 1.0
        ));

        // 往复位移参数（幅度小，保持星星接近原位）
        float speed = 1.0 + hash3D(cell + vec3(0.5)) * 2.0;   // 1.0 ~ 3.0
        float amplitude = 0.04;                                          // 位移量（可调）
        vec3 displacement = randDir * sin(time * speed) * amplitude;

        // 位移后采样位置
        vec3 displacedSample = samplePos + displacement;
        vec3 displacedCell = floor(displacedSample * starDensity);

        // 星星出现判断
    	float h = hash3D(displacedCell);
    		float brightness = smoothstep(0.97, 1.0, h);

    		float twinkle = sin(time * 10.0 + h * 20.0) * 0.375 + 0.625;
    		float flash = pow(sin(time * 7.7 + h * 67.0) * 0.5 + 0.5, 16.0);
    		float twinkleEnhanced = twinkle + flash * 0.5;

    		star += brightness * twinkleEnhanced * 0.75;
    }

    vec3 starColor = vec3(0.9, 0.95, 1.0);   // 原版星色
    col.rgb += starColor * star;             // 原版叠加方式

    // 边缘发光（不变）
    float edgeGlow = 1.0 - abs(dot(dir, vec3(0.0, 0.0, 1.0)));
    edgeGlow = pow(edgeGlow, 3.0) * 0.3;
    col.rgb += vec3(0.5, 0.7, 1.0) * edgeGlow;

    // 光照混合（不变）
    float lightMix = 0.25;
    vec3 shade = vertexColor.rgb * lightMix + vec3(1.0 - lightMix);
    col.rgb *= shade;

    col.a *= mask.r;
    col.rgb *= 1.15;
    col = clamp(col, 0.0, 1.0);

    fragColor = linear_fog(col * ColorModulator, vertexDistance, FogStart, FogEnd, FogColor);
}