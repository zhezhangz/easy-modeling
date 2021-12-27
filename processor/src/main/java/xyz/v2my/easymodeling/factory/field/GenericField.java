package xyz.v2my.easymodeling.factory.field;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

public class GenericField extends AbstractField {

    public GenericField(TypeName type, String name) {
        super(type, name);
    }

    @Override
    public CodeBlock initializer() {
        return CodeBlock.of("null");
    }
}
