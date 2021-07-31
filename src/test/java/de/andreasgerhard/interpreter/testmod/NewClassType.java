package de.andreasgerhard.interpreter.testmod;

import de.andreasgerhard.interpreter.YamlCommanderRoot;
import lombok.Getter;

@YamlCommanderRoot("new-class")
public class NewClassType {

  @Getter
  private Application application = new Application();

  public Application application() {
    return application;
  }

}
