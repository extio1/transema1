/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class transema_codegen_LLVMCodegen */

#ifndef _Included_transema_codegen_LLVMCodegen
#define _Included_transema_codegen_LLVMCodegen
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     transema_codegen_LLVMCodegen
 * Method:    emit
 * Signature: (Ltransema/SemanticFunction;)V
 */
JNIEXPORT void JNICALL Java_transema_codegen_LLVMCodegen_emit
  (JNIEnv *, jobject, jobject);

/*
 * Class:     transema_codegen_LLVMCodegen
 * Method:    createNewModule
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_transema_codegen_LLVMCodegen_createNewModule
  (JNIEnv *, jobject, jstring);

/*
 * Class:     transema_codegen_LLVMCodegen
 * Method:    cleanNativeState
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_transema_codegen_LLVMCodegen_cleanNativeState
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif