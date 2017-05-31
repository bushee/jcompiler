package pl.bushee.jcompiler.definition;

import pl.bushee.jcompiler.definition.AccessFlag.AccessFlags;
import pl.bushee.jcompiler.definition.attribute.Attribute;
import pl.bushee.jcompiler.definition.attribute.Code;
import pl.bushee.jcompiler.definition.attribute.LocalVariableTable;
import pl.bushee.jcompiler.definition.constant.Utf8Value;
import pl.bushee.jcompiler.definition.constant.type.ClassType;
import pl.bushee.jcompiler.definition.constant.type.Type;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Method implements Definition {
    private final AccessFlags accessFlags;
    private final Utf8Value name;
    private final Utf8Value methodDescriptor;
    private final DefinitionList<Attribute> attributes;

    private Method(
        final AccessFlags accessFlags,
        final Utf8Value name,
        final Utf8Value methodDescriptor,
        final DefinitionList<Attribute> attributes
    ) {
        this.accessFlags = accessFlags;
        this.name = name;
        this.methodDescriptor = methodDescriptor;
        this.attributes = attributes;
    }

    public EnumSet<AccessFlag> getAccessFlags() {
        return accessFlags.copy();
    }

    public Utf8Value getName() {
        return name;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void addToPool(final ConstantPoolMutator constantPoolMutator) {
        constantPoolMutator.add(name);
        constantPoolMutator.add(methodDescriptor);
        attributes.addToPool(constantPoolMutator);
    }

    @Override
    public void writeToFile(final ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) throws IOException {
        accessFlags.writeToFile(dataOutputStream);
        dataOutputStream.writeShort(constantPoolAccessor.indexOf(name));
        dataOutputStream.writeShort(constantPoolAccessor.indexOf(methodDescriptor));
        attributes.writeToFile(constantPoolAccessor, dataOutputStream);
    }

    public static class Builder {
        private final AccessFlags accessFlags = new AccessFlags();
        private Utf8Value name;
        private Utf8Value methodDescriptor;
        private final DefinitionList<Attribute> attributes = new DefinitionList<>();

        private Builder() {
        }

        public Builder withAccessFlags(final AccessFlag... accessFlags) {
            this.accessFlags.add(accessFlags);
            return this;
        }

        public Builder withName(final Utf8Value name) {
            this.name = name;
            return this;
        }

        public Builder withName(final String name) {
            return withName(new Utf8Value(name));
        }

        public Builder withMethodDescriptor(final Type returnType, final Object... argumentTypes) {
            this.methodDescriptor = new Utf8Value("("
                + asTypeList(argumentTypes)
                .stream()
                .sequential()
                .map(Type::asConstantString)
                .reduce((a, b) -> a + b)
                .orElse("")
                + ")"
                + returnType.asConstantString()
            );
            return this;
        }

        public Builder withMethodDescriptor(final Class returnType, final Object... argumentTypes) {
            return withMethodDescriptor(new ClassType(returnType), argumentTypes);
        }

        public Builder withMethodDescriptor(final String returnType, final Object... argumentTypes) {
            return withMethodDescriptor(new ClassType(returnType), argumentTypes);
        }

        public Builder withCode(final Code code) {
            this.attributes.add(code);
            return this;
        }

        public Builder withLocalVariableTable(final LocalVariableTable localVariableTable) {
            this.attributes.add(localVariableTable);
            return this;
        }

        public Method build() {
            return new Method(accessFlags, name, methodDescriptor, attributes);
        }

        private List<Type> asTypeList(Object... argumentTypes) {
            final List<Type> types = new ArrayList<>(argumentTypes.length);
            for (int i = 0; i < argumentTypes.length; ++i) {
                Object argumentType = argumentTypes[i];
                if (argumentType instanceof String) {
                    types.add(new ClassType((String) argumentType));
                } else if (argumentType instanceof Class) {
                    types.add(new ClassType((Class) argumentType));
                } else if (argumentType instanceof Type) {
                    types.add((Type) argumentType);
                } else {
                    throw new IllegalArgumentException("Illegal argument " + argumentType + " at index " + i + "; one of Type, Class or String expected.");
                }
            }
            return types;
        }
    }
}