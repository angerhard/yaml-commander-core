package de.andreasgerhard.interpreter.testmod;

import de.andreasgerhard.interpreter.Tag;
import de.andreasgerhard.interpreter.YamlCommanderRoot;

import java.util.ArrayList;
import java.util.List;

@YamlCommanderRoot("vault")
public class MultipleExecutionParameter {

  private List<String> values = new ArrayList<>();

  public MultipleExecutionParameter test(@Tag("val") String val) {
    values.add(val);
    return this;
  }

}
