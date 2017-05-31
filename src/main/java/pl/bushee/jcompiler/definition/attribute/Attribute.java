package pl.bushee.jcompiler.definition.attribute;

import pl.bushee.jcompiler.definition.ConstantPoolAccessor;
import pl.bushee.jcompiler.definition.ConstantPoolMutator;
import pl.bushee.jcompiler.definition.Definition;
import pl.bushee.jcompiler.definition.constant.Utf8Value;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Attribute implements Definition {
    @Override
    public final void addToPool(final ConstantPoolMutator constantPoolMutator) {
        constantPoolMutator.add(new Utf8Value(getName()));
        addConstantsToPool(constantPoolMutator);
    }

    @Override
    public final void writeToFile(final ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(constantPoolAccessor.indexOf(new Utf8Value(getName())));
        dataOutputStream.writeInt(getLength());
        writeData(constantPoolAccessor, dataOutputStream);
    }

    abstract String getName();

    abstract int getLength();

    abstract void addConstantsToPool(final ConstantPoolMutator constantPoolMutator);

    abstract void writeData(final ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) throws IOException;
}