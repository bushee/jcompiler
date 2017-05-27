package pl.bushee.jcompiler.definition;

import pl.bushee.jcompiler.definition.Attribute.Attributes;
import pl.bushee.jcompiler.definition.Method.Methods;
import pl.bushee.jcompiler.definition.constant.ClassReference;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

import static pl.bushee.jcompiler.definition.AccessFlag.AccessFlags;
import static pl.bushee.jcompiler.definition.Field.Fields;
import static pl.bushee.jcompiler.definition.Interface.Interfaces;

public class JClass {

    private static final long MAGIC = 0xCAFEBABE;

    private final ClassReference className;
    private final ClassReference superClass;
    private final Version version;
    private final AccessFlags accessFlags;
    private final Interfaces interfaces;
    private final Fields fields;
    private final Methods methods;
    private final Attributes attributes;

    private JClass(
        final ClassReference className,
        final ClassReference superClass,
        final Version version,
        final AccessFlags accessFlags,
        final Interfaces interfaces,
        final Fields fields,
        final Methods methods,
        final Attributes attributes
    ) {
        this.className = className;
        this.superClass = superClass;
        this.version = version;
        this.accessFlags = accessFlags;
        this.interfaces = interfaces;
        this.fields = fields;
        this.methods = methods;
        this.attributes = attributes;
    }

    public ClassReference getClassName() {
        return className;
    }

    public ClassReference getSuperClass() {
        return superClass;
    }

    public Version getVersion() {
        return version;
    }

    public EnumSet<AccessFlag> getAccessFlags() {
        return accessFlags.copy();
    }

    public Set<Interface> getInterfaces() {
        return interfaces.copy();
    }

    public Set<Field> getFields() {
        return fields.copy();
    }

    public Set<Method> getMethods() {
        return methods.copy();
    }

    public void writeToFile(final File outputFile) throws IOException {
        final ConstantPool constantPool = ConstantPool.forJClass(this);

        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(outputFile));
        dataOutputStream.writeInt((int) MAGIC);
        version.writeToFile(dataOutputStream);
        constantPool.writeToFile(dataOutputStream);
        accessFlags.writeToFile(dataOutputStream);
        dataOutputStream.writeShort(constantPool.indexOf(className));
        dataOutputStream.writeShort(constantPool.indexOf(superClass));
        interfaces.writeToFile(dataOutputStream);
        fields.writeToFile(dataOutputStream);
        methods.writeToFile(dataOutputStream);
        attributes.writeToFile(dataOutputStream);
        dataOutputStream.close();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ClassReference className;
        private ClassReference superClass = ClassReference.OBJECT;
        private Version version;
        private final AccessFlags accessFlags = new AccessFlags();
        private final Interfaces interfaces = new Interfaces();
        private final Fields fields = new Fields();
        private final Methods methods = new Methods();
        private final Attributes attributes = new Attributes();

        public Builder withClassName(final ClassReference classReference) {
            className = classReference;
            return this;
        }

        public Builder withClassName(final String className) {
            return withClassName(new ClassReference(className));
        }

        public Builder withClassName(final Class clazz) {
            return withClassName(new ClassReference(clazz));
        }

        public Builder withVersion(final Version version) {
            this.version = version;
            return this;
        }

        public Builder withVersion(final int major, final int minor) {
            return withVersion(new Version(major, minor));
        }

        public Builder withAccessFlags(final AccessFlag... accessFlags) {
            this.accessFlags.add(accessFlags);
            return this;
        }

        public Builder withInterfaces(final Interface... interfaces) {
            this.interfaces.add(interfaces);
            return this;
        }

        public Builder withFields(final Field... fields) {
            this.fields.add(fields);
            return this;
        }

        public Builder withMethods(final Method... methods) {
            this.methods.add(methods);
            return this;
        }

        public Builder withAttributes(final Attribute... attributes) {
            this.attributes.add(attributes);
            return this;
        }

        public JClass build() {
            return new JClass(className, superClass, version, accessFlags, interfaces, fields, methods, attributes);
        }
    }
}
