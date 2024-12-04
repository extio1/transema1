#!/bin/bash
SOURCES=$(dirname $0)/transema

SOURCES="transema/KTermAnalyser.java
        transema/Main.java
        transema/SemaRulesLoader.java
        transema/SemanticFunction.java
        transema/codegen/TransemaCodegen.java
        transema/codegen/LLVMCodegen.java"

# javac -h "transema/codegen/native" -cp "$(dirname $0)/../kframework/usr/lib/kframework/lib/java/*":. $SOURCES
javac -cp "$(dirname $0)/../kframework/usr/lib/kframework/lib/java/*":. $SOURCES
