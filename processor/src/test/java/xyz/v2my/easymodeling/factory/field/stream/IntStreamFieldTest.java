package xyz.v2my.easymodeling.factory.field.stream;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import org.junit.jupiter.api.BeforeEach;
import xyz.v2my.easymodeling.factory.field.FieldTest;
import xyz.v2my.easymodeling.factory.field.number.IntegerField;
import xyz.v2my.easymodeling.factory.helper.FieldWrapperFactory;
import xyz.v2my.easymodeling.randomizer.stream.IntStreamRandomizer;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class IntStreamFieldTest extends FieldTest {

    @Override
    @BeforeEach
    protected void setUp() {
        fieldWrapper = FieldWrapperFactory.one(FIELD_NAME).min(2.).max(9.).minSize(10).maxSize(15).build();
        typeName = ClassName.get(IntStream.class);
        modelField = new IntStreamField().create(fieldWrapper);
    }

    @Override
    protected void should_generate_initializer() {
        final CodeBlock initializer = modelField.initializer();

        assertThat(initializer.toString()).isEqualTo(
                "new " + $(IntStreamRandomizer.class) + "(" + new IntegerField().create(fieldWrapper).initializer() + ", 10, 15)");
    }
}
