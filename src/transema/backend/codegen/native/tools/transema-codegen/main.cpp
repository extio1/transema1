#include "LLVMCodegen/LLVMCodegen.h"
#include "YamlParser/YamlParser.h"
#include "SemanticFunction/SemanticFunction.h"

#include <llvm/Support/CommandLine.h>
#include <llvm/Support/raw_ostream.h>

using namespace transema;

// TODO USE cl::opt for options (dependencies issues)
// static llvm::cl::opt<std::string> OutputFilename("o", llvm::cl::desc("The name of the output file containing LLVM IR"), llvm::cl::Required);
// static llvm::cl::opt<std::string> PathToSemaFuncDescs("s", llvm::cl::desc("Path to directory with .yaml files with semantic function descriptions"), llvm::cl::Required);

int main(int argc, char **argv) {
  // TODO USE cl::opt for options (dependencies issues)
  // llvm::cl::ParseCommandLineOptions(argc, argv);
  // LLVMCodegen codegen(OutputFilename);

  std::string OutputFilename = "test_semantic.ll"; // TODO: exterminate hardcode 
  std::string PathToSemaFuncDescs = "/home/extio1/mnt/edu/diploma/transema1/src/semantics"; // TODO: exterminate hardcode 
  
  LLVMCodegen Codegen(OutputFilename);
  YamlParser Parser(PathToSemaFuncDescs);
  
  auto SFs = Parser.parse();

  for(auto &SF: SFs) {
    Codegen.emit(SF);
  }

  // std::error_code err; TODO: linker issues
  // auto test_out_file_stream = llvm::raw_fd_stream(OutputFilename, err);
  // Codegen.printModule(test_out_file_stream);
  Codegen.printModule(); 

  return 0;
}