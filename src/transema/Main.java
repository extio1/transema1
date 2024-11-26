package transema;

import org.kframework.definition.Rule;

import java.util.Collection;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        if(args.length == 0){
         System.out.println("No path to compiled definition specified");
         return;
        }
        String filePath = args[0];

        SemaRulesLoader loader = new SemaRulesLoader();

        Map<String, Rule> ISEL2Rule = loader.load(filePath);

        ISEL2Rule.forEach((k, v) -> {
//            System.out.println(k);
//            KTermAnalyser.printKTerm(v.body(), 0);
            SemanticFunction semanticFunction = new SemanticFunction();
            semanticFunction.emitCpp(k, v);
        });
    }
}
