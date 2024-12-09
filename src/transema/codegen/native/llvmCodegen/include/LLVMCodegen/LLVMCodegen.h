#pragma once

#include "SemanticFunction.h"
#include "EmitFunctionContext.h"

// #include <jni.h>

#include <llvm/IR/LLVMContext.h>
#include <llvm/IR/IRBuilder.h>
#include <llvm/IR/Module.h>

#include <memory>

namespace transema {

class LLVMCodegen {
public:
  LLVMCodegen(llvm::StringRef ModuleName);

  void emit(const semafunc::SemanticFunction &SF);

  void printModule();

  ~LLVMCodegen();
private:
  llvm::LLVMContext &Context;
  llvm::Module &MainModule;
  llvm::IRBuilder<> &Builder;
private: // Transema intrinsics //
  // Function *__transema_write_gpr_gpr;
  // void generateIntrinsic_write_gpr_gpr();
private:
  static const unsigned AddressSpace = 0;
  llvm::Function *getFunctionDeclaration(const semafunc::SemanticFunction &SF, semafunc::EmitFunctionContext &EFC) const;
  void emitStateDelta(const semafunc::EmitFunctionContext &EFC, const semafunc::SemanticFunction &SF, llvm::Function *F);
};

} // namespace transema
