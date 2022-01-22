package xyz.v2my.easymodeling.factory.field;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.v2my.easymodeling.factory.FieldWrapper;

import javax.lang.model.element.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class FieldTest {

    public static final String FIELD_NAME = "fieldName";

    protected FieldWrapper fieldWrapper;

    protected TypeName typeName;

    protected ModelField modelField;

    protected String $(Class<?> clazz) {
        return clazz.getCanonicalName();
    }

    @BeforeEach
    protected abstract void setUp();

    @Test
    final void should_generate_builder_field() {
        final FieldSpec field = modelField.field();

        assertThat(field.modifiers).containsExactly(Modifier.PRIVATE);
        assertThat(field.type).isEqualTo(typeName);
        assertThat(field.name).isEqualTo(FIELD_NAME);
    }

    @Test
    final void should_generate_builder_setter() {
        final MethodSpec setter = modelField.setter();

        assertThat(setter.name).isEqualTo(FIELD_NAME);
        assertThat(setter.returnType).hasToString("Builder");
        assertThat(setter.parameters).hasSize(1);
        assertThat(setter.parameters.get(0).name).isEqualTo(FIELD_NAME);
        assertThat(setter.parameters.get(0).type).isEqualTo(typeName);
        final String code = "" +
                "this." + FIELD_NAME + " = " + FIELD_NAME + ";\n" +
                "return this;\n";
        assertThat(setter.code).hasToString(code);
    }

    @Test
    void should_generate_initial_value() {
        final CodeBlock initializer = modelField.initializer();
        final CodeBlock initialValue = modelField.initialValue();

        assertThat(initialValue).isEqualTo(CodeBlock.of("$L.next()", initializer));
    }

    @Test
    void should_generate_initializer_() {
        should_generate_initializer();
    }

    protected abstract void should_generate_initializer();
}
