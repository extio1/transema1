#pragma once

#include <jni.h>

#include <llvm/IR/LLVMContext.h>
#include <llvm/IR/IRBuilder.h>
#include <llvm/IR/Module.h>

#include <memory>

class LLVMCodegen {
public:
  LLVMCodegen(llvm::StringRef ModuleName);

  void emit(JNIEnv *Env, jobject Obj, jobject SemanticFuction);

  ~LLVMCodegen();

private:
  llvm::LLVMContext &Context;
  llvm::Module &MainModule;
  llvm::IRBuilder<> &Builder;
};
