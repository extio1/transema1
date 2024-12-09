#pragma once

#include "SemanticFunction.h"

#include <llvm/IR/Value.h>
#include <map>

namespace transema {
namespace semafunc {

// class EmitFunctionContext {
//     explicit EmitFunctionContext(const SemanticFunction &SF);

//     void addNamedMapping(std::string oper, llvm::Value *Value);
//     llvm::Value *getValueByName();
// public:
//     const SemanticFunction &SF;
//     std::map<std::string, llvm::Value *> OperandToValue;
// };

struct EmitFunctionContext {
    std::map<std::string, llvm::Value *> NamedValues;
};

} // namespace semafunc
} // namespace transema