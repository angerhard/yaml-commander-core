package de.andreasgerhard.interpreter.testmod;

import de.andreasgerhard.interpreter.Tag;
import de.andreasgerhard.interpreter.YamlCommanderRoot;
import lombok.Getter;

@YamlCommanderRoot("multi-string")
public class MultipleStringParameter {


  @Getter
  private String firstVal;
  @Getter
  private String secondVal;

  public MultipleStringParameter init(@Tag("shares") String firstVal,
      @Tag("threshold") String secondVal) {
    this.firstVal = firstVal;
    this.secondVal = secondVal;
    return this;
  }

}
