package transema;

import org.kframework.definition.Rule;
import transema.backend.codegen.SemaRulesLoader;
import transema.backend.codegen.SemanticFunction;
import transema.backend.codegen.TransemaIREmitter;

import java.io.IOException;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        if(args.length == 0){
            System.out.println("No path to compiled definition specified");
            return;
        }
        String filePath = args[0];

        SemaRulesLoader loader = new SemaRulesLoader();
        TransemaIREmitter irEmitter = new TransemaIREmitter("semantics");
        Map<String, Rule> ISEL2Rule = loader.load(filePath);

        if (!ISEL2Rule.isEmpty()){
            ISEL2Rule.forEach((k, v) -> {
                SemanticFunction semanticFunction = new SemanticFunction(k, v);
                try {
                    irEmitter.emit(semanticFunction);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }
}
