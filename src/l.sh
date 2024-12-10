#!/bin/bash

ARGS="$(dirname $0)/../X86-64-semantics/semantics/x86-semantics-kompiled/compiled.bin"
#java -cp "$(dirname $0)/../kframework/usr/lib/kframework/lib/java/*":"transema/*":"out/transema/*". transema.Main $ARGS
java -cp "$(dirname $0)/../kframework/usr/lib/kframework/lib/java/*":"transema-with-main":. transema.Main $ARGS