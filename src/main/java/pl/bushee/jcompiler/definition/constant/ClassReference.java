package pl.bushee.jcompiler.definition.constant;

import pl.bushee.jcompiler.definition.Constant;
import pl.bushee.jcompiler.util.ClassNameConverter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public final class ClassReference extends Constant {
    private final Utf8Value className;

    public ClassReference(final String className) {
        this.className = new Utf8Value(ClassNameConverter.toInternal(className));
    }

    public ClassReference(final Class clazz) {
        this(clazz.getCanonicalName());
    }

    @Override
    final protected int getTag() {
        return 7;
    }

    @Override
    protected Constant[] getDependencies() {
        return new Constant[]{className};
    }

    @Override
    protected void writeData(final ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(constantPoolAccessor.indexOf(className));
    }

    @Override
    public int hashCode() {
        return Objects.hash(className);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ClassReference)) {
            return false;
        }

        ClassReference classReference = (ClassReference) obj;
        return Objects.equals(classReference.className, className);
    }

    @Override
    public String toString() {
        return "ClassReference[" + className + "]";
    }
}
