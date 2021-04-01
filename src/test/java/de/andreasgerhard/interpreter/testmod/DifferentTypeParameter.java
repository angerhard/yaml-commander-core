package de.andreasgerhard.interpreter.testmod;

import de.andreasgerhard.interpreter.YamlCommanderRoot;


public class DifferentTypeParameter {

  private String string;
  private int integer;
  private Permission enumeration;

  public DifferentTypeParameter string(String val) {
    this.string = val;
    return this;
  }

  public DifferentTypeParameter integer(int val) {
    this.integer = val;
    return this;
  }

  public DifferentTypeParameter enumeration(Permission permission) {
    this.enumeration = permission;
    return this;
  }

}
