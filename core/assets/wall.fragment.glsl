#ifdef GL_ES
    precision mediump float;
#endif

varying float v_oscillationOffest;

// Do opacity based on deviation from rest

void main()
{
//    gl_FragColor = vec4(1);
    gl_FragColor = vec4(-v_oscillationOffest*2.0, abs(v_oscillationOffest*2.0), 0, 1);
//  gl_FragColor = vec4(abs(v_oscillationOffest), -v_oscillationOffest, v_oscillationOffest, 1);
}