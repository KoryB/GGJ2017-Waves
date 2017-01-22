#define pi2 6.283185307179586
#define WAVES 10

attribute vec4 a_position;
attribute vec2 a_texCoord;

uniform mat4 u_projTrans;
uniform mat4 u_trans;

uniform vec4 u_wavesOrigin[WAVES];
uniform float u_wavesTheta[WAVES];
uniform float u_wavesAmplitude[WAVES];
uniform float u_wavesFreq[WAVES];

uniform float u_wavesWidth[WAVES];
uniform float u_wavesEnd[WAVES];
uniform float u_wavesFinal[WAVES];
uniform float u_wavesActive;

varying vec2 v_texcoords;
varying float v_oscillationOffest;

void main() {
    vec4 globalPos = u_trans * a_position;

    vec4 oscillationOffset = vec4(0.0);
    float totalOscillation = 0.0;

    for (int i = 0; i < WAVES; i++) {
        float oscillation = 0.0;
        float attenuation = 0.0;
        float distAttenuation = 0.0;

        vec4 toMe = globalPos - u_wavesOrigin[i];
        float distToMe = length(toMe);
        toMe = toMe / distToMe;

        if (distToMe >= u_wavesEnd[i] - u_wavesWidth[i] && distToMe <= u_wavesEnd[i]) {
            oscillation = u_wavesAmplitude[i] * sin(u_wavesFreq[i] * (u_wavesTheta[i] + distToMe));
            distAttenuation = ((u_wavesFinal[i] - distToMe) / u_wavesFinal[i]);
            attenuation = pow(
                sin(pi2 * (distToMe-(u_wavesEnd[i] - u_wavesWidth[i])) / u_wavesWidth[i]), 2
            ) * pow(distAttenuation, 0.5);

            oscillationOffset += toMe * (oscillation * attenuation);
            totalOscillation += (oscillation * attenuation) / (u_wavesAmplitude[i]);
        }
    }

    v_texcoords = a_texCoord;
    v_oscillationOffest = totalOscillation;
    gl_Position = u_projTrans * (globalPos + oscillationOffset);
}
