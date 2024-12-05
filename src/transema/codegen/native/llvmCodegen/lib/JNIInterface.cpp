#include "transema_codegen_LLVMCodegen.h"

#include "LLVMCodegen/LLVMCodegen.h"

#include <iostream>

namespace {

LLVMCodegen* getNativePtr(JNIEnv *Env, jobject Obj) {
  jfieldID nativePtrField = Env->GetFieldID(Env->GetObjectClass(Obj), "nativeState", "J");
  jlong ptr = Env->GetLongField(Obj, nativePtrField);
  return reinterpret_cast<LLVMCodegen*>(ptr);
}

} // anonymous namespace

JNIEXPORT void JNICALL 
Java_transema_codegen_LLVMCodegen_emit
(JNIEnv *Env, jobject Obj, jobject SemanticFunction)
{
  auto state = getNativePtr(Env, Obj);
  state->emit(Env, Obj, SemanticFunction);
}


JNIEXPORT void JNICALL 
Java_transema_codegen_LLVMCodegen_createNewModule
(JNIEnv *Env, jobject Obj, jstring ModuleName)
{
  const char *cModuleName = Env->GetStringUTFChars(ModuleName, NULL);
  LLVMCodegen* state = new LLVMCodegen(cModuleName);

  jclass cls = Env->GetObjectClass(Obj);
  jfieldID nativePtrField = Env->GetFieldID(cls, "nativeState", "J");

  Env->SetLongField(Obj, nativePtrField, reinterpret_cast<jlong>(state));
}

JNIEXPORT void JNICALL 
Java_transema_codegen_LLVMCodegen_cleanNativeState
(JNIEnv *Env, jobject Obj)
{
  auto state = getNativePtr(Env, Obj);
  delete state;
}
