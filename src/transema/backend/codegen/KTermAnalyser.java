package transema.backend.codegen;

import org.kframework.kore.ADT;
import org.kframework.kore.K;
import org.kframework.kore.KApply;
import org.kframework.kore.KAs;
import org.kframework.kore.KLabel;
import org.kframework.kore.KList;
import org.kframework.kore.KRewrite;
import org.kframework.kore.KSequence;
import org.kframework.kore.KToken;
import org.kframework.kore.KVariable;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class KTermAnalyser {
    static Map<Class<? extends K>, List<String>> instances = new HashMap<>();

    public void countKTerm(K term) {
        String nameOfTerm = "";

        if (term instanceof ADT.KLabel) {
            KLabel kLabel = (ADT.KLabel) term;
            nameOfTerm = kLabel.name();
        } else if (term instanceof ADT.KToken) {
            KToken kToken = (ADT.KToken) term;
            nameOfTerm = kToken.s();
        } else if (term instanceof ADT.KVariable) {
            KVariable kVariable = (ADT.KVariable) term;
            nameOfTerm = kVariable.name();
        } else if (term instanceof ADT.KAs) {
            KAs kAs = (ADT.KAs) term;
            nameOfTerm = kAs.pattern().toString();
        } else if (term instanceof ADT.KList) {
            KList kList = (ADT.KList) term;
            nameOfTerm = "KList";
            kList.stream().forEach(this::countKTerm);
        } else if (term instanceof ADT.KSequence) {
            KSequence kSequence = (ADT.KSequence) term;
            nameOfTerm = "KSequence";
            kSequence.stream().forEach(this::countKTerm);
        } else if (term instanceof ADT.KApply) {
            KApply kApply = (ADT.KApply) term;
            nameOfTerm = kApply.klabel().name();
            kApply.items().forEach(this::countKTerm);
        } else if (term instanceof ADT.KRewrite) {
            KRewrite kRewrite = (ADT.KRewrite) term;
//            nameOfTerm = kRewrite.left().toString() + " -----> " + kRewrite.right().toString();
            nameOfTerm = "KRewrite";
            countKTerm(kRewrite.left());
            countKTerm(kRewrite.right());
        }

        if(instances.containsKey(term.getClass())){
            instances.get(term.getClass()).add(nameOfTerm);
        } else {
            List<String> l = new ArrayList<>();
            l.add(nameOfTerm);
            instances.put(term.getClass(), l);
        }
    }

    public static void printKTerm(K term){
        dump(term, 0, System.out::print);
    }

    public static void printKTerm(K term, String filename) throws IOException {
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));

        dump(term, 0, (s) -> {
            try {
                writer.write(s);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        writer.close();
    }

    public static void dump(K term, int tabCount, Consumer<String> output) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tabCount; i++) {
            sb.append("  ");
        }

        String fullClassName = term.getClass().toString();
        String simpleName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
        if (simpleName.contains("$")) {
            simpleName = simpleName.substring(simpleName.lastIndexOf('$') + 1);
        }
        sb.append(simpleName).append(" ");

        if (term instanceof ADT.KAs) {
            KAs kAs = (ADT.KAs) term;
            sb.append(kAs.toString()).append("\n");;
            output.accept(sb.toString());
        } else if (term instanceof ADT.KLabel) {
            KLabel kLabel = (ADT.KLabel) term;
            sb.append(kLabel.name()).append("\n");;
            output.accept(sb.toString());
        } else if (term instanceof ADT.KList) {
            KList kList = (ADT.KList) term;
            sb.append(kList.size()).append("\n");;
            output.accept(sb.toString());
        } else if (term instanceof ADT.KSequence) {
            KSequence kSequence = (ADT.KSequence) term;
            sb.append(kSequence.toString()).append("\n");;
            output.accept(sb.toString());
        } else if (term instanceof ADT.KToken) {
            KToken kToken = (ADT.KToken) term;
            sb.append(kToken.s()).append("\n");;
            output.accept(sb.toString());
        } else if (term instanceof ADT.KVariable) {
            KVariable kVariable = (ADT.KVariable) term;
            sb.append(kVariable.name()).append("\n");;
            output.accept(sb.toString());
        } else if (term instanceof ADT.KApply) {
            KApply kApply = (ADT.KApply) term;
            List<K> kParams = kApply.items();
            sb.append(kApply.klabel()).append("\n");
            output.accept(sb.toString());

            kParams.forEach((t) -> dump(t, tabCount + 1, output));
        } else if (term instanceof ADT.KRewrite) {
            KRewrite kRewrite = (ADT.KRewrite) term;
            sb.append(kRewrite.toString()).append("\n");;
            output.accept(sb.toString());

            dump(kRewrite.left(), tabCount + 1, output);
            dump(kRewrite.right(), tabCount + 1, output);
        }
    }

}
