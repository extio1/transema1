package transema;

import org.kframework.definition.Rule;
import org.kframework.main.KModule;
import transema.codegen.LLVMCodegen;
import transema.codegen.TransemaCodegen;

import java.util.Map;
import java.util.ServiceLoader;

public class Main {

    public static void main(String[] args) throws Exception {
        if(args.length == 0){
            System.out.println("No path to compiled definition specified");
            return;
        }
        String filePath = args[0];

        SemaRulesLoader loader = new SemaRulesLoader();
        TransemaCodegen codegen = new LLVMCodegen();
        Map<String, Rule> ISEL2Rule = loader.load(filePath);

        if (!ISEL2Rule.isEmpty()){
            codegen.createNewModule("llvm_sema_module.ll");
            ISEL2Rule.forEach((k, v) -> {
//            System.out.println(k);
//            KTermAnalyser.printKTerm(v.body());
//            try {
//                KTermAnalyser.printKTerm(v.body(), "../ast_"+k);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
                SemanticFunction semanticFunction = new SemanticFunction(k, v);
                codegen.emit(semanticFunction);
            });
            codegen.cleanNativeState();
        }

    }
}
