#include <jni.h>
#include <stdio.h>
#include "nrlmsise-00.h"
#include "com_github_qcha_utils_NativeNRLMSISE.h"


JNIEXPORT jdouble JNICALL Java_com_github_qcha_utils_NativeNRLMSISE_densityNow
  (JNIEnv *env, jclass thisClass, jdouble x, jdouble y, jdouble z, jint doy, jint year, jint sec) {
  	struct nrlmsise_output output;
	struct nrlmsise_input input;
  	struct nrlmsise_flags flags;
	struct ap_array aph;
	int i;
	int j;
	/* input values */
  	for (i=0;i<7;i++)
		aph.a[i]=100;
	aph.a[0]=4;
	flags.switches[0]=0;
  	for (i=1;i<24;i++)
  		flags.switches[i]=1;
	
	input.doy=doy;
	input.year=year; /* without effect */
  	input.sec=sec;
	input.alt=z/1000;
	input.g_lat=x;
	input.g_long=y;
	input.lst=sec/3600+y/15;
	input.f107A=150;
	input.f107=150;
	input.ap=4;
	input.ap_a=&aph;
	
  	gtd7(&input, &flags, &output);
	
	return output.d[5]*1000;
}
