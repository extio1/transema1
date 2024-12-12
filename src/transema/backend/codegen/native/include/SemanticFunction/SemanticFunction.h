#pragma once

#include <llvm/IR/Type.h>
#include <vector>

namespace transema {

enum class OperandLocation {
  gpr,
  flag,
  imm,
  mem
};

struct Operand {
  std::string name;
  uint8_t width;
  OperandLocation location;
};

struct Assignment{
  Operand lhs;
  Operand rhs;
};

struct SemanticFunction {
  std::string isel;
  std::vector<std::string> writeOpers;
  std::vector<std::string> readOpers;
  std::vector<Assignment> RSDelta;
};

} // namespace transema


