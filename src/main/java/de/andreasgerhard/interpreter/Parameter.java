package de.andreasgerhard.interpreter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.extern.java.Log;

@Log
class Parameter {

  private Class type;
  @Getter
  private final String name;
  private String value;
  private String[] array;
  private boolean asArray;

  public Parameter(Class type, String name) {
    this.type = type;
    this.name = name;
    if (type.isArray()) {
      this.type = type.getComponentType();
      this.asArray = true;
    }
  }

  public void updateValue(String val) {
    this.value = val;
  }

  public void updateValue(String[] val) {
    this.asArray = true;
    this.array = val;
  }

  public Object getValue() {
    if (asArray) {
      Object o = Array.newInstance(type, this.array.length);
      IntStream.range(0, this.array.length).forEach(i -> {
        Array.set(o, i, retrieveValue(this.array[i]));
      });
      return o;
    }
    return retrieveValue(this.value);
  }

  private Object retrieveValue(String value) {
    if (type == Integer.class || type == int.class) {
      return Integer.parseInt(value);
    }
    if (type == String.class) {
      return value;
    }
    if (Enum.class.isAssignableFrom(type)) {

      Enum anEnum = null;
      try {
        anEnum = Enum.valueOf(type, value);
      } catch (Exception e) {
        errorParameterNotFound(value, type.getEnumConstants());
      }
      return anEnum;
    }
    return null;
  }

  private void errorParameterNotFound(String asserted, Object[] expected) {
    log.severe(String
        .format("Sorry, could not find enum \"%s\". Enum \"%s\" has parameter: ",
            asserted, type.getName()));
    Arrays.stream(expected)
        .forEach(parameter -> log.severe(String.valueOf(parameter)));
    throw new RuntimeException(
        String
            .format("Enum val \"%s#%s\" not found", type.getName(), asserted));
  }

  @Override
  public String toString() {
    return "Parameter{" +
        "type=" + type +
        ", name='" + name + '\'' +
        ", value='" + value + '\'' +
        '}';
  }
}
