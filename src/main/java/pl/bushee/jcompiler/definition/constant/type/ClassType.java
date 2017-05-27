package pl.bushee.jcompiler.definition.constant.type;

import pl.bushee.jcompiler.util.ClassNameConverter;

public final class ClassType implements Type {
    private final String internalClassName;

    public ClassType(final String className) {
        this.internalClassName = ClassNameConverter.toInternal(className);
    }

    public ClassType(final Class clazz) {
        this.internalClassName = ClassNameConverter.toInternal(clazz);
    }

    @Override
    public String asConstantString() {
        return "L" + ClassNameConverter.toInternal(internalClassName) + ';';
    }
}
