package de.andreasgerhard.interpreter.testmod;

import de.andreasgerhard.interpreter.Tag;
import de.andreasgerhard.interpreter.YamlCommanderRoot;
import lombok.Getter;

@YamlCommanderRoot("multi-enum")
public class MultipleEnumArrayParameter {


  @Getter
  private String path;
  @Getter
  private Permission[] permissions;

  public MultipleEnumArrayParameter init(@Tag("path") String path,
      @Tag("acl") Permission... permissions) {
    this.path = path;
    this.permissions = permissions;
    return this;
  }

}
