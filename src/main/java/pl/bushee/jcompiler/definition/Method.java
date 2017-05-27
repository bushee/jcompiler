package pl.bushee.jcompiler.definition;

import pl.bushee.jcompiler.definition.AccessFlag.AccessFlags;
import pl.bushee.jcompiler.definition.constant.Utf8Value;
import pl.bushee.jcompiler.definition.constant.type.ClassType;
import pl.bushee.jcompiler.definition.constant.type.Type;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Method implements ConstantRegistering, ConstantUsingWriter {
    private final AccessFlags accessFlags;
    private final Utf8Value name;
    private final Utf8Value methodDescriptor;

    private Method(
        final AccessFlags accessFlags,
        final Utf8Value name,
        final Utf8Value methodDescriptor
    ) {
        this.accessFlags = accessFlags;
        this.name = name;
        this.methodDescriptor = methodDescriptor;
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
    }

    @Override
    public void writeToFile(final ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) throws IOException {
        accessFlags.writeToFile(dataOutputStream);
        dataOutputStream.writeShort(constantPoolAccessor.indexOf(name));
        dataOutputStream.writeShort(constantPoolAccessor.indexOf(methodDescriptor));
        dataOutputStream.writeShort(2); // TODO: attributes count
        dataOutputStream.write(new byte[]{
            // attributes
            // 0:0: Code
            0, 7, // name index
            0, 0, 0, 21, // length
            0, 2, // max stack
            0, 1, // max locals
            0, 0, 0, 9, // code length
            (byte) 0xb2, 0, 11, // getstatic
            (byte) 0x12, 17, // ldc
            (byte) 0xb6, 0, 19, // invokevirtual
            (byte) 0xb1, // return
            0, 0, // exception table length
            // exception table
            0, 0, // attributes count
            // attribute info
            // 0:1: LocalVariableTable
            0, 8, // name index
            0, 0, 0, 12, // length
            0, 1, // local variable table length
            // 0:1:0: String[] args
            0, 0, // start pc
            0, 1, // length
            0, 9, // name index
            0, 10, // descriptor index
            0, 0, // index
        });
    }

    static class Methods implements ConstantUsingWriter {

        private final Set<Method> methods = new HashSet<>();

        @Override
        public void writeToFile(final ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) throws IOException {
            dataOutputStream.writeShort(methods.size());
            for (Method method : methods) {
                method.writeToFile(constantPoolAccessor, dataOutputStream);
            }
        }

        void add(final Method... methods) {
            Collections.addAll(this.methods, methods);
        }

        Set<Method> copy() {
            return new HashSet<>(methods);
        }
    }

    public static class Builder {
        private final AccessFlags accessFlags = new AccessFlags();
        private Utf8Value name;
        private Utf8Value methodDescriptor;

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

        public Method build() {
            return new Method(accessFlags, name, methodDescriptor);
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