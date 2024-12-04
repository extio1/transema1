package transema.codegen;

import transema.SemanticFunction;

public class LLVMCodegen extends TransemaCodegen {
    static {
        System.loadLibrary("LLVMCodegen");
    }

    private long nativeState = 0;

    @Override
    public native void emit(SemanticFunction semanticFunction);

    @Override
    public native void createNewModule(String name);

    @Override
    public native void cleanNativeState();
}
