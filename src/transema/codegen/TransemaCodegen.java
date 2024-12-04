package transema.codegen;

import transema.SemanticFunction;

public abstract class TransemaCodegen {
    public native void emit(SemanticFunction semanticFunction);
}
