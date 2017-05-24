package pl.bushee.jcompiler;

import pl.bushee.jcompiler.definition.Class;
import pl.bushee.jcompiler.definition.Version;

import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        File outputFile = new File("build/classes/test/Test.class");
        File parentFile = outputFile.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            System.err.println("Couldn't create parent directory");
        }

        Class jClass = new Class();
        jClass.setVersion(new Version(52, 0));

        jClass.writeToFile(outputFile);
    }
}