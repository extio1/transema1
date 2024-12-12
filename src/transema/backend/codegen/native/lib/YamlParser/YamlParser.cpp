#include "SemanticFunction/SemanticFunction.h"
#include "YamlParser/YamlParser.h"

#include <filesystem>

using namespace transema;

template <>
struct llvm::yaml::ScalarEnumerationTraits<OperandLocation> {
  static void enumeration(IO &io, OperandLocation &value) {
    io.enumCase(value, "mem",   OperandLocation::mem);
    io.enumCase(value, "gpr",   OperandLocation::gpr);
    io.enumCase(value, "flag",  OperandLocation::flag);
    io.enumCase(value, "imm",   OperandLocation::imm);
  }
};

template <> 
struct llvm::yaml::MappingTraits<Operand>{
  static void mapping(IO &io, Operand &Oper) {
    io.mapRequired("name",     Oper.name);
    io.mapRequired("width",    Oper.width);
    io.mapRequired("location", Oper.location);
  }
};

LLVM_YAML_IS_SEQUENCE_VECTOR(Operand);

template <> 
struct llvm::yaml::MappingTraits<Assignment>{
  static void mapping(IO &io, Assignment &A) {
    io.mapRequired("write_from",   A.rhs);
    io.mapRequired("to_register", A.lhs);
  }
};

LLVM_YAML_IS_SEQUENCE_VECTOR(Assignment);

template <> 
struct llvm::yaml::MappingTraits<SemanticFunction>{
  static void mapping(IO &io, SemanticFunction &SF) {
    io.mapRequired("isel",       SF.isel);
    io.mapRequired("write_arguments", SF.writeOpers);
    io.mapRequired("read_arguments",  SF.readOpers);
    io.mapRequired("state_delta",     SF.RSDelta);
  }
};

namespace {
  std::unique_ptr<llvm::MemoryBuffer> openYamlFile(llvm::StringRef Filename, std::string *ErrMsg) {
    auto File = llvm::MemoryBuffer::getFile(Filename);
    if(!File) {
      *ErrMsg = File.getError().message();
      return nullptr;
    }

    return std::move(*File);
  }
} // namespace

YamlParser::YamlParser(llvm::StringRef PathToDirWithYamls): PathToDirWithYamls(PathToDirWithYamls) {}

std::vector<SemanticFunction> YamlParser::parse() {
  std::string ErrMsg;
  auto ResultVector = std::vector<SemanticFunction>();

  for (const auto& entry : std::filesystem::directory_iterator(PathToDirWithYamls)) {
    auto YamlBuffer = openYamlFile(entry.path().c_str(), &ErrMsg);
    if(!YamlBuffer) {
      llvm::errs() << ErrMsg << "\n";
      return ResultVector;
    }

    SemanticFunction SF;
    llvm::yaml::Input yin(*YamlBuffer);
    yin >> SF;

    if(yin.error()) {
      llvm::errs() << "Error while parsing " << entry.path().c_str() << ": " << yin.error().message() << "\n";
      continue;
    }

    ResultVector.push_back(SF);
  }

  return ResultVector;
}

