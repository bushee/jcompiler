package pl.bushee.jcompiler.definition;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Constant {
    abstract protected int getTag();

    abstract protected Constant[] getDependencies();

    abstract protected void writeData(ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) throws IOException;

    // abstract public int hashCode();

    // abstract public boolean equals(Object obj);

    // abstract public String toString();

    final protected void addToPool(final ConstantPool pool) {
        pool.add(this);
        for (Constant constant : getDependencies()) {
            pool.add(constant);
        }
    }

    final void writeToFile(final ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(getTag());
        writeData(constantPoolAccessor, dataOutputStream);
    }
}
