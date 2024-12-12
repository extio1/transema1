#pragma once

#include "SemanticFunction/SemanticFunction.h"
#include "SemanticFunction/EmitFunctionContext.h"

#include <llvm/IR/LLVMContext.h>
#include <llvm/IR/IRBuilder.h>
#include <llvm/IR/Module.h>

#include <memory>

namespace transema {

class LLVMCodegen {
public:
  LLVMCodegen(llvm::StringRef ModuleName);

  void emit(const SemanticFunction &SF);

  void printModule();
  void printModule(llvm::raw_fd_stream &OS);

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
  llvm::Function *getFunctionDeclaration(const SemanticFunction &SF, EmitFunctionContext &EFC) const;
  void emitStateDelta(const EmitFunctionContext &EFC, const SemanticFunction &SF, llvm::Function *F);
};

} // namespace transema
