package de.andreasgerhard.interpreter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

@Log
class Command {

  private final Node instance;
  private final JsonNode commandJsonNode;
  private List<Parameter> parameter = new ArrayList<>();
  private Method method;

  public Command(Node instance, Method method, JsonNode commandJsonNode) {
    this.instance = instance;
    this.method = method;
    this.commandJsonNode = commandJsonNode;

    createParametersByMethod();

    switch (method.getParameterCount()) {
      case 0:
        readNoneParameter();
        break;
      case 1:
        readSingleParameter();
        break;
      default:
        readMultipleParameter();
        break;
    }

    if (method.getParameterCount() == 0 && commandJsonNode instanceof ArrayNode) {
      commandJsonNode.iterator().forEachRemaining(jsonNode -> {
        Object execute = execute();
        new Node(execute, jsonNode);
      });
      return;
    }

    Object execute = execute();

    if (instance.getInstance() == execute) {
      return;
    }

    new Node(execute, commandJsonNode);
  }

  private void createParametersByMethod() {
    Arrays.stream(this.method.getParameters()).forEach(param -> {
      if (param.isAnnotationPresent(Tag.class)) {
        parameter.add(new Parameter(param.getType(), param.getAnnotation(Tag.class).value()));
      }
    });
  }

  private void readSingleParameter() {
    Arrays.stream(method.getParameters()).forEach(param -> {
      if (param.getAnnotation(Tag.class) == null) {
        errorTagNotFound(method.getDeclaringClass().getName() + "#" + method.getName());
      }
      String key = param.getAnnotation(Tag.class).value();
      extractValueFromJsonObject(key, commandJsonNode);
    });
  }

  private void readNoneParameter() {
  }

  private void readMultipleParameter() {
    commandJsonNode.fieldNames().forEachRemaining(fieldName -> {
      JsonNode valueNode = commandJsonNode.get(fieldName);
      extractValueFromJsonObject(fieldName, valueNode);
    });
  }

  private void extractValueFromJsonObject(String fieldName, JsonNode valueNode) {
    if (valueNode instanceof ArrayNode) {
      List<String> values = new ArrayList<>();
      valueNode.iterator().forEachRemaining(jsonNode -> {
        values.add(jsonNode.asText());
      });
      addValueToParameter(fieldName, values.toArray(new String[0]));
    } else {
      addValueToParameter(fieldName, valueNode.asText());
    }
  }


  @SneakyThrows
  private Object execute() {
    try {
      return method.invoke(instance.getInstance(), getParameterList());
    } catch (Exception e) {
      log.severe(
          "Sorry, could not call method [" + method.getName() + "] on [" + instance.getInstance()
              .getClass().getSimpleName() + "] for parameter list [" + parameter + "]");
      throw e;
    }
  }

  private Object[] getParameterList() {
    List<Object> result = new ArrayList<>();
    Arrays.stream(method.getParameters()).forEach(param -> {
      Parameter parameter = getParameter(param.getAnnotation(Tag.class).value());
      result.add(parameter.getValue());
    });
    return result.toArray();
  }

  private void addValueToParameter(String parameterName, String value) {
    Parameter first = getParameter(parameterName);
    first.updateValue(value);
  }

  private void addValueToParameter(String parameterName, String[] value) {
    Parameter first = getParameter(parameterName);
    first.updateValue(value);
  }

  private Parameter getParameter(String parameterName) {
    Optional<Parameter> first = parameter
        .stream()
        .filter(param -> param.getName().equals(parameterName))
        .findFirst();
    if (first.isEmpty()) {
      errorParameterNotFound(parameterName, method.getParameters());
    }
    return first.get();
  }

  private void errorParameterNotFound(String asserted, java.lang.reflect.Parameter[] expected) {
    log.severe(String
        .format("Sorry, could not find parameter \"%s\". Command \"%s\" has parameter: ",
            asserted, method.getName()));
    Arrays.stream(expected)
        .forEach(parameter -> log.severe(parameter.getAnnotation(Tag.class).value()));
    throw new RuntimeException(
        String
            .format("Parameter \"%s#%s\" not found", method.getName(), asserted));
  }

  private void errorTagNotFound(String method) {
    log.severe(String
        .format("Sorry, missing @Tag at method \"%s\"", method));
    throw new RuntimeException(
        String
            .format("@Tag at method %s not found.", method));
  }

}
