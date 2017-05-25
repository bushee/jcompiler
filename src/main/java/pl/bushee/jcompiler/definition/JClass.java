package pl.bushee.jcompiler.definition;

import pl.bushee.jcompiler.definition.Attribute.Attributes;
import pl.bushee.jcompiler.definition.Method.Methods;

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
    private static final int THIS_CLASS = 1;
    private static final int SUPER_CLASS = 3;

    private final String className;
    private final Class superClass;
    private Version version;
    private final AccessFlags accessFlags = new AccessFlags();
    private final Interfaces interfaces = new Interfaces();
    private final Fields fields = new Fields();
    private final Methods methods = new Methods();
    private final Attributes attributes = new Attributes();

    public JClass(final String className) {
        this(className, Object.class);
    }

    public JClass(final String className, Class superClass) {
        this.className = className;
        this.superClass = superClass;
    }

    public String getClassName() {
        return className;
    }

    public Class getSuperClass() {
        return superClass;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(final Version version) {
        this.version = version;
    }

    public EnumSet<AccessFlag> getAccessFlags() {
        return accessFlags.copy();
    }

    public void setAccessFlags(AccessFlag... accessFlagArray) {
        accessFlags.setTo(accessFlagArray);
    }

    public Set<Interface> getInterfaces() {
        return interfaces.copy();
    }

    public void setInterfaces(Interface... interfacesArray) {
        interfaces.setTo(interfacesArray);
    }

    public Set<Field> getFields() {
        return fields.copy();
    }

    public void setFields(Field... fieldsArray) {
        fields.setTo(fieldsArray);
    }

    public Set<Method> getMethods() {
        return methods.copy();
    }

    public void setMethods(Method... methodsArray) {
        methods.setTo(methodsArray);
    }

    public void writeToFile(final File outputFile) throws IOException {
        final ConstantPool constantPool = new ConstantPool(this);

        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(outputFile));
        dataOutputStream.writeInt((int) MAGIC);
        version.writeToFile(dataOutputStream);
        constantPool.writeToFile(dataOutputStream);
        accessFlags.writeToFile(dataOutputStream);
        dataOutputStream.writeShort(THIS_CLASS);
        dataOutputStream.writeShort(SUPER_CLASS);
        interfaces.writeToFile(dataOutputStream);
        fields.writeToFile(dataOutputStream);
        methods.writeToFile(dataOutputStream);
        attributes.writeToFile(dataOutputStream);
        dataOutputStream.close();
    }
}
