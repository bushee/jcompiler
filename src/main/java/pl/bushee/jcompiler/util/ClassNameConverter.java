package pl.bushee.jcompiler.util;

public class ClassNameConverter {
    public static String toInternal(Class clazz) {
        return toInternal(clazz.getCanonicalName());
    }

    public static String toInternal(String className) {
        return className.replace('.', '/');
    }
}
