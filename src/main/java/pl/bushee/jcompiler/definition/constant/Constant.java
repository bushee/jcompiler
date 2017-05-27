package pl.bushee.jcompiler.definition.constant;

import pl.bushee.jcompiler.definition.ConstantPoolAccessor;
import pl.bushee.jcompiler.definition.ConstantPoolMutator;
import pl.bushee.jcompiler.definition.ConstantRegistering;
import pl.bushee.jcompiler.definition.ConstantUsingWriter;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Constant implements ConstantRegistering, ConstantUsingWriter {
    abstract protected int getTag();

    abstract protected Constant[] getDependencies();

    abstract protected void writeData(ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) throws IOException;

    // abstract public int hashCode();

    // abstract public boolean equals(Object obj);

    // abstract public String toString();

    @Override
    public final void addToPool(final ConstantPoolMutator constantPoolMutator) {
        constantPoolMutator.add(this);
        for (Constant constant : getDependencies()) {
            constantPoolMutator.add(constant);
        }
    }

    @Override
    public final void writeToFile(final ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(getTag());
        writeData(constantPoolAccessor, dataOutputStream);
    }
}
