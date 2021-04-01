package de.andreasgerhard.interpreter.testmod;

import de.andreasgerhard.interpreter.Tag;
import de.andreasgerhard.interpreter.YamlCommanderRoot;
import lombok.Getter;

@YamlCommanderRoot("single")
public class SingleParameter {

  @Getter
  private String test;

  public SingleParameter init(@Tag("test") String test) {
    this.test = test;
    return this;
  }

}
