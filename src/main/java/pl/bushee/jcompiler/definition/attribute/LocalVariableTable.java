package pl.bushee.jcompiler.definition.attribute;

import pl.bushee.jcompiler.definition.ConstantPoolAccessor;
import pl.bushee.jcompiler.definition.ConstantPoolMutator;
import pl.bushee.jcompiler.definition.ConstantRegistering;
import pl.bushee.jcompiler.definition.ConstantUsingWriter;
import pl.bushee.jcompiler.definition.constant.Utf8Value;
import pl.bushee.jcompiler.definition.constant.type.ClassType;
import pl.bushee.jcompiler.definition.constant.type.Type;

import java.io.DataOutputStream;
import java.io.IOException;

public class LocalVariableTable extends Attribute {

    private final VariableDescription[] variableDescriptions;

    public LocalVariableTable(final VariableDescription... variableDescriptions) {
        this.variableDescriptions = variableDescriptions;
    }

    @Override
    String getName() {
        return "LocalVariableTable";
    }

    @Override
    int getLength() {
        return 2 + 10 * variableDescriptions.length;
    }

    @Override
    void addConstantsToPool(final ConstantPoolMutator constantPoolMutator) {
        for (VariableDescription variableDescription : variableDescriptions) {
            variableDescription.addToPool(constantPoolMutator);
        }
    }

    @Override
    void writeData(final ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(variableDescriptions.length);
        for (VariableDescription variableDescription : variableDescriptions) {
            variableDescription.writeToFile(constantPoolAccessor, dataOutputStream);
        }
    }

    public static class VariableDescription implements ConstantRegistering, ConstantUsingWriter {
        private final int firstCodeIndex;
        private final int codeLength;
        private final Utf8Value name;
        private final Utf8Value descriptor;
        private final int index;

        public VariableDescription(
            final int firstCodeIndex,
            final int codeLength,
            final Utf8Value name,
            final Utf8Value descriptor,
            final int index
        ) {
            this.firstCodeIndex = firstCodeIndex;
            this.codeLength = codeLength;
            this.name = name;
            this.descriptor = descriptor;
            this.index = index;
        }

        @Override
        public void addToPool(final ConstantPoolMutator constantPoolMutator) {
            constantPoolMutator.add(name);
            constantPoolMutator.add(descriptor);
        }

        @Override
        public void writeToFile(final ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) throws IOException {
            dataOutputStream.writeShort(firstCodeIndex);
            dataOutputStream.writeShort(codeLength);
            dataOutputStream.writeShort(constantPoolAccessor.indexOf(name));
            dataOutputStream.writeShort(constantPoolAccessor.indexOf(descriptor));
            dataOutputStream.writeShort(index);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private int firstCodeIndex;
            private int codeLength;
            private Utf8Value name;
            private Utf8Value descriptor;
            private int index;

            private Builder() {
            }

            public Builder withFirstCodeIndex(final int firstCodeIndex) {
                this.firstCodeIndex = firstCodeIndex;
                return this;
            }

            public Builder withCodeLength(final int codeLength) {
                this.codeLength = codeLength;
                return this;
            }

            public Builder withName(final String name) {
                return withName(new Utf8Value(name));
            }

            public Builder withName(final Utf8Value name) {
                this.name = name;
                return this;
            }

            public Builder withType(final Type type) {
                this.descriptor = new Utf8Value(type.asConstantString());
                return this;
            }

            public Builder withType(final Class type) {
                return withType(new ClassType(type));
            }

            public Builder withType(final String type) {
                return withType(new ClassType(type));
            }

            public Builder withIndex(final int index) {
                this.index = index;
                return this;
            }

            public VariableDescription build() {
                return new VariableDescription(firstCodeIndex, codeLength, name, descriptor, index);
            }
        }
    }
}
