#include "LLVMCodegen/LLVMCodegen.h"
#include "SemanticFunction/SemanticFunction.h"
#include "SemanticFunction/EmitFunctionContext.h"

#include <llvm/IR/Type.h>

#include <iostream>

using namespace llvm;
using namespace transema;

LLVMCodegen::LLVMCodegen(StringRef ModuleName): 
  Context(*new LLVMContext()), 
  MainModule(*new Module(ModuleName, Context)),
  Builder(*new IRBuilder<>(Context))
{ 
  // generateIntrinsic_write_gpr_gpr();
}

void LLVMCodegen::printModule() {
  MainModule.print(outs(), nullptr); // WHY printModule(llvm::outs()) doesn't works 
}

void LLVMCodegen::printModule(llvm::raw_fd_stream &OS) {
  MainModule.print(OS, nullptr);
}

LLVMCodegen::~LLVMCodegen() {}

void LLVMCodegen::emit(const SemanticFunction &SF) {
  EmitFunctionContext EFC;
  Function *F = getFunctionDeclaration(SF, EFC);

  BasicBlock *BB = BasicBlock::Create(Context, "entry", F);
  Builder.SetInsertPoint(BB);

  emitStateDelta(EFC, SF, F);

  Builder.CreateRet(F->getArg(0)); // return %memory
}

void LLVMCodegen::emitStateDelta(const EmitFunctionContext &EFC, const SemanticFunction &SF, llvm::Function *F) {
  for (auto &RSChange : SF.RSDelta) {
    auto &lhs = RSChange.lhs;
    auto &rhs = RSChange.rhs;

    if(lhs.location == OperandLocation::gpr && (rhs.location == OperandLocation::imm || rhs.location == OperandLocation::gpr)) {
      Builder.CreateStore(EFC.NamedValues.at(rhs.name), EFC.NamedValues.at(lhs.name), false);
    } else if(lhs.location == OperandLocation::gpr && rhs.location == OperandLocation::mem) {
      auto Ptr = Builder.CreateIntToPtr(EFC.NamedValues.at(rhs.name), PointerType::get(Context, AddressSpace));
      auto Val = Builder.CreateLoad(Type::getInt64Ty(Context), Ptr);
      Builder.CreateStore(Val, EFC.NamedValues.at(lhs.name), false);
    } else {
      llvm_unreachable("unhandled operands locations");
    }
  }
}

Function *LLVMCodegen::getFunctionDeclaration(const SemanticFunction &SF, EmitFunctionContext &EFC) const {
  std::vector<std::string> ArgsNames{"memory", "state"};
  std::vector<Type *> InputArgumentsFuncType{ // initialize with pointers to memory, state
    PointerType::get(Context, AddressSpace),
    PointerType::get(Context, AddressSpace)
  };

  for(auto &OName : SF.writeOpers) {
    InputArgumentsFuncType.push_back(PointerType::get(Context, AddressSpace)); // all arguments for write are of type ref
    ArgsNames.push_back(OName);
  }

  for(auto &OName : SF.readOpers) {
    InputArgumentsFuncType.push_back(Type::getInt64Ty(Context)); // all arguments for read are of type i64
    ArgsNames.push_back(OName);
  }

  FunctionType *FT = FunctionType::get(PointerType::get(Context, AddressSpace), InputArgumentsFuncType, false);

  // TODO change linkage to Internal + make global var referring this function
  Function *F = Function::Create(FT, Function::InternalLinkage, Twine(SF.isel)+"_func", MainModule);
  GlobalVariable *GV = new GlobalVariable(MainModule, F->getType(), true, GlobalValue::ExternalLinkage, F, Twine(SF.isel));

  for(int i = 0; i < ArgsNames.size(); i++) {
    auto *Arg = F->getArg(i);
    Arg->setName(ArgsNames[i]);
    EFC.NamedValues.insert({ArgsNames[i], Arg});
  }

  return F;
}

// void LLVMCodegen::generateIntrinsic_write_gpr_gpr() {
//   std::vector<const char*> ArgsNames{"memory", "state"};
//   std::vector<Type *> InputArgumentsFuncType{ // initialize with pointers to memory, state
//     PointerType::get(Context, AddressSpace),
//     PointerType::get(Context, AddressSpace)
//   };

//   for(auto &O : SF.writeOpers) {
//     InputArgumentsFuncType.push_back(PointerType::get(Context, AddressSpace));
//     ArgsNames.push_back(O.name);
//   }

//   for(auto &O : SF.readOpers) {
//     InputArgumentsFuncType.push_back(Type::getInt64Ty(Context)); // all arguments for read are of type i64
//     ArgsNames.push_back(O.name);
//   }

//   FunctionType *FT = FunctionType::get(PointerType::get(Context, AddressSpace), InputArgumentsFuncType, false);

//   // TODO change linkage to Internal + make global var referring this function
//   Function *F = Function::Create(FT, Function::ExternalLinkage, Twine(SF.isel)+"_func", MainModule);

//   __transema_write_gpr_gpr = F;
// }


