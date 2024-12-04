#!/bin/bash

ARGS="/repo/X86-64-semantics/semantics/x86-semantics-kompiled/compiled.bin"
java -cp "/kframework/usr/lib/kframework/lib/java/*":. transema.Main $ARGS
