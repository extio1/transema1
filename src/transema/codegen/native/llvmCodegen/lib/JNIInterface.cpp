#include "transema_codegen_LLVMCodegen.h"

#include "LLVMCodegen/LLVMCodegen.h"

#include <iostream>

JNIEXPORT void JNICALL 
Java_transema_codegen_LLVMCodegen_emit
(JNIEnv *, jobject, jobject)
{
  std::cout << "Emitting LLVM IR" << std::endl;
}


JNIEXPORT void JNICALL 
Java_transema_codegen_LLVMCodegen_createNewModule
(JNIEnv *Env, jobject Obj, jstring ModuleName)
{
  LLVMCodegen* state = new LLVMCodegen();

  jclass cls = Env->GetObjectClass(Obj);
  jfieldID nativePtrField = Env->GetFieldID(cls, "nativeState", "J");

  Env->SetLongField(Obj, nativePtrField, reinterpret_cast<jlong>(state));
}

JNIEXPORT void JNICALL 
Java_transema_codegen_LLVMCodegen_cleanNativeState
(JNIEnv *Env, jobject Obj)
{
  jfieldID nativePtrField = Env->GetFieldID(Env->GetObjectClass(Obj), "nativePtr", "J");

  jlong ptr = Env->GetLongField(Obj, nativePtrField);
  LLVMCodegen* state = reinterpret_cast<LLVMCodegen*>(ptr);
  delete state;

  Env->SetLongField(Obj, nativePtrField, 0);
}