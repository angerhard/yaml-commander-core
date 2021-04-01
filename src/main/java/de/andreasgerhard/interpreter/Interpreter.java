package de.andreasgerhard.interpreter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import lombok.SneakyThrows;

public class Interpreter {

    private Chain chain;

    @SneakyThrows
    public Interpreter(String importFile) {
        Iterator<Entry<String, JsonNode>> fields = parseFile(
            importFile);
        chain = new Chain().readRoot(fields);
    }

    private Iterator<Entry<String, JsonNode>> parseFile(String importFile)
        throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        JsonNode jsonNode = mapper.readTree(new File(importFile));
        Iterator<Entry<String, JsonNode>> fields = jsonNode.fields();
        return fields;
    }

    public Interpreter(Map<String, Class> rootClassMapping, String importFile) throws IOException {
        Iterator<Entry<String, JsonNode>> fields = parseFile(
            importFile);
        chain = new Chain().addRootClasses(rootClassMapping).readRoot(fields);
    }

    public Object getMainInstance() {
        return chain.getActiveObject();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("you have to set the yaml import file as a parameter");
        }
        new Interpreter(args[0]);
    }

}
