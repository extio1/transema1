package transema.backend.codegen;

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
    private List<AssignLeft> writeArgs = new ArrayList<>();
    private List<AssignRight> readArgs = new ArrayList<>();
    private List<SemanticStateChange> changes = new ArrayList<>();
    private String isel; // TODO: by the way isel may be calculated from arguments

    public SemanticFunction(String isel, Rule rule) {
        this.isel = isel;
        this.handleTerm(rule.body());
    }

    public List<AssignLeft> getWriteArgs() { return writeArgs; }
    public List<AssignRight> getReadArgs() { return readArgs; }
    public List<SemanticStateChange> getSemanticStateChange() { return changes; }
    public String getIsel() { return isel; } // TODO: by the way isel may be calculated from arguments

    private final Map<String, Integer> s2w = new HashMap<String, Integer>(){{
            put("64", 64);
            put("32", 32);
            put("16", 16);
            put("8",  8);
    }};

    private abstract class Operand {
        // данные для сериализации в yaml. Для правильной расстановки якорей необходимо
        // получать ссылку на один и тот же объект, что соотвествует этому операнду
        // поэтому данные для сериализации лежат сразу удобно в мапе
        private class OperandOuterInfo {
            private final Map<String, Object> data = new HashMap<>();
            public void setName(String name) { data.put("name", name); }
            public void setWidth(Integer width) { data.put("width", width); }
            public void setLocation(String location) { data.put("location", location); }
            public Map<String, Object> getState() { return data; }
        }

        protected Boolean isVariable = false;
        protected OperandOuterInfo data = new OperandOuterInfo();

        public Map<String, Object> getProperties(){
            return data.getState();
        }

        protected void handleRegister(KApply kApply) {
            String label = kApply.klabel().name();
            List<K> args =  kApply.items();

            if (Objects.equals(label, "#SemanticCastToR32")) {
                data.setWidth(32);
            } else if (Objects.equals(label, "#SemanticCastToR64")) {
                data.setWidth(64);
            } else {
                System.out.println("Unknown width");
                return;
            }

            handleName(args.get(0));
        }

        protected void handleName(K k) {
            if (k instanceof KVariable){
                data.setName(((KVariable)k).name());
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
                    data.setWidth(s2w.get(outputWidth.s()));
                }
            } else {
                System.out.println("varWidth instanceof KToken && extendedWidth instanceof KToken error");
            }
        }
    }

    public class AssignLeft extends Operand {
        public AssignLeft(KApply kApply) {
            handleLocation(kApply);
        }

        private void handleLocation(KApply kApply) {
            String label = kApply.klabel().name();
            List<K> args =  kApply.items();

            if (Objects.equals(label, "convToRegKeys")) {
                data.setLocation("gpr");
                handleRegister((KApply) args.get(0));
            } else {
                System.out.println("Unknown location!");
                return;
            }
        }
    }

    public class AssignRight extends Operand {
        public AssignRight(KApply kApply) {
                handleLocation(kApply);
        }

        private void handleLocation(KApply kApply) {
            String label = kApply.klabel().name();
            List<K> args =  kApply.items();

            if (Objects.equals(label, "getParentValue")) {
                data.setLocation("gpr");
                handleRegister((KApply) args.get(0));
            } else if (Objects.equals(label, "handleImmediateWithSignExtend")){
                data.setLocation("imm");
                handleImmediate((KApply) args.get(0), args.get(1), args.get(2));
            } else {
                System.out.println("Unknown location!");
                return;
            }
        }
    }

    public static class SemanticStateChange {
        private AssignLeft lhs;
        private AssignRight rhs;

        public SemanticStateChange(AssignLeft lhs, AssignRight rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }

        public Map<String, Object> getDelta() {
            Map<String, Object> map = new HashMap<>();
            map.put("from", lhs.getProperties());
            map.put("to", rhs.getProperties());
            return map;
        }
    }

//    private void printDelta(){
//        changes.forEach((c) -> {
//            System.out.println(c.lhs.name);
//            System.out.println(c.lhs.location);
//            System.out.println(c.lhs.width);
//            System.out.println();
//            System.out.println(c.rhs.name);
//            System.out.println(c.rhs.location);
//            System.out.println(c.rhs.width);
//        });
//        System.out.println();
//        writeArgs.forEach((a) -> System.out.println(a.name));
//        readArgs.forEach((a) -> System.out.println(a.name));
//    }

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
        if(assignTo.isVariable) {
            writeArgs.add(assignTo);
        }

        AssignRight assignFrom = new AssignRight(rhs);
        if(assignFrom.isVariable){
            readArgs.add(assignFrom);
        }

        changes.add(new SemanticStateChange(assignTo, assignFrom));
    }
}
