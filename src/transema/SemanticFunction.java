package transema;

import org.kframework.definition.Rule;
import org.kframework.kore.K;
import org.kframework.kore.KApply;
import org.kframework.kore.KRewrite;
import org.kframework.kore.KSequence;
import org.kframework.kore.KToken;
import org.kframework.kore.KVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SemanticFunction {
    private enum Width {
        i8,
        i16,
        i32,
        i64
    }
    private final Map<String, Width> s2w = new HashMap<String, Width>(){{
            put("64", Width.i64);
            put("32", Width.i32);
            put("16", Width.i16);
            put("8",  Width.i8);
    }};

    private enum Location {
        registerGpr,
        registerFlag,
        memory,
        immediate
    }

    private abstract class AssignCommon {
        protected String name;
        protected Width width;
        protected Location location;
        protected Boolean isVariable = false;

        public Location getLocation() { return location; }
        public String getName() { return name; }
        public Width getWidth() { return width; }
        public Boolean isVariable() { return isVariable; }

        protected void handleRegister(KApply kApply) {
            String label = kApply.klabel().name();
            List<K> args =  kApply.items();

            if (Objects.equals(label, "#SemanticCastToR32")) {
                width = Width.i32;
            } else if (Objects.equals(label, "#SemanticCastToR64")) {
                width = Width.i64;
            } else {
                System.out.println("Unknown width");
                return;
            }

            handleName(args.get(0));
        }

        protected void handleName(K k) {
            if (k instanceof KVariable){
                name = ((KVariable)k).name();
                isVariable = true;
            } else {
                System.out.println("Unknown name");
            }
        }

        protected void handleImmediate(KApply varApply, K varWidth, K extendedWidth) {
            if (!Objects.equals(varApply.klabel().name(), "#SemanticCastToImm")) {
                System.out.println("Something goes wrong! protected void handleImmediate");
                return;
            } else {
                handleName(varApply.items().get(0));
            }

            if (varWidth instanceof KToken && extendedWidth instanceof KToken){
                KToken inputWidth = (KToken) varWidth;
                KToken outputWidth = (KToken) extendedWidth;

                if(!Objects.equals(inputWidth.s(), outputWidth.s())){
                    System.out.println("!Objects.equals(inputWidth.s(), outputWidth.s())");
                } else {
                    this.width = s2w.get(outputWidth.s());
                }
            } else {
                System.out.println("varWidth instanceof KToken && extendedWidth instanceof KToken error");
            }
        }
    }

    private class AssignLeft extends AssignCommon {
        public AssignLeft(KApply kApply) {
            handleLocation(kApply);
        }

        private void handleLocation(KApply kApply) {
            String label = kApply.klabel().name();
            List<K> args =  kApply.items();

            if (Objects.equals(label, "convToRegKeys")) {
                location = Location.registerGpr;
                handleRegister((KApply) args.get(0));
            } else {
                System.out.println("Unknown location!");
                return;
            }
        }
    }

    private class AssignRight extends AssignCommon {
        public AssignRight(KApply kApply) {
                handleLocation(kApply);
        }

        private void handleLocation(KApply kApply) {
            String label = kApply.klabel().name();
            List<K> args =  kApply.items();

            if (Objects.equals(label, "getParentValue")) {
                location = Location.registerGpr;
                handleRegister((KApply) args.get(0));
            } else if (Objects.equals(label, "handleImmediateWithSignExtend")){
                location = Location.immediate;
                handleImmediate((KApply) args.get(0), args.get(1), args.get(2));
            } else {
                System.out.println("Unknown location!");
                return;
            }
        }
    }

    private static class SemanticStateChange {
        private AssignLeft lhs;
        private AssignRight rhs;

        public SemanticStateChange(AssignLeft lhs, AssignRight rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }
    }

    private List<AssignLeft> writeArgs = new ArrayList<>();
    private List<AssignRight> readArgs = new ArrayList<>();
    private List<SemanticStateChange> changes = new ArrayList<>();

    public void emitCpp(String isel, Rule rule) {
        this.handleTerm(rule.body());
        changes.forEach((c) -> {
            System.out.println(c.lhs.name);
            System.out.println(c.lhs.location);
            System.out.println(c.lhs.width);
            System.out.println();
            System.out.println(c.rhs.name);
            System.out.println(c.rhs.location);
            System.out.println(c.rhs.width);
        });
        System.out.println();
        writeArgs.forEach((a) -> System.out.println(a.name));
        readArgs.forEach((a) -> System.out.println(a.name));
    }

    private void handleTerm(K term) {
        if (term instanceof KVariable){
            this.handleKVariable((KVariable) term);
        } else if (term instanceof KToken) {
            this.handleKToken((KToken) term);
        } else if (term instanceof KSequence) {
            this.handleKSequence((KSequence) term);
        } else if (term instanceof KApply) {
            this.handleKApply((KApply) term);
        } else if (term instanceof KRewrite) {
            this.handleKRewrite((KRewrite) term);
        }
    }

    private void handleKVariable(KVariable kVariable) {

    }

    private void handleKToken(KToken kToken) {

    }

    private void handleKSequence(KSequence kSequence) {

    }

    private void handleKApply(KApply kApply) {
        String label = kApply.klabel().name();
        List<K> args =  kApply.items();

        if (Objects.equals(label, "#cells")) {
            this.handleTerm(args.get(0));
            this.handleTerm(args.get(1));
        } else if (Objects.equals(label, "<regstate>")) {
            this.handleTerm(args.get(1));
        } else if (Objects.equals(label, "updateMap")) {
            this.handleTerm(args.get(1));
        } else if (Objects.equals(label, "_|->_")) {
            this.handleStateAssignment((KApply) args.get(0), (KApply) args.get(1));
        } else {
            System.out.println("No function for label: " + label);
        }
    }

    private void handleKRewrite(KRewrite kRewrite) {
        this.handleTerm(kRewrite.left());
        this.handleTerm(kRewrite.right());
    }

    private void handleStateAssignment(KApply lhs, KApply rhs) {
        AssignLeft assignTo = new AssignLeft(lhs);
        if(assignTo.isVariable()) {
            writeArgs.add(assignTo);
        }

        AssignRight assignFrom = new AssignRight(rhs);
        if(assignFrom.isVariable()){
            readArgs.add(assignFrom);
        }

        changes.add(new SemanticStateChange(assignTo, assignFrom));
    }
}
