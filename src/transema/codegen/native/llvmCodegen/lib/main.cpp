#include "LLVMCodegen/LLVMCodegen.h"
#include "LLVMCodegen/SemanticFunction.h"

using namespace transema;
using namespace transema::semafunc;

int main(int argc, char **argv) {
  LLVMCodegen codegen("test_semantic.ll");
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

  codegen.emit(SF);
  codegen.printModule();
  return 0;
}