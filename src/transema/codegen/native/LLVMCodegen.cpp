#include "transema_codegen_LLVMCodegen.h"
#include <iostream>

JNIEXPORT void JNICALL Java_transema_codegen_LLVMCodegen_emit
  (JNIEnv *, jobject, jobject)
{
    std::cout << "Hello from java native!\n";
}