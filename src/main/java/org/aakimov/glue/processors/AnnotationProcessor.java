package org.aakimov.glue.processors;

import org.aakimov.glue.annotations.ImmutableImplementation;
import org.aakimov.glue.generators.ImmutableClassGenerator;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Main annotation processor.
 *
 * Handles annotations at compile time.
 * TODO make this class composite to support different annotations.
 *
 * @author aakimov
 */
@SupportedAnnotationTypes(value = {"org.aakimov.glue.annotations.ImmutableImplementation"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AnnotationProcessor extends AbstractProcessor {

    /**
     * Immutable class generator
     */
    private final ImmutableClassGenerator immutableClassGenerator;

    /**
     * Immutable class name prefix (target class name will be composed from this prefix and parent interface name)
     */
    private final String immutableClassNamePrefix;

    /**
     * Public constructor is required for tools.
     */
    public AnnotationProcessor() {
        this.immutableClassGenerator = new ImmutableClassGenerator();
        this.immutableClassNamePrefix = "Generic";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (annotations == null || annotations.isEmpty()) {
            return false;
        }

        for (Element element : roundEnvironment.getElementsAnnotatedWith(ImmutableImplementation.class)) {
            if (element.getKind() == ElementKind.INTERFACE) {
                TypeElement interfaceElement = (TypeElement)element;
                Map<String, String> properties = new LinkedHashMap<>();
                // populate property list based on getter methods of the interface
                for (Element enclosedElement : interfaceElement.getEnclosedElements()) {
                    // skip everything but method elements
                    if (enclosedElement.getKind() == ElementKind.METHOD) {
                        ExecutableElement methodElement = (ExecutableElement)enclosedElement;
                        String methodName = methodElement.getSimpleName().toString();

                        if (!methodName.startsWith("get")
                            || !methodElement.getParameters().isEmpty()
                            || !methodElement.getThrownTypes().isEmpty()
                            || !methodElement.getTypeParameters().isEmpty()
                        ) {
                            this.processingEnv.getMessager().printMessage(
                                Diagnostic.Kind.ERROR,
                                "Interface annotated with @ImmutableImplementation should contain only getters that are not generic and do not throw any exceptions."
                            );
                            return false;
                        }

                        String rawPropertyName = methodName.substring(3);
                        properties.put(
                            rawPropertyName.substring(0, 1).toLowerCase() + rawPropertyName.substring(1),
                            methodElement.getReturnType().toString()
                        );
                    }
                }
                // generate source file for the implementation
                try {
                    PackageElement packageElement = (PackageElement)interfaceElement.getEnclosingElement();
                    String parentInterfaceName = interfaceElement.getSimpleName().toString();
                    this.immutableClassGenerator.generateSource(
                        this.processingEnv.getFiler(),
                        packageElement.getQualifiedName().toString(),
                        this.immutableClassNamePrefix + parentInterfaceName,
                        parentInterfaceName,
                        properties
                    );
                } catch (IOException exception) {
                    this.processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "Unable to generate source file."
                    );
                    return false;
                }
            } else {
                this.processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    "Only interfaces can be annotated with @ImmutableImplementation"
                );
                return false;
            }
        }

        return true;
    }
}
