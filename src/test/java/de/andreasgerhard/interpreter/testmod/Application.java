package de.andreasgerhard.interpreter.testmod;

import de.andreasgerhard.interpreter.Tag;
import lombok.Getter;

public class Application {

  @Getter private String name;

  public Application name(@Tag("name") String name) {
    this.name = name;
    return this;
  }
}
