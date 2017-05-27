package pl.bushee.jcompiler;

import pl.bushee.jcompiler.definition.AccessFlag;
import pl.bushee.jcompiler.definition.JClass;

import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        File outputFile = new File("build/classes/test/Test.class");
        File parentFile = outputFile.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            System.err.println("Couldn't create parent directory");
        }

        JClass jClass = JClass.builder()
            .withClassName("Test")
            .withVersion(52, 0)
            .withAccessFlags(AccessFlag.PUBLIC, AccessFlag.SYNTHETIC)
            .build();

        jClass.writeToFile(outputFile);
    }
}
