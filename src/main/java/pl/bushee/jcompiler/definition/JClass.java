package pl.bushee.jcompiler.definition;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class JClass {

    private static final short ACC_PUBLIC = 0x0001;
    private static final short ACC_PRIVATE = 0x0002;
    private static final short ACC_PROTECTED = 0x0004;
    private static final short ACC_STATIC = 0x0008;
    private static final short ACC_FINAL = 0x0010;
    private static final short ACC_SYNCHRONIZED = 0x0020;
    private static final short ACC_BRIDGE = 0x0040;
    private static final short ACC_VARARGS = 0x0080;
    private static final short ACC_NATIVE = 0x0100;
    private static final short ACC_ABSTRACT = 0x0400;
    private static final short ACC_STRICT = 0x0800;
    private static final short ACC_SYNTHETIC = 0x1000;

    private static final byte[] MAGIC = {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE};
    private static final byte[] ACCESS_FLAGS = {0x10, 0x01};
    private static final int THIS_CLASS = 1;
    private static final int SUPER_CLASS = 3;

    private final String className;
    private final Class superClass;
    private Version version;

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

    public void writeToFile(final File outputFile) throws IOException {
        final ConstantPool constantPool = new ConstantPool(this);
        byte[] interfaces = {};
        byte[] interfacesCount = {0, 0};
        byte[] fields = {};
        byte[] fieldsCount = {0, 0};
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
        byte[] attributes = {};
        byte[] attributesCount = {0, 0};

        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(outputFile));
        dataOutputStream.write(MAGIC);
        version.writeToFile(dataOutputStream);
        constantPool.writeToFile(dataOutputStream);
        dataOutputStream.write(ACCESS_FLAGS);
        dataOutputStream.writeShort(THIS_CLASS);
        dataOutputStream.writeShort(SUPER_CLASS);
        dataOutputStream.write(interfacesCount);
        dataOutputStream.write(interfaces);
        dataOutputStream.write(fieldsCount);
        dataOutputStream.write(fields);
        dataOutputStream.write(methodsCount);
        dataOutputStream.write(methods);
        dataOutputStream.write(attributesCount);
        dataOutputStream.write(attributes);
        dataOutputStream.close();
    }
}
