package transema.backend;

import org.kframework.compile.Backend;
import org.kframework.definition.Definition;
import org.kframework.definition.Module;
import org.kframework.definition.ModuleTransformer;
import org.kframework.kompile.CompiledDefinition;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class TransemaBackend implements Backend {
    @Override
    public void accept(CompiledDefinition compiledDefinition) {
        System.out.println("TRANSEMA ACCEPTS COMPILED DEFINITION");
    }

    @Override
    public Function<Definition, Definition> steps() {
        return null;
    }

    @Override
    public Function<Module, Module> specificationSteps(Definition definition) {
        return null;
    }

    @Override
    public Set<String> excludedModuleTags() {
        return new HashSet<>();
    }

    @Override
    public ModuleTransformer restoreDefinitionModulesTransformer(Definition kompiledDefinition) {
        return Backend.super.restoreDefinitionModulesTransformer(kompiledDefinition);
    }
}
