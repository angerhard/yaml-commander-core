package de.andreasgerhard.interpreter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Test;

class YamlTest {

  @Test
  void shouldReadSubArray() throws Exception {
    String yaml = "test:\n"
        + "  - {key: \"secret1\", policy: \"default1\"}\n"
        + " "
        + " - {key: \"secret2\", policy: \"default2\"}";
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    JsonNode jsonNode = mapper.readTree(yaml);
    JsonNode testNode = jsonNode.get("test");

    testNode.iterator().forEachRemaining(
        arrayNode -> {
          arrayNode.fieldNames().forEachRemaining(s -> {
            System.out.print(s);
            System.out.print("#");
            System.out.println(arrayNode.get(s));
          });
        }
    );


  }
}
