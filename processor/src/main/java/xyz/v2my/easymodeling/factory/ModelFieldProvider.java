package xyz.v2my.easymodeling.factory;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import xyz.v2my.easymodeling.factory.field.Container;
import xyz.v2my.easymodeling.factory.field.ModelField;
import xyz.v2my.easymodeling.factory.field.OptionalField;
import xyz.v2my.easymodeling.factory.field.PlainField;
import xyz.v2my.easymodeling.factory.field.UnknownField;
import xyz.v2my.easymodeling.factory.field.array.ArrayField;
import xyz.v2my.easymodeling.factory.field.array.PrimitiveArrayField;
import xyz.v2my.easymodeling.factory.field.collection.ArrayListField;
import xyz.v2my.easymodeling.factory.field.collection.HashSetField;
import xyz.v2my.easymodeling.factory.field.collection.LinkedListField;
import xyz.v2my.easymodeling.factory.field.collection.ListField;
import xyz.v2my.easymodeling.factory.field.collection.MapField;
import xyz.v2my.easymodeling.factory.field.collection.SetField;
import xyz.v2my.easymodeling.factory.field.collection.TreeSetField;
import xyz.v2my.easymodeling.factory.field.datetime.InstantField;
import xyz.v2my.easymodeling.factory.field.number.ByteField;
import xyz.v2my.easymodeling.factory.field.number.DoubleField;
import xyz.v2my.easymodeling.factory.field.number.FloatField;
import xyz.v2my.easymodeling.factory.field.number.IntegerField;
import xyz.v2my.easymodeling.factory.field.number.LongField;
import xyz.v2my.easymodeling.factory.field.number.ShortField;
import xyz.v2my.easymodeling.factory.field.primitive.BooleanField;
import xyz.v2my.easymodeling.factory.field.primitive.CharField;
import xyz.v2my.easymodeling.factory.field.string.StringBuilderField;
import xyz.v2my.easymodeling.factory.field.string.StringField;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class ModelFieldProvider {

    public ModelField provide(TypeName type, FieldWrapper field) {
        try {
            return findField(type, field);
        } catch (FieldNotSupportedException e) {
            return new UnknownField(type, field);
        }
    }

    private ModelField findField(TypeName type, FieldWrapper field) {
        if (type instanceof ArrayTypeName) {
            return arrayField((ArrayTypeName) type, field);
        }
        if (type instanceof ParameterizedTypeName) {
            return containerField((ParameterizedTypeName) type, field);
        }
        return plainField(type, field);
    }

    private ModelField nestedField(TypeName type, FieldWrapper field) {
        final ModelField nestedField = findField(type, field);
        if (nestedField instanceof PrimitiveArrayField) {
            throw new FieldNotSupportedException();
        }
        return nestedField;
    }

    private Container arrayField(ArrayTypeName type, FieldWrapper field) {
        final TypeName rawType = rawType(type);
        if (rawType.isPrimitive()) {
            return new PrimitiveArrayField(type, field, plainField(rawType, field));
        }
        return new ArrayField(type, field, nestedField(type.componentType, field));
    }

    private Container containerField(ParameterizedTypeName parameterizedTypeName, FieldWrapper field) {
        final ClassName rawType = parameterizedTypeName.rawType;
        final ModelField[] modelFields = parameterizedTypeName.typeArguments.stream()
                .map(type -> nestedField(type, field))
                .toArray(ModelField[]::new);
        try {
            if (ClassName.get(Optional.class).equals(rawType)) {
                return new OptionalField(field, modelFields[0]);
            }
            if (ClassName.get(List.class).equals(rawType)) {
                return new ListField(field, modelFields[0]);
            }
            if (ClassName.get(ArrayList.class).equals(rawType)) {
                return new ArrayListField(field, modelFields[0]);
            }
            if (ClassName.get(LinkedList.class).equals(rawType)) {
                return new LinkedListField(field, modelFields[0]);
            }
            if (ClassName.get(Set.class).equals(rawType)) {
                return new SetField(field, modelFields[0]);
            }
            if (ClassName.get(HashSet.class).equals(rawType)) {
                return new HashSetField(field, modelFields[0]);
            }
            if (ClassName.get(TreeSet.class).equals(rawType)) {
                return new TreeSetField(field, modelFields[0]);
            }
            if (ClassName.get(Map.class).equals(rawType)) {
                return new MapField(field, modelFields[0], modelFields[1]);
            }
        } catch (ArrayIndexOutOfBoundsException obe) {
            throw new FieldNotSupportedException();
        }
        throw new FieldNotSupportedException();
    }

    private PlainField<?> plainField(TypeName type, FieldWrapper field) {
        if (TypeName.BYTE.equals(type) || ClassName.get(Byte.class).equals(type)) {
            return new ByteField(field);
        }
        if (TypeName.SHORT.equals(type) || ClassName.get(Short.class).equals(type)) {
            return new ShortField(field);
        }
        if (TypeName.INT.equals(type) || ClassName.get(Integer.class).equals(type)) {
            return new IntegerField(field);
        }
        if (TypeName.LONG.equals(type) || ClassName.get(Long.class).equals(type)) {
            return new LongField(field);
        }
        if (TypeName.FLOAT.equals(type) || ClassName.get(Float.class).equals(type)) {
            return new FloatField(field);
        }
        if (TypeName.DOUBLE.equals(type) || ClassName.get(Double.class).equals(type)) {
            return new DoubleField(field);
        }
        if (TypeName.BOOLEAN.equals(type) || ClassName.get(Boolean.class).equals(type)) {
            return new BooleanField(field);
        }
        if (TypeName.CHAR.equals(type) || ClassName.get(Character.class).equals(type)) {
            return new CharField(field);
        }
        if (type.equals(ClassName.get(String.class))) {
            return new StringField(field);
        }
        if (type.equals(ClassName.get(StringBuilder.class))) {
            return new StringBuilderField(field);
        }
        if (type.equals(ClassName.get(Instant.class))) {
            return new InstantField(field);
        }
        throw new FieldNotSupportedException();
    }

    private TypeName rawType(TypeName type) {
        if (type instanceof ArrayTypeName) {
            return rawType(((ArrayTypeName) type).componentType);
        } else {
            return type;
        }
    }
}
