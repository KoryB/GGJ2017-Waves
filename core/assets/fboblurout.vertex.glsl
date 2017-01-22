//combined projection and view matrix
uniform mat4 u_projTrans;

//"in" attributes from our SpriteBatch
attribute vec2 a_position;
attribute vec2 a_texcoords0;
attribute vec4 a_color;

//"out" varyings to our fragment shader
varying vec4 vColor;
varying vec2 vTexCoord;

void main() {
	vColor = a_color;
	vTexCoord = a_texcoords0;
	gl_Position = u_projTrans * vec4(a_position, 0.0, 1.0);
}