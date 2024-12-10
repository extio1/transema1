#pragma once

#include "SemanticFunction/SemanticFunction.h"

#include <vector>

namespace transema{
  class YamlParser{
    public:
      std::vector<SemanticFunction> parse(const std::string &pathToDir);
  };
} // namespace transema