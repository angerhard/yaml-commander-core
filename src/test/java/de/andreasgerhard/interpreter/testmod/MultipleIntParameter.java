package de.andreasgerhard.interpreter.testmod;

import de.andreasgerhard.interpreter.Tag;
import de.andreasgerhard.interpreter.YamlCommanderRoot;
import lombok.Getter;

@YamlCommanderRoot("multi-int")
public class MultipleIntParameter {


  @Getter
  private int firstVal;
  @Getter
  private int secondVal;

  public MultipleIntParameter init(@Tag("shares") int firstVal,
      @Tag("threshold") int secondVal) {
    this.firstVal = firstVal;
    this.secondVal = secondVal;
    return this;
  }

}
