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
  const char *name;
  unsigned width;
  OperandLocation location;
};

struct Assignment
{
  Operand lhs;
  Operand rhs;
};

struct SemanticFunction {
  const char *isel;
  std::vector<Operand> writeOpers;
  std::vector<Operand> readOpers;
  std::vector<Assignment> RSDelta;
};

} // namespace transema


