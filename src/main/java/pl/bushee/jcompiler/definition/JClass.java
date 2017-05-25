package pl.bushee.jcompiler.definition;

import pl.bushee.jcompiler.definition.Attribute.Attributes;

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

    public void writeToFile(final File outputFile) throws IOException {
        final ConstantPool constantPool = new ConstantPool(this);
        byte[] methods = {
            // 0: public static void main
            0, 0x09, // access flags
            0, 5, // name index
            0, 6, // descriptor index
            0, 2, // attributes count
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
        };
        byte[] methodsCount = {0, 1};

        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(outputFile));
        dataOutputStream.writeInt((int) MAGIC);
        version.writeToFile(dataOutputStream);
        constantPool.writeToFile(dataOutputStream);
        accessFlags.writeToFile(dataOutputStream);
        dataOutputStream.writeShort(THIS_CLASS);
        dataOutputStream.writeShort(SUPER_CLASS);
        interfaces.writeToFile(dataOutputStream);
        fields.writeToFile(dataOutputStream);
        dataOutputStream.write(methodsCount);
        dataOutputStream.write(methods);
        attributes.writeToFile(dataOutputStream);
        dataOutputStream.close();
    }
}
