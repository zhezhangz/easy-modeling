package xyz.v2my.easymodeling.factory.field;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import xyz.v2my.easymodeling.factory.FieldWrapper;
import xyz.v2my.easymodeling.randomizer.OptionalRandomizer;

import java.util.List;
import java.util.Optional;

public class OptionalField extends Container {

    public OptionalField(TypeName rawType, FieldWrapper field, List<ModelField> valueFields) {
        super(ParameterizedTypeName.get(ClassName.get(Optional.class), rawType), field, valueFields);
    }

    @Override
    protected CodeBlock initializerType() {
        return CodeBlock.of("$T", OptionalRandomizer.class);
    }

    @Override
    protected CodeBlock initializerParameter() {
        return CodeBlock.of("$L", field.allowEmpty());
    }
}
