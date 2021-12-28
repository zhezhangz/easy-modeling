package xyz.v2my.easymodeling.factory.field.numeric;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import xyz.v2my.easymodeling.Field;
import xyz.v2my.easymodeling.randomizer.Randomizer;
import xyz.v2my.easymodeling.randomizer.numberrandomizer.IntegerRandomizer;

public class IntegerField extends NumericField {

    public IntegerField(TypeName type, String name, Field field) {
        super(type, name, field);
    }

    @Override
    protected long ceiling() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected long floor() {
        return Integer.MIN_VALUE;
    }

    @Override
    protected CodeBlock constantInit(Double c) {
        return CodeBlock.of("$L", c.intValue());
    }

    @Override
    protected Class<? extends Randomizer> randomizer() {
        return IntegerRandomizer.class;
    }
}
