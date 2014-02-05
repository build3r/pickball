#include <jni.h>
#include<stdlib.h>

//generate random array
jintArray Java_com_example_pickball_MainActivity_randomUnique(JNIEnv* env,
		jobject javaThis, jint iL) {
	//return (pointer)
	jintArray ret;
	//Create the instance to return
	ret = (*env)->NewIntArray(env, (jsize) iL);
	if (ret == NULL) {
		return NULL;
	}
	jint arrayL[iL];
	int iNew, iLT, iL1;
	for (iL1 = 0; iL1 < iL; iL1++) {
		arrayL[iL1]=-1;
	}
	iLT = 0;
	while (iLT < iL) {
		iNew = rand() % 10;
		do {
			if (iNew < iL)
				break;
			iNew = rand() % 10;
		} while (1);
		iL1 = 0;
		for (iL1 = 0; iL1 < iL; iL1++) {
			if (iNew == arrayL[iL1]) {
				break;
			}
		}
		if (iL1 == iL) {
			arrayL[iLT] = iNew;
			iLT = iLT + 1;
		}
	}
	(*env)->SetIntArrayRegion(env, ret, 0, iL, arrayL);

	// int array
	return ret;

}
// check character frequency
jboolean Java_com_example_pickball_MainActivity_charFrequencyTest(JNIEnv* env,
		jobject javaThis, jstring scheck) {
	jboolean bresult = 0;
	const char *nativeString = (*env)->GetStringUTFChars(env, scheck, 0);char ch;
	int iSL = strlen(nativeString),iStart,ichcounter;
	if (iSL > 0) {
		ch = nativeString[0];
		iStart = 1; ichcounter = 1;
		for (; iStart < iSL; iStart++) {
			char chl = nativeString[iStart];//scheck.charAt(iStart);
			if (ch == chl) {
				ichcounter++;
			}
			if (ichcounter > 2) {
				bresult = 1;
				break;
			}
		}
	} else {
		bresult = 1;
	}
	return bresult;

}
// check special character
jboolean Java_com_example_pickball_MainActivity_checkSpecialCharacter(JNIEnv* env,
		jobject javaThis, jstring scheck) {
	jboolean bresult = 0;
	const char *nativeString = (*env)->GetStringUTFChars(env, scheck, 0);char ch;
	int iSL = strlen(nativeString),iStart;
	for (iStart = 0; iStart < iSL; iStart++) {
		ch = nativeString[iStart];
		if ((ch >= 'A' && ch <= 'Z')
				|| (ch >= 'a' && ch <= 'z')
				|| (ch >= '0' && ch <= '9')) {
		} else {
			bresult = 1;
			break;
		}
	}
	return bresult;
}

