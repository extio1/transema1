package transema.backend;

import com.google.inject.Inject;
import org.kframework.backend.java.symbolic.JavaBackend;
import org.kframework.kompile.CompiledDefinition;
import org.kframework.kompile.KompileOptions;
import org.kframework.main.GlobalOptions;
import org.kframework.utils.errorsystem.KExceptionManager;
import org.kframework.utils.file.FileUtil;

public class TransemaBackend extends JavaBackend {
    @Inject
    public TransemaBackend(KExceptionManager kem, FileUtil files, GlobalOptions globalOptions, KompileOptions kompileOptions) {
        super(kem, files, globalOptions, kompileOptions);
    }

    @Override
    public void accept(CompiledDefinition compiledDefinition) {
        System.out.println("TRANSEMA ACCEPTS COMPILED DEFINITION");
        System.exit(42);
    }
}
