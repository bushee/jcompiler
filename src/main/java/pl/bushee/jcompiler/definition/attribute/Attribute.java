package pl.bushee.jcompiler.definition.attribute;

import pl.bushee.jcompiler.definition.ConstantPoolAccessor;
import pl.bushee.jcompiler.definition.ConstantPoolMutator;
import pl.bushee.jcompiler.definition.ConstantRegistering;
import pl.bushee.jcompiler.definition.ConstantUsingWriter;
import pl.bushee.jcompiler.definition.constant.Utf8Value;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class Attribute implements ConstantRegistering, ConstantUsingWriter {
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

    public static class Attributes implements ConstantRegistering, ConstantUsingWriter {

        private final Set<Attribute> attributes = new HashSet<>();

        @Override
        public void addToPool(final ConstantPoolMutator constantPoolMutator) {
            attributes.forEach(attribute -> attribute.addToPool(constantPoolMutator));
        }

        @Override
        public void writeToFile(final ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) throws IOException {
            dataOutputStream.writeShort(attributes.size());
            for (Attribute attribute:attributes) {
                attribute.writeToFile(constantPoolAccessor,dataOutputStream);
            }
        }

        public void add(final Attribute... attributes) {
            Collections.addAll(this.attributes, attributes);
        }

        Set<Attribute> copy() {
            return new HashSet<>(attributes);
        }
    }
}