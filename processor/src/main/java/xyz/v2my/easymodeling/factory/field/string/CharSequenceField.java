package xyz.v2my.easymodeling.factory.field.string;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import xyz.v2my.easymodeling.factory.FieldWrapper;
import xyz.v2my.easymodeling.factory.field.ModelField;

import java.util.Optional;

import static xyz.v2my.easymodeling.randomizer.string.CharSequenceRandomizer.ALPHABETIC;
import static xyz.v2my.easymodeling.randomizer.string.CharSequenceRandomizer.ALPHANUMERIC;
import static xyz.v2my.easymodeling.randomizer.string.CharSequenceRandomizer.NUMERIC;
import static xyz.v2my.easymodeling.randomizer.string.CharSequenceRandomizer.RANDOM;

public abstract class CharSequenceField<FIELD extends CharSequence> extends ModelField<FIELD> {

    public CharSequenceField() {
    }

    public CharSequenceField(TypeName type, FieldWrapper field) {
        super(type, field);
    }

    @Override
    public CodeBlock randomParameter() {
        final int charset = alphabetic() & numeric() & alphaNumeric();
        if (charset == 0) {
            // TODO: 28.12.21 switch to more specific exception
            throw new IllegalArgumentException("No charset specified");
        }
        return CodeBlock.of("$L, $L, $L", min(), max(), charset);
    }

    @Override
    protected Optional<CodeBlock> constantParameter() {
        return string().map(s -> CodeBlock.of("$S", s));
    }

    private long min() {
        return field.min()
                .map(Double::longValue)
                .filter(min -> min >= 1)
                .orElse(6L);
    }

    private long max() {
        // FIXME: 28.12.21 should limit the reasonable string length in validation
        return field.max()
                .map(Double::longValue)
                .filter(min -> min < Integer.MAX_VALUE)
                .orElse(20L);
    }

    private int alphabetic() {
        return field.alphabetic() ? ALPHABETIC : RANDOM;
    }

    private int numeric() {
        return field.numeric() ? NUMERIC : RANDOM;
    }

    private int alphaNumeric() {
        return field.alphanumeric() ? ALPHANUMERIC : RANDOM;
    }

    private Optional<String> string() {
        return field.string();
    }
}
