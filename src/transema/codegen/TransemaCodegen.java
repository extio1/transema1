package transema.codegen;

import transema.SemanticFunction;

public abstract class TransemaCodegen {
    // Creates new module containing generated semantic function
    public native void createNewModule(String name);

    // Emits SemanticFunction to module (created by createNewModule() before)
    public native void emit(SemanticFunction semanticFunction);

    // Should be called after all emit() operations.
    // Cleans native object state create in createNewModule()
    public native void cleanNativeState();

    // TODO finalize deprecated. Почему?
    @Override
    protected void finalize() throws Throwable {
        cleanNativeState();
        super.finalize();
    }
}
