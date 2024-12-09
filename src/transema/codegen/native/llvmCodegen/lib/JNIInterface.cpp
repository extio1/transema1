#include "transema_codegen_LLVMCodegen.h"

#include "LLVMCodegen/LLVMCodegen.h"
#include "LLVMCodegen/SemanticFunction.h"

#include <iostream>
#include <vector>

using namespace transema;

namespace {

LLVMCodegen* getNativePtr(JNIEnv *Env, jobject Obj) {
  jfieldID nativePtrField = Env->GetFieldID(Env->GetObjectClass(Obj), "nativeState", "J");
  jlong ptr = Env->GetLongField(Obj, nativePtrField);
  return reinterpret_cast<LLVMCodegen*>(ptr);
}

} // anonymous namespace

JNIEXPORT void JNICALL 
Java_transema_codegen_LLVMCodegen_emit
(JNIEnv *Env, jobject Obj, jobject SemaFunc)
{
  auto state = getNativePtr(Env, Obj);

  using namespace transema::semafunc;
  // TODO: Parse SemanticFunction from .yaml probably
  SemanticFunction SF = 
    {
    .isel = "MOV_GPR64_GPR64", 
    .writeOpers = {
      std::vector<Operand> {
        {.name = "R1", .width = 64, .location = OperandLocation::gpr}
      }
    },
    .readOpers = {
      std::vector<Operand> {
        {.name = "R2", .width = 64, .location = OperandLocation::gpr}
      }
    },
    .RSDelta = {
      std::vector<Assignment> {
        (Assignment){ .lhs = {.name = "R1", .width = 64, .location = OperandLocation::gpr}, 
                      .rhs = {.name = "R2", .width = 64, .location = OperandLocation::gpr} }
      }
    }
  };
  state->emit(SF);
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
