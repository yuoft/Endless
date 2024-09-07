#version 120

vec4 Ambient;
vec4 Diffuse;
vec4 Specular;
attribute float activelights;
varying vec3 position;
 
void pointLight(in int i, in vec3 normal, in vec3 eye, in vec3 ecPosition3) {
   float nDotVP;
   float nDotHV;
   float pf;
   float attenuation;
   float d;
   vec3  VP;
   vec3  halfVector;
   VP = vec3 (gl_LightSource[i].position) - ecPosition3;
   d = length(VP);
   VP = normalize(VP);
   attenuation = 1.0 / (gl_LightSource[i].constantAttenuation + gl_LightSource[i].linearAttenuation * d + gl_LightSource[i].quadraticAttenuation * d * d);
   halfVector = normalize(VP + eye);
   nDotVP = max(0.0, dot(normal, VP));
   pf = 0.0;
   Ambient  += gl_LightSource[i].ambient * attenuation;
   Diffuse  += gl_LightSource[i].diffuse * nDotVP * attenuation;
}
 
void spotLight(in int i, in vec3 normal, in vec3 eye, in vec3 ecPosition3) {
   float nDotVP;
   float nDotHV;
   float pf;
   float spotDot;
   float spotAttenuation;
   float attenuation;
   float d;
   vec3 VP;
   vec3 halfVector;
   VP = vec3 (gl_LightSource[i].position) - ecPosition3;
   d = length(VP);
   VP = normalize(VP);
   attenuation = 1.0 / (gl_LightSource[i].constantAttenuation + gl_LightSource[i].linearAttenuation * d + gl_LightSource[i].quadraticAttenuation * d * d);
   spotDot = dot(-VP, normalize(gl_LightSource[i].spotDirection));
   if (spotDot < gl_LightSource[i].spotCosCutoff) {
	   spotAttenuation = 0.0;
   } else {
	   spotAttenuation = pow(spotDot, gl_LightSource[i].spotExponent);
   }
   attenuation *= spotAttenuation;
   halfVector = normalize(VP + eye);
   nDotVP = max(0.0, dot(normal, VP));
   pf = 0.0;
   Ambient  += gl_LightSource[i].ambient * attenuation;
   Diffuse  += gl_LightSource[i].diffuse * nDotVP * attenuation;
}
 
void directionalLight(in int i, in vec3 normal) {
   float nDotVP;
   float nDotHV;
   float pf;
   nDotVP = max(0.0, dot(normal, normalize(vec3 (gl_LightSource[i].position))));
   pf = 0.0;
   Ambient  += gl_LightSource[i].ambient;
   Diffuse  += gl_LightSource[i].diffuse * nDotVP;
}
 
vec3 fnormal(void) {
	vec3 normal = gl_NormalMatrix * gl_Normal;
	normal = normalize(normal);
	return normal;
}
 
void ProcessLight(in int i, in vec3 normal, in vec3 eye, in vec3 ecPosition3) {
	if (gl_LightSource[i].spotCutoff==180.0) {
		if (gl_LightSource[i].position.w==0.0) {
			directionalLight(i, normal);
		} else {
			pointLight(i, normal, eye, ecPosition3);
		}
	} else {
	 spotLight(i,normal,eye,ecPosition3);
	}
}
 
void flight(in vec3 normal, in vec4 ecPosition, float alphaFade) {
	vec4 color;
	vec3 ecPosition3;
	vec3 eye;
	int i;
	ecPosition3 = (vec3 (ecPosition)) / ecPosition.w;
	eye = vec3 (0.0, 0.0, 1.0);
	Ambient  = vec4 (0.0);
	Diffuse  = vec4 (0.0);
	Specular = vec4 (0.0);
	if (activelights>0) {
		ProcessLight(0,normal,eye,ecPosition3);
	}
	if (activelights>1) {
		ProcessLight(1,normal,eye,ecPosition3);
	}
   color = gl_FrontLightModelProduct.sceneColor +
    Ambient * gl_FrontMaterial.ambient +
    Diffuse * gl_FrontMaterial.diffuse;
    color += Specular * gl_FrontMaterial.specular;
    color = clamp( color, 0.0, 1.0 );
    gl_FrontColor = color;
    gl_FrontColor.a *= alphaFade;
}
 
void main (void) {
	vec3  transformedNormal;
	float alphaFade = 1.0;
	vec4 ecPosition = gl_ModelViewMatrix * gl_Vertex;
	gl_Position = ftransform();
	transformedNormal = fnormal();
	flight(transformedNormal, ecPosition, alphaFade);
	gl_TexCoord[0] = gl_MultiTexCoord0;
	position = (gl_ModelViewMatrix * gl_Vertex).xyz;
}