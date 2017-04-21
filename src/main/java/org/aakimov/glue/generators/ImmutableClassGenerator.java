package org.aakimov.glue.generators;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Immutable class generator.
 *
 * Can be used to generate immutable data objects based on the given interface.
 *
 * @see org.aakimov.glue.annotations.ImmutableImplementation
 * @author aakimov
 */
public class ImmutableClassGenerator {

    /**
     * Generate immutable class for the given interface.
     *
     * This ugly boilerplate code is used for better performance instead of custom template engine usage.
     *
     * @param filer filer instance to create new source file
     * @param packageName package name of the new class
     * @param className name of the new class
     * @param parentInterfaceName parent interface name
     * @param properties list of the properties in the new class in form of the map that maps property to its type
     * @throws IOException thrown if source file cannot be created
     */
    public void generateSource(
        Filer filer,
        String packageName,
        String className,
        String parentInterfaceName,
        Map<String, String> properties
    ) throws IOException {
        JavaFileObject javaFileObject = filer.createSourceFile(packageName + "." + className);

        BufferedWriter bufferedWriter = new BufferedWriter(javaFileObject.openWriter());
        bufferedWriter.append("package ");
        bufferedWriter.append(packageName);
        bufferedWriter.append(";");
        bufferedWriter.newLine();
        bufferedWriter.newLine();
        bufferedWriter.append("public class ");
        bufferedWriter.append(className);
        bufferedWriter.append(" implements ");
        bufferedWriter.append(parentInterfaceName);
        bufferedWriter.append(" {");
        bufferedWriter.newLine();

        for (Map.Entry<String, String> entry: properties.entrySet()) {
            bufferedWriter.append("    private final ");
            bufferedWriter.append(entry.getValue());
            bufferedWriter.append(" ");
            bufferedWriter.append(entry.getKey());
            bufferedWriter.append(";");
            bufferedWriter.newLine();
        }

        bufferedWriter.newLine();
        bufferedWriter.append("    public ");
        bufferedWriter.append(className);
        bufferedWriter.append("(");
        bufferedWriter.newLine();

        boolean insertComma = false;
        for (Map.Entry<String, String> entry: properties.entrySet()) {
            if (insertComma) {
                bufferedWriter.append(",");
                bufferedWriter.newLine();
            } else {
                insertComma = true;
            }
            bufferedWriter.append("        ");
            bufferedWriter.append(entry.getValue());
            bufferedWriter.append(" ");
            bufferedWriter.append(entry.getKey());
        }
        bufferedWriter.newLine();
        bufferedWriter.append("    ) {");
        bufferedWriter.newLine();

        for (Map.Entry<String, String> entry: properties.entrySet()) {
            bufferedWriter.append("        this.");
            bufferedWriter.append(entry.getKey());
            bufferedWriter.append(" = ");
            bufferedWriter.append(entry.getKey());
            bufferedWriter.append(";");
            bufferedWriter.newLine();
        }

        bufferedWriter.append("    }");
        bufferedWriter.newLine();
        bufferedWriter.newLine();

        for (Map.Entry<String, String> entry: properties.entrySet()) {
            bufferedWriter.append("    public ");
            bufferedWriter.append(entry.getValue());
            bufferedWriter.append(" ");
            bufferedWriter.append("get");
            bufferedWriter.append(entry.getKey().substring(0, 1).toUpperCase());
            bufferedWriter.append(entry.getKey().substring(1));
            bufferedWriter.append("() {");
            bufferedWriter.newLine();
            bufferedWriter.append("        return this.");
            bufferedWriter.append(entry.getKey());
            bufferedWriter.append(";");
            bufferedWriter.newLine();
            bufferedWriter.append("    }");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
        }

        bufferedWriter.newLine();
        bufferedWriter.append("}");
        bufferedWriter.newLine();
        bufferedWriter.close();
    }
}
