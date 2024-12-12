#pragma once

#include "SemanticFunction/SemanticFunction.h"

#include <llvm/Support/YAMLTraits.h>

#include <vector>

namespace transema{
  class YamlParser {
    public:
      explicit YamlParser(llvm::StringRef PathToDirWithYamls);
      std::vector<SemanticFunction> parse();
    private:
      std::string PathToDirWithYamls;      
  };
} // namespace transema