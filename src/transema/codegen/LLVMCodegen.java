package transema.codegen;

import transema.SemanticFunction;

public class LLVMCodegen extends TransemaCodegen {
    static {
        System.loadLibrary("LLVMCodegen");
    }

    @Override
    public native void emit(SemanticFunction semanticFunction);
}
