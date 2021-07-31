package de.andreasgerhard.interpreter;

import de.andreasgerhard.interpreter.testmod.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class InterpreterTest {

    private static String getResourcePath() {
        File targetClassesDir = new File(
                InterpreterTest.class
                        .getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .getPath());
        File targetDir = targetClassesDir
                .getParentFile()
                .getParentFile();
        return new File(targetDir.getAbsolutePath(), "src/test/resources").getAbsolutePath();
    }

    @Test
    void shouldReadFile() {
        File testFile = new File(getResourcePath(), "single-parameter.yaml");
        Assertions
                .assertThat(testFile)
                .exists();
        new Interpreter(testFile.getAbsolutePath());
    }

    @Test
    void shouldReadSingleValue() throws Exception {
        Interpreter interpreter = new Interpreter(new File(getResourcePath(), "single-parameter.yaml").getAbsolutePath());
        Assertions
                .assertThat(((SingleParameter) interpreter.getMainInstance())
                        .getTest())
                .isEqualTo("assertedValue");
    }

    @Test
    void shouldReadSingleOnlyTagValue() throws Exception {
        Interpreter interpreter = new Interpreter(new File(getResourcePath(), "single-only-tag-parameter.yaml").getAbsolutePath());
        Assertions
            .assertThat(((SingleOnlyTagParameter) interpreter.getMainInstance())
                .getTest())
            .isEqualTo("assertedValue");
    }

    @Test
    void shouldReadSingleWithoutParameterValue() throws Exception {
        Interpreter interpreter = new Interpreter(new File(getResourcePath(), "single-only-init-wo-parameter.yaml").getAbsolutePath());
        Assertions
            .assertThat(((SingleOnlyTagParameter) interpreter.getMainInstance())
                .getTest())
            .isEqualTo("onlyinitcalled");
    }

    @Test
    void shouldReadEnumArrayValue() throws Exception {
        Interpreter interpreter = new Interpreter(
                new File(getResourcePath(), "multiple-enumarrays-parameter.yaml").getAbsolutePath());
        MultipleEnumArrayParameter mainInstance = (MultipleEnumArrayParameter) interpreter
                .getMainInstance();
        Assertions
                .assertThat(mainInstance
                        .getPath())
                .isEqualTo("test1");
        Assertions
                .assertThat(mainInstance.getPermissions())
                .containsExactly(Permission.LIST, Permission.READ);
    }

    @Test
    void shouldReadMultipleIntValues() throws Exception {
        Interpreter interpreter = new Interpreter(
                new File(getResourcePath(), "multiple-int-parameter.yaml").getAbsolutePath());
        MultipleIntParameter mainInstance = (MultipleIntParameter) interpreter.getMainInstance();
        Assertions
                .assertThat(mainInstance
                        .getFirstVal())
                .isEqualTo(2);
        Assertions
                .assertThat(mainInstance
                        .getSecondVal())
                .isEqualTo(5);
    }

    @Test
    void shouldReadMultipleStringValues() throws Exception {
        Interpreter interpreter = new Interpreter(
                new File(getResourcePath(), "multiple-string-parameter.yaml").getAbsolutePath());
        MultipleStringParameter mainInstance = (MultipleStringParameter) interpreter.getMainInstance();
        Assertions
                .assertThat(mainInstance
                        .getFirstVal())
                .isEqualTo("test1");
        Assertions
                .assertThat(mainInstance
                        .getSecondVal())
                .isEqualTo("test2");
    }

    @Test
    void shouldReadMultipleArray() throws Exception {
        Interpreter interpreter = new Interpreter(
                new File(getResourcePath(), "multiple-array-parameter.yaml").getAbsolutePath());
        MultipleArrayParameter mainInstance = (MultipleArrayParameter) interpreter.getMainInstance();

        Assertions
                .assertThat(mainInstance.getKeys())
                .containsExactly("secret1", "secret2", "secret3");
        Assertions
                .assertThat(mainInstance.getValues())
                .containsExactly("123");
        Assertions
                .assertThat(mainInstance.getPolicies())
                .containsExactly("default1", "default2");

        Assertions
                .assertThat(mainInstance.getSecretCalled())
                .isEqualTo(3);
        Assertions
                .assertThat(mainInstance.getUpdateCalled())
                .isEqualTo(3);
    }

    @Test
    void shouldFindNewClass() throws Exception {
        Interpreter interpreter = new Interpreter(new File(getResourcePath(), "new-class.yaml").getAbsolutePath());
        Assertions
            .assertThat(((NewClassType) interpreter.getMainInstance())
                .getApplication().getName())
            .isEqualTo("test-application");
    }
}

