#define pi2 6.283185307179586
#define WAVES 2

attribute vec4 a_position;

uniform mat4 u_projTrans;
uniform mat4 u_trans;

uniform vec4 u_wavesOrigin[WAVES];
uniform float u_wavesTheta[WAVES];
uniform float u_wavesAmplitude[WAVES];
uniform float u_wavesFreq[WAVES];

uniform float u_wavesWidth[WAVES];
uniform float u_wavesEnd[WAVES];


varying vec4 v_color;

void main() {
    vec4 globalPos = u_trans * a_position;

    vec4 oscillationOffset = vec4(0.0);

    for (int i = 0; i < WAVES; i++) {
        float wavesPeriod = 1.0 / u_wavesFreq[i];
        float oscillation = 0.0;
        float attenuation = 0.0;

        vec4 toMe = globalPos - u_wavesOrigin[i];
        float distToMe = length(toMe);
        toMe = toMe / distToMe;

        if (distToMe >= u_wavesEnd[i] - u_wavesWidth[i] && distToMe <= u_wavesEnd[i]) {
            oscillation = u_wavesAmplitude[i] * sin(u_wavesFreq[i] * (u_wavesTheta[i] + distToMe));
            attenuation = pow(
                sin(pi2 * (distToMe-(u_wavesEnd[i] - u_wavesWidth[i])) / u_wavesWidth[i]),
                2);

            oscillationOffset += toMe * ((oscillation * attenuation) / float(WAVES) );
        }
    }

    v_color = vec4(1);
    gl_Position = u_projTrans * (globalPos + 10.0*oscillationOffset);
}
