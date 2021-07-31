package de.andreasgerhard.interpreter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
        .filter(method -> {
          if (stringJsonNodeEntry.getValue().isNull()) {
            return method.getParameterCount() == 0;
          } else if (stringJsonNodeEntry.getValue().isTextual()) {
            return method.getParameterCount() == 1;
          } else if (stringJsonNodeEntry.getValue().isObject()) {

            ObjectNode objectNode = ((ObjectNode) stringJsonNodeEntry.getValue());
            // case 1: json will be processed by returning type, so no parameters on method.
            if (method.getParameterCount() == 0) {
              return true;
            }

            Iterable<String> iterable = () -> objectNode.fieldNames();
            List<String> givenParametersFromJson = StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
            List<String> givenParameterdFromMethod = Arrays.stream(method.getParameters()).map(
                p -> p.getAnnotation(Tag.class) == null ? null : p.getAnnotation(Tag.class).value())
                .filter(
                    Objects::nonNull).collect(Collectors.toList());

            givenParametersFromJson.removeAll(givenParameterdFromMethod);
            return givenParametersFromJson.isEmpty();
          } else if (stringJsonNodeEntry.getValue().isArray()) {
            return true;
          }
          return false;
        }).findFirst();

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
