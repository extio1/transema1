package transema.backend;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.multibindings.MapBinder;
import org.apache.commons.lang3.tuple.Pair;
import org.kframework.backend.llvm.LLVMBackend;
import org.kframework.backend.llvm.LLVMBackendKModule;
import org.kframework.backend.llvm.LLVMKompileOptions;
import org.kframework.compile.Backend;
import org.kframework.main.AbstractKModule;

import java.util.Collections;
import java.util.List;

import java.io.File;
import java.io.IOException;

public class TransemaKModule extends AbstractKModule {
    public TransemaKModule() {
    }

    public List<Module> getKompileModules() {

        List<Module> mods = super.getKompileModules();
        mods.add(new AbstractModule() {
            protected void configure() {
                MapBinder<String, Backend> mapBinder = MapBinder.newMapBinder(this.binder(), String.class, Backend.class);
                mapBinder.addBinding("transema").to(TransemaBackend.class);
            }
        });

        return mods;
    }

    public List<Pair<Class<?>, Boolean>> kompileOptions() {
        return Collections.singletonList(Pair.of(LLVMKompileOptions.class, true));
    }
}
