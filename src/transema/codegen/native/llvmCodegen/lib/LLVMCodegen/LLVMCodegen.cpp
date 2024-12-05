#include "LLVMCodegen/LLVMCodegen.h"

#include <iostream>

using namespace llvm;

LLVMCodegen::LLVMCodegen(StringRef ModuleName): 
  Context(*new LLVMContext()), 
  MainModule(*new Module(ModuleName, Context)),
  Builder(*new IRBuilder<>(Context))
{ 
  std::cout << "Create module and stuff like that\n";
}

LLVMCodegen::~LLVMCodegen() {}

void LLVMCodegen::emit(JNIEnv *Env, jobject Obj, jobject SemanticFuction) 
{
  jclass cls = Env->GetObjectClass(Obj);
  jmethodID getWriteArgsMethodId = Env->GetMethodID(cls, "getWriteArgs", "()Ljava/util/List;");
  jmethodID getReadArgsId = Env->GetMethodID(cls, "getReadArgs", "()Ljava/util/List;");
  jmethodID getSemanticStateChangeId = Env->GetMethodID(cls, "getSemanticStateChange", "()Ljava/util/List;");

  jobject writeArgsList = Env->CallObjectMethod(Obj, getWriteArgsMethodId);
  jobject readArgsList = Env->CallObjectMethod(Obj, getReadArgsId);
  jobject semanticChangeList = Env->CallObjectMethod(Obj, getSemanticStateChangeId);

  
}


