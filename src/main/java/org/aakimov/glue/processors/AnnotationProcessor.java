package org.aakimov.glue.processors;

import org.aakimov.glue.annotations.ImmutableImplementation;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Main annotation processor.
 *
 * Handles annotations at compile time.
 *
 * @author aakimov
 */
@SupportedAnnotationTypes(value = {"org.aakimov.glue.annotations.ImmutableImplementation"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AnnotationProcessor extends AbstractProcessor {

    /**
     * Public constructor is required for tools.
     */
    public AnnotationProcessor() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(ImmutableImplementation.class)) {
            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement)element;
                for (Element enclosedElement : typeElement.getEnclosedElements()) {
                    // TODO add some logic here :)
                }
            }
        }

        return true;
    }
}
