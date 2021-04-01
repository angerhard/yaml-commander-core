package de.andreasgerhard.interpreter.testmod;

import de.andreasgerhard.interpreter.Tag;
import de.andreasgerhard.interpreter.YamlCommanderRoot;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@YamlCommanderRoot("multi-array")
public class MultipleArrayParameter {

  private int secretCalled = 0;
  private int updateCalled = 0;

  private List<String> keys = new ArrayList<>();
  private List<String> values = new ArrayList<>();
  private List<String> policies = new ArrayList<>();

  public MultipleArrayParameter secret() {
    secretCalled++;
    return this;
  }

  public MultipleArrayParameter update() {
    updateCalled++;
    return this;
  }

  public MultipleArrayParameter key(@Tag("key") String key) {
    keys.add(key);
    return this;
  }

  public MultipleArrayParameter value(@Tag("value") String value) {
    values.add(value);
    return this;
  }

  public MultipleArrayParameter policy(@Tag("policy") String policy) {
    policies.add(policy);
    return this;
  }
}
