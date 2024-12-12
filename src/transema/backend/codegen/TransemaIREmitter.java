package transema.backend.codegen;

import org.yaml.snakeyaml.Yaml;
import transema.backend.astAnalyser.SemanticFunction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TransemaIREmitter {
    private final String pathToDir;

    public TransemaIREmitter(String pathToDir){
        this.pathToDir = pathToDir;
    }

    // Emits .yaml file for semantic fucntion
    public void emit(SemanticFunction semf) throws IOException {
        Yaml yaml = new Yaml();
        File file = new File(pathToDir+"/"+semf.getIsel());
        if(file.getParentFile().mkdirs()){
            System.out.println("Create new file: " + file.getAbsolutePath());
        }

        FileWriter fileWriter = new FileWriter(file);

        Map<String, Object> yamlStruct = new HashMap<>();

        yamlStruct.put("isel", semf.getIsel());

        yamlStruct.put("read_arguments",
                semf.getReadArgs().stream().map((a) -> a.getProperties().get("name")).toArray());

        yamlStruct.put("write_arguments",
                semf.getWriteArgs().stream().map((a) -> a.getProperties().get("name")).toArray());

        yamlStruct.put("state_delta",
                semf.getSemanticStateChange().stream().map(SemanticFunction.SemanticStateChange::getDelta).toArray());

        yaml.dump(yamlStruct, fileWriter);
    }
}
