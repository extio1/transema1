#include "LLVMCodegen/LLVMCodegen.h"
#include "SemanticFunction/SemanticFunction.h"

#include <llvm/Support/CommandLine.h>

using namespace transema;
using namespace transema::semafunc;

// TODO USE cl::opt for options (dependencies issues)
// static llvm::cl::opt<std::string> OutputFilename("o", llvm::cl::desc("The name of the output file containing LLVM IR"), llvm::cl::Required);
// static llvm::cl::opt<std::string> PathToSemaFuncDescs("s", llvm::cl::desc("Path to directory with .yaml files with semantic function descriptions"), llvm::cl::Required);

int main(int argc, char **argv) {
  // TODO USE cl::opt for options (dependencies issues)
  // llvm::cl::ParseCommandLineOptions(argc, argv);
  // LLVMCodegen codegen(OutputFilename);
  
  LLVMCodegen codegen("test_semantic.ll");
  const char* PathToSemaFuncDescs = "/home/extio1/mnt/edu/diploma/transema1/src/semantics"; // TODO: exterminate hardcode 

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