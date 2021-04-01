package de.andreasgerhard.interpreter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.java.Log;

@Log
class Node {

  @Getter
  private final Object instance;

  @Getter
  private final List<Command> commands = new ArrayList<>();

  public Node(@NonNull Object instance, @NonNull JsonNode objectJsonNode) {
    this.instance = instance;

    final Method[] methods = instance.getClass().getMethods();

    objectJsonNode.fields().forEachRemaining(stringJsonNodeEntry -> {
      Method method = retrieveMethod(methods, stringJsonNodeEntry);
      commands.add(new Command(this, method, stringJsonNodeEntry.getValue()));
    });

  }

  private Method retrieveMethod(Method[] methods,
      java.util.Map.Entry<String, JsonNode> stringJsonNodeEntry) {
    Optional<Method> first = Arrays.stream(methods)
        .filter(method -> method.getName().equals(stringJsonNodeEntry.getKey()))
        .findFirst();
    if (first.isEmpty()) {
      errorMethodNotFound(stringJsonNodeEntry.getKey(), methods);
    }
    return first.get();
  }

  private void errorMethodNotFound(String asserted, Method[] expected) {
    log.severe(String
        .format("Sorry, could not find command \"%s\". Node \"%s\" has following commands: ",
            asserted, instance.getClass()
                .getSimpleName()));
    Arrays.stream(expected).forEach(method -> log.severe(method.getName()));
    throw new RuntimeException(
        String
            .format("Command \"%s#%s\" not found", instance.getClass().getSimpleName(), asserted));
  }

}
