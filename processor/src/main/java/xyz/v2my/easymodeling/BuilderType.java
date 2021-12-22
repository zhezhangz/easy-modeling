package xyz.v2my.easymodeling;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.stream.Collectors;

public class BuilderType {

    private static final String BUILDER_NAME_PATTERN = "EM%sBuilder";

    private final TypeElement clazz;

    private final List<BuilderField> builderFields;

    public BuilderType(TypeElement clazz, List<BuilderField> builderFields) {
        this.clazz = clazz;
        this.builderFields = builderFields;
    }

    public TypeSpec createType() {
        final String builderTypeName = String.format(BUILDER_NAME_PATTERN, clazz.getSimpleName());
        final TypeSpec.Builder builder = TypeSpec.classBuilder(builderTypeName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        builder.addMethod(builderAllArgsConstructor())
                .addMethod(buildMethod())
                .addFields(builderFields())
                .addMethods(builderSetters());
        return builder.build();
    }

    private MethodSpec builderAllArgsConstructor() {
        final List<ParameterSpec> constructorParameters = builderFields.stream()
                .map(BuilderField::constructorParameter)
                .collect(Collectors.toList());
        final List<CodeBlock> constructorStatements = builderFields.stream()
                .map(BuilderField::constructorStatement)
                .collect(Collectors.toList());

        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameters(constructorParameters)
                .addStatement(CodeBlock.join(constructorStatements, ";\n")).build();
    }

    private MethodSpec buildMethod() {
        final String constructionParameters = builderFields.stream()
                .map(BuilderField::constructorVariable)
                .collect(Collectors.joining(", "));
        return MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(clazz))
                .addStatement("return new $N(" + constructionParameters + ")", clazz.getSimpleName())
                .build();
    }

    private List<FieldSpec> builderFields() {
        return builderFields.stream().map(BuilderField::builderField).collect(Collectors.toList());
    }

    private List<MethodSpec> builderSetters() {
        final String builderTypeName = String.format("EM%sBuilder", clazz.getSimpleName());
        return builderFields.stream().map(field -> field.builderSetter(builderTypeName)).collect(Collectors.toList());
    }
}
