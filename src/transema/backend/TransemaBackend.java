package transema.backend;

import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.kframework.backend.java.symbolic.JavaBackend;
import org.kframework.backend.llvm.matching.Matching;
import org.kframework.kompile.CompiledDefinition;
import org.kframework.kompile.KompileOptions;
import org.kframework.main.GlobalOptions;
import org.kframework.utils.errorsystem.KEMException;
import org.kframework.utils.errorsystem.KExceptionManager;
import org.kframework.utils.file.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransemaBackend extends JavaBackend {
    private FileUtil files;

    @Inject
    public TransemaBackend(KExceptionManager kem, FileUtil files, GlobalOptions globalOptions, KompileOptions kompileOptions) {
        super(kem, files, globalOptions, kompileOptions);
        this.files = files;
    }

    @Override
    public void accept(CompiledDefinition def) {
        String pathToBackend = this.files.getEnv().get("TRANSEMA_BACKEND_PATH");
//        FileUtils.deleteQuietly(this.files.resolveKompiled("dt"));
//        MutableInt warnings = new MutableInt();
//        Matching.writeDecisionTreeToFile(this.files.resolveKompiled("definition.kore"), this.options.heuristic, this.files.resolveKompiled("dt"), Matching.getThreshold(this.getThreshold()), this.options.warnUseless, (ex) -> {
//            this.kem.addKException(ex);
//            warnings.increment();
//            return null;
//        });
//        if (warnings.intValue() > 0 && this.kem.options.warnings2errors) {
//            throw KEMException.compilerError("Had " + warnings.intValue() + " pattern matching errors.");
//        } else if (!this.options.noLLVMKompile) {
//            ProcessBuilder pb = this.files.getProcessBuilder();
//            List<String> args = new ArrayList();
//            args.add("llvm-kompile");
//            args.add("definition.kore");
//            args.add("dt");
//            args.add("main");
//            args.add("-o");
//            args.add("interpreter");
//            if (this.kompileOptions.optimize1) {
//                args.add("-O1");
//            }
//
//            if (this.kompileOptions.optimize2) {
//                args.add("-O2");
//            }
//
//            if (this.kompileOptions.optimize3) {
//                args.add("-O2");
//            }
//
//            args.addAll(this.options.ccopts);
//
//            try {
//                Process p = pb.command(args).directory(this.files.resolveKompiled(".")).inheritIO().start();
//                int exit = p.waitFor();
//                if (exit != 0) {
//                    throw KEMException.criticalError("llvm-kompile returned nonzero exit code: " + exit + "\nExamine output to see errors.");
//                }
//            } catch (InterruptedException | IOException e) {
//                throw KEMException.criticalError("Error with I/O while executing llvm-kompile", e);
//            }
//        }
    }
}
