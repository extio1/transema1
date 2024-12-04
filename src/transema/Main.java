package transema;

import org.kframework.definition.Rule;
import transema.codegen.LLVMCodegen;
import transema.codegen.TransemaCodegen;

import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        if(args.length == 0){
            System.out.println("No path to compiled definition specified");
            return;
        }
        String filePath = args[0];

        SemaRulesLoader loader = new SemaRulesLoader();
        TransemaCodegen emitter = new LLVMCodegen();
        Map<String, Rule> ISEL2Rule = loader.load(filePath);

        ISEL2Rule.forEach((k, v) -> {
//            System.out.println(k);
//            KTermAnalyser.printKTerm(v.body());
//            try {
//                KTermAnalyser.printKTerm(v.body(), "../ast_"+k);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
            SemanticFunction semanticFunction = new SemanticFunction(k, v);
            emitter.emit(semanticFunction);
        });
    }
}
