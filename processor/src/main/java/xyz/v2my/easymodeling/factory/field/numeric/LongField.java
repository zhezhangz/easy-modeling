package xyz.v2my.easymodeling.factory.field.numeric;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import xyz.v2my.easymodeling.factory.FieldWrapper;
import xyz.v2my.easymodeling.factory.field.ModelField;
import xyz.v2my.easymodeling.randomizer.Randomizer;
import xyz.v2my.easymodeling.randomizer.number.LongRandomizer;

public class LongField extends NumericField<Long> {

    protected LongField(TypeName type, FieldWrapper field) {
        super(type, field);
    }

    public LongField() {
    }

    @Override
    protected double ceiling() {
        return Long.MAX_VALUE;
    }

    @Override
    protected double floor() {
        return Long.MIN_VALUE;
    }

    @Override
    protected CodeBlock constantInit(Double c) {
        return CodeBlock.of("$LL", c.longValue());
    }

    @Override
    protected Class<? extends Randomizer<Long>> randomizer() {
        return LongRandomizer.class;
    }

    @Override
    public ModelField<Long> create(TypeName type, FieldWrapper field) {
        return new LongField(type, field);
    }
}
