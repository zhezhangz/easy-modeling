package xyz.v2my.easymodeling.factory.field.collection;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import xyz.v2my.easymodeling.factory.FieldWrapper;
import xyz.v2my.easymodeling.factory.field.Container;
import xyz.v2my.easymodeling.factory.field.PlainField;
import xyz.v2my.easymodeling.factory.field.array.ArrayField;
import xyz.v2my.easymodeling.factory.field.datetime.InstantField;
import xyz.v2my.easymodeling.factory.field.numeric.IntegerField;
import xyz.v2my.easymodeling.randomizer.array.ArrayRandomizer;
import xyz.v2my.easymodeling.randomizer.datetime.InstantRandomizer;
import xyz.v2my.easymodeling.randomizer.number.IntegerRandomizer;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ArrayFieldTest {

    private FieldWrapper field;

    @BeforeEach
    void setUp() throws IllegalAccessException {
        this.field = FieldWrapper.of("field");

        FieldUtils.writeField(field, "minLength", 2, true);
        FieldUtils.writeField(field, "maxLength", 5, true);
        FieldUtils.writeField(field, "min", 1., true);
        FieldUtils.writeField(field, "max", 3., true);
    }

    @Nested
    class ArrayOfArrayTest {

        @Test
        void should_generate_statement_of_array() {
            final PlainField<Integer> integerField = new IntegerField().create(ClassName.get(Integer.class), field);
            final Container arrayField = new ArrayField(ClassName.get(Integer.class), field, integerField);

            final CodeBlock initialValue = arrayField.initialValue();

            final String arrayRandomizer = "new " + $(ArrayRandomizer.class) + "<>(new " + $(IntegerRandomizer.class) + "(1.0, 3.0), 2, 5)";
            assertThat(initialValue.toString()).isEqualTo(arrayRandomizer + ".next()");
        }

        @Test
        void should_generate_statement_of_matrix() {
            final PlainField<Integer> integerField = new IntegerField().create(ClassName.get(Integer.class), field);
            final Container arrayField = new ArrayField(ArrayTypeName.get(Integer[].class), field, integerField);
            final Container matrixField = new ArrayField(ArrayTypeName.get(Integer[][].class), field, arrayField);

            final CodeBlock initialValue = matrixField.initialValue();

            final String arrayRandomizer = "new " + $(ArrayRandomizer.class) + "<>(new " + $(IntegerRandomizer.class) + "(1.0, 3.0), 2, 5)";
            final String matrixRandomizer = "new " + $(ArrayRandomizer.class) + "<>(" + arrayRandomizer + ", 2, 5)";
            assertThat(initialValue.toString()).isEqualTo(matrixRandomizer + ".next()");
        }

        @Test
        void should_generate_statement_of_cube() {
            final PlainField<Instant> instantField = new InstantField().create(ClassName.get(Instant.class), field);
            final Container arrayField = new ArrayField(ArrayTypeName.get(Instant[].class), field, instantField);
            final Container matrixField = new ArrayField(ArrayTypeName.get(Instant[][].class), field, arrayField);
            final Container cubeField = new ArrayField(ArrayTypeName.get(Instant[][][].class), field, matrixField);

            final CodeBlock initialValue = cubeField.initialValue();

            final String arrayRandomizer = "new " + $(ArrayRandomizer.class) + "<>(new " + $(InstantRandomizer.class) + "(0L, 2147483647000L), 2, 5)";
            final String matrixRandomizer = "new " + $(ArrayRandomizer.class) + "<>(" + arrayRandomizer + ", 2, 5)";
            final String cubeRandomizer = "new " + $(ArrayRandomizer.class) + "<>(" + matrixRandomizer + ", 2, 5)";
            assertThat(initialValue.toString()).isEqualTo(cubeRandomizer + ".next()");
        }
    }

    private String $(Class<?> clazz) {
        return clazz.getCanonicalName();
    }
}
