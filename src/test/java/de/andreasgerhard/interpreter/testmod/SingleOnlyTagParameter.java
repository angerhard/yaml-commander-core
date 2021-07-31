package de.andreasgerhard.interpreter.testmod;

import de.andreasgerhard.interpreter.Tag;
import de.andreasgerhard.interpreter.YamlCommanderRoot;
import lombok.Getter;

@YamlCommanderRoot("single-only-tag")
public class SingleOnlyTagParameter {

  @Getter
  private String test;

  public SingleOnlyTagParameter init(@Tag("test") String test) {
    this.test = test;
    return this;
  }

  public SingleOnlyTagParameter init() {
    this.test = "onlyinitcalled"; return this;
  }


}
