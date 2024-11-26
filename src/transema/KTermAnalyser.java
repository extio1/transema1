package transema;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static void printKTerm(K term, int tabCount) {
        for (int i = 0; i < tabCount; i++){
            System.out.print("  ");
        }
        System.out.print(term.getClass()+"--");

        if (term instanceof ADT.KAs) {
            KAs kAs = (ADT.KAs) term;
            System.out.println(kAs);
        } else if (term instanceof ADT.KLabel) {
            KLabel kLabel = (ADT.KLabel) term;
            System.out.println(kLabel.name());
        } else if (term instanceof ADT.KList) {
            KList kList = (ADT.KList) term;
            System.out.println(kList.size());
        } else if (term instanceof ADT.KSequence) {
            KSequence kSequence = (ADT.KSequence) term;
            System.out.println(kSequence);
        } else if (term instanceof ADT.KToken) {
            KToken kToken = (ADT.KToken) term;
            System.out.println(kToken.s());
        } else if (term instanceof ADT.KVariable) {
            KVariable kVariable = (ADT.KVariable) term;
            System.out.print(kVariable.name()+"==");
            System.out.println(kVariable);
        } else if (term instanceof ADT.KApply) {
            KApply kApply = (ADT.KApply) term;
            List<K> kParams = kApply.items();
            System.out.print("Label: " + kApply.klabel() + "==");
            System.out.println("N Arguments: " + kParams.size());
            kParams.forEach((t) -> printKTerm(t, tabCount+1));
        } else if (term instanceof ADT.KRewrite) {
            KRewrite kRewrite = (ADT.KRewrite) term;
            System.out.println(kRewrite);
            printKTerm(kRewrite.left(), tabCount+1);
            printKTerm(kRewrite.right(), tabCount+1);
        }
    }
}
