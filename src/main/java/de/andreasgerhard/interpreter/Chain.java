package de.andreasgerhard.interpreter;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Log
public class Chain {

    private static final Map<String, Class> ROOTS = new HashMap<>();

    static {

        Reflections reflections = new Reflections("");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(YamlCommanderRoot.class);
        for (Class<?> controller : annotated) {

            ROOTS.put(controller
                    .getAnnotation(YamlCommanderRoot.class)
                    .value(), controller);
        }

    }

    @Getter
    private Object activeObject;

    public Chain addRootClasses(Map<String, Class> roots) {
        ROOTS.putAll(roots);
        return this;
    }

    public Chain readRoot(Iterator<Entry<String, JsonNode>> fields) {
        fields.forEachRemaining(stringJsonNodeEntry -> {
            activeObject = null;
            readObject(stringJsonNodeEntry.getKey(),
                    stringJsonNodeEntry.getValue());
        });
        return this;
    }

    @SneakyThrows
    private void readObject(String name, JsonNode field) {
        buildRootClass(name);
        new Node(activeObject, field);
    }

    private void buildRootClass(String name)
            throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        if (activeObject == null) {
            activeObject = ROOTS
                    .get(name)
                    .getDeclaredConstructor()
                    .newInstance();
        }
    }

}
