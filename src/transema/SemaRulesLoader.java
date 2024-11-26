package transema;

import org.kframework.attributes.Source;
import org.kframework.definition.Module;
import org.kframework.definition.Rule;
import org.kframework.kompile.CompiledDefinition;
import org.kframework.main.GlobalOptions;
import org.kframework.utils.BinaryLoader;
import org.kframework.utils.errorsystem.KExceptionManager;
import scala.Option;
import scala.collection.Iterator;
import scala.collection.Set;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SemaRulesLoader {
    static final String SEMA_MODULE_NAME = "X86-INSTRUCTIONS-SEMANTICS";
    static final String SEMA_SOURCE_PATH = "x86-instructions-semantics.k";

    private String translateISELToXEDFormat(String iselK) {
        // TODO logic to transform something like MOVL-R32-R32 to XED_FORM_MOVL_GPR32_GPR32
        return iselK;
    }

    public Map<String, Rule> load(String filePath) throws IOException, ClassNotFoundException, InterruptedException {
        BinaryLoader bl = new BinaryLoader(new KExceptionManager(new GlobalOptions()));
        CompiledDefinition def = (CompiledDefinition) bl.loadSynchronized(new File(filePath));
        Map<String, Rule> iselToRule = new HashMap<>();

        Option<Module> instructionSemaModuleOpt = def
                .getParsedDefinition()
                .getModule(SEMA_MODULE_NAME);

        if(instructionSemaModuleOpt.isDefined()){
            Module instructionSemaModule = instructionSemaModuleOpt.get();
            Set<Rule> instructionSemaRules = instructionSemaModule.rules();

            Iterator<Rule> it = instructionSemaRules.iterator();
            while(it.hasNext()) {
                Rule r = it.next();
                if (r.isNonSyntax()) {
                    Optional<Source> s = r.source();
                    if (s.isPresent() && s.get().source().endsWith(SEMA_SOURCE_PATH)) {
                        Option<String> klabel = r.att().getOption("klabel");
                        if(klabel.isDefined())
                            iselToRule.put(translateISELToXEDFormat(klabel.get()), r);
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Module <" + SEMA_MODULE_NAME + "> is not defined");
        }

        return iselToRule;
    }
}
