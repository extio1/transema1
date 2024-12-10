package transema.backend;

import com.google.inject.Inject;
import org.kframework.backend.java.symbolic.JavaBackend;
import org.kframework.definition.Rule;
import org.kframework.kompile.CompiledDefinition;
import org.kframework.kompile.KompileOptions;
import org.kframework.main.GlobalOptions;
import org.kframework.utils.errorsystem.KEMException;
import org.kframework.utils.errorsystem.KExceptionManager;
import org.kframework.utils.file.FileUtil;
import transema.backend.codegen.SemaRulesLoader;
import transema.backend.codegen.SemanticFunction;
import transema.backend.codegen.TransemaIREmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransemaBackend extends JavaBackend {
    private FileUtil files;
    GlobalOptions globalOptions;
    KompileOptions kompileOptions;

    @Inject
    public TransemaBackend(KExceptionManager kem, FileUtil files, GlobalOptions globalOptions, KompileOptions kompileOptions) {
        super(kem, files, globalOptions, kompileOptions);
        this.files = files;
        this.kompileOptions = kompileOptions;
    }

    @Override
    public void accept(CompiledDefinition def) {
        String pathToBackend = this.files.getEnv().get("TRANSEMA_BACKEND_PATH");
        Map<String, Rule> ISEL2Rule = SemaRulesLoader.getSemanticFunctionRules(def);
        TransemaIREmitter irEmitter = new TransemaIREmitter("/"); // TODO: somehow get path to directory with kompiled definition

        if (!ISEL2Rule.isEmpty()){
            ISEL2Rule.forEach((k, v) -> {
                SemanticFunction semanticFunction = new SemanticFunction(k, v);
                try { // TODO error handling
                    irEmitter.emit(semanticFunction);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // Create process with c++ codegen and pass path to directory with .yaml files of semantic functions
        ProcessBuilder pb = this.files.getProcessBuilder();
        List<String> args = new ArrayList();
        args.add(pathToBackend);
//        args.add() // TODO path to directory with .yaml files with semantic function definitions
        try {
            Process p = pb.command(args).directory(this.files.resolveKompiled(".")).inheritIO().start();
            int exit = p.waitFor();
            if (exit != 0) {
                throw KEMException.criticalError("transema-codegen returned nonzero exit code: " + exit + "\nExamine output to see errors.");
            }
        } catch (InterruptedException | IOException e) {
            throw KEMException.criticalError("Error with I/O while executing transema-codegen", e);
        }
    }
}
