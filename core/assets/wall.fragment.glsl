#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

// Do opacity based on deviation from rest

void main()
{
  gl_FragColor = v_color;
}