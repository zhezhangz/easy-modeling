package xyz.v2my.easymodeling;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.stream.Collectors;

public class FactoryType {

    private static final String FACTORY_NAME_PATTERN = "EM%sFactory";

    private final TypeElement clazz;

    private final List<BuilderField> builderFields;

    private final BuilderType builderType;

    public FactoryType(TypeElement clazz) {
        final List<BuilderField> builderFields = initBuilderFields(clazz);
        this.clazz = clazz;
        this.builderFields = builderFields;
        this.builderType = new BuilderType(clazz, builderFields);
    }

    private List<BuilderField> initBuilderFields(TypeElement clazz) {
        return clazz.getEnclosedElements().stream()
                .filter(element -> element.getKind().equals(ElementKind.FIELD))
                .filter(element -> !element.getModifiers().contains(Modifier.STATIC))
                .map(BuilderField::new)
                .collect(Collectors.toList());
    }

    public TypeSpec createType() {
        final String factoryTypeName = String.format(FACTORY_NAME_PATTERN, clazz.getSimpleName());
        final TypeSpec.Builder factory = TypeSpec.classBuilder(factoryTypeName).addModifiers(Modifier.PUBLIC);
        final TypeSpec innerBuilder = builderType.createType();
        factory.addType(innerBuilder);

        final MethodSpec builder = builderMethod(innerBuilder.name);
        factory.addMethod(builder);

        return factory.build();
    }

    private MethodSpec builderMethod(String builderName) {
        String builderParameters = builderFields.stream()
                .map(BuilderField::filedInitializer)
                .collect(Collectors.joining(","));
        return MethodSpec.methodBuilder("builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.get("", builderName))
                .addStatement("return new $N(" + builderParameters + ")", builderName)
                .build();
    }
}
