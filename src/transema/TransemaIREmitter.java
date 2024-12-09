package transema;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TransemaIREmitter {
    private final String pathToDir;

    public TransemaIREmitter(String pathToDir){
        this.pathToDir = pathToDir;
    }

    // Emits .yaml file for semantic fucntion
    public void emit(SemanticFunction semf) throws IOException {
        Yaml yaml = new Yaml();
        File file = new File(semf.getIsel());
        FileWriter fileWriter = new FileWriter(file);

        Map<String, Object> yamlStruct = new HashMap<>();

        yamlStruct.put("read_arguments",
                semf.getReadArgs().stream().map(SemanticFunction.AssignRight::getProperties).toArray());

        yamlStruct.put("write_arguments",
                semf.getWriteArgs().stream().map(SemanticFunction.AssignLeft::getProperties).toArray());

        yamlStruct.put("state_delta",
                semf.getSemanticStateChange().stream().map(SemanticFunction.SemanticStateChange::getDelta).toArray());

        yaml.dump(yamlStruct, fileWriter);
    }
}
