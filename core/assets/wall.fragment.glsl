#ifdef GL_ES
    precision mediump float;
#endif

uniform sampler2D u_texture;

varying vec2 v_texcoords;
varying float v_oscillationOffest;

// Do opacity based on deviation from rest

void main()
{
    vec4 tex = texture2D(u_texture, v_texcoords);
//    gl_FragColor = vec4(1) + tex;
    gl_FragColor = vec4(-v_oscillationOffest*2.0, abs(v_oscillationOffest*2.0), 0, 1) + tex;
//  gl_FragColor = vec4(abs(v_oscillationOffest), -v_oscillationOffest, v_oscillationOffest, 1);
}