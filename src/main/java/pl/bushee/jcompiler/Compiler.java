package pl.bushee.jcompiler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Compiler {

    private static final byte CONSTANT_Class = 7;
    private static final byte CONSTANT_Fieldref = 9;
    private static final byte CONSTANT_Methodref = 10;
    private static final byte CONSTANT_InterfaceMethodref = 11;
    private static final byte CONSTANT_String = 8;
    private static final byte CONSTANT_Integer = 3;
    private static final byte CONSTANT_Float = 4;
    private static final byte CONSTANT_Long = 5;
    private static final byte CONSTANT_Double = 6;
    private static final byte CONSTANT_NameAndType = 12;
    private static final byte CONSTANT_Utf8 = 1;
    private static final byte CONSTANT_MethodHandle = 15;
    private static final byte CONSTANT_MethodType = 16;
    private static final byte CONSTANT_InvokeDynamic = 18;

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
    private static final byte[] MAJOR_VERSION = {0, 52};
    private static final byte[] MINOR_VERSION = {0, 0};
    private static final byte[] ACCESS_FLAGS = {0x10, 0x01};
    private static final byte[] THIS_CLASS = {0, 1};
    private static final byte[] SUPER_CLASS = {0, 3};

    public static void main(String[] args) throws IOException {
        byte[] constantPool = {
            // 1: this_class
            CONSTANT_Class, // tag
            0, 2, // index

            // 2: this class name
            CONSTANT_Utf8, //tag
            0, 4, // length
            'T', 'e', 's', 't', // class name

            // 3: super_class
            CONSTANT_Class, // tag
            0, 4, // index

            // 4: java.lang.Object
            CONSTANT_Utf8, // tag
            0, 16, // length
            'j', 'a', 'v', 'a', '/', 'l', 'a', 'n', 'g', '/', 'O', 'b', 'j', 'e', 'c', 't', // class name

            // 5: main method name
            CONSTANT_Utf8, // tag
            0, 4, // length
            'm', 'a', 'i', 'n',// class name

            // 6: main method descriptor
            CONSTANT_Utf8, // tag
            0, 22, // length
            '(', '[', 'L', 'j', 'a', 'v', 'a', '/', 'l', 'a', 'n', 'g', '/', 'S', 't', 'r', 'i', 'n', 'g', ';', ')', 'V', // method descriptor

            // 7: Code attribute name
            CONSTANT_Utf8, // tag,
            0, 4, // length
            'C', 'o', 'd', 'e', // attribute name

            // 8: LocalVariableTable attribute name
            CONSTANT_Utf8, // tag,
            0, 18, // length
            'L', 'o', 'c', 'a', 'l', 'V', 'a', 'r', 'i', 'a', 'b', 'l', 'e', 'T', 'a', 'b', 'l', 'e', // attribute name

            // 9: args parameter name
            CONSTANT_Utf8, // tag,
            0, 4, // length
            'a', 'r', 'g', 's', // parameter name

            // 10: args parameter descriptor
            CONSTANT_Utf8, // tag,
            0, 19, // length
            '[', 'L', 'j', 'a', 'v', 'a', '/', 'l', 'a', 'n', 'g', '/', 'S', 't', 'r', 'i', 'n', 'g', ';', // parameter descriptor

            // 11: System.out field reference
            CONSTANT_Fieldref, // tag
            0, 12, // class index
            0, 14, // field name and type

            // 12: java.lang.System class
            CONSTANT_Class, // tag
            0, 13, // class name

            // 13: java.lang.System
            CONSTANT_Utf8, // tag
            0, 16, // length
            'j', 'a', 'v', 'a', '/', 'l', 'a', 'n', 'g', '/', 'S', 'y', 's', 't', 'e', 'm', // class name

            // 14: PrintStream out field
            CONSTANT_NameAndType, // tag
            0, 15, // field name
            0, 16, // field descriptor

            // 15: out field name
            CONSTANT_Utf8, // tag
            0, 3, // length
            'o', 'u', 't', // field name

            // 16: System.out field descriptor
            CONSTANT_Utf8, // tag
            0, 21, // length
            'L', 'j', 'a', 'v', 'a', '/', 'i', 'o', '/', 'P', 'r', 'i', 'n', 't', 'S', 't', 'r', 'e', 'a', 'm', ';', // class name

            // 17: 'Bello!' string constant
            CONSTANT_String, // tag
            0, 18, // value index

            // 18: 'Bello!' string constant value
            CONSTANT_Utf8, // tag
            0, 6, // length
            'B', 'e', 'l', 'l', 'o', '!', // string const

            // 19: PrintStream.println method reference
            CONSTANT_Methodref, // tag
            0, 20, // class index
            0, 22, // method name and type

            // 20: java.io.PrintStream class
            CONSTANT_Class, // tag
            0, 21, // class name

            // 21: java.lang.System
            CONSTANT_Utf8, // tag
            0, 19, // length
            'j', 'a', 'v', 'a', '/', 'i', 'o', '/', 'P', 'r', 'i', 'n', 't', 'S', 't', 'r', 'e', 'a', 'm', // class name

            // 22: PrintStream.println method
            CONSTANT_NameAndType, // tag
            0, 23, // field name
            0, 24, // field descriptor

            // 23: println method name
            CONSTANT_Utf8, // tag
            0, 7, // length
            'p', 'r', 'i', 'n', 't', 'l', 'n', // string const

            // 24: PrintStream.println method descriptor
            CONSTANT_Utf8, // tag
            0, 21, // length
            '(', 'L', 'j', 'a', 'v', 'a', '/', 'l', 'a', 'n', 'g', '/', 'S', 't', 'r', 'i', 'n', 'g', ';', ')', 'V', // method descriptor
        };
        byte[] constantPoolCount = {0, 25};
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

        File outputFile = new File("build/classes/test/Test.class");
        File parentFile = outputFile.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            System.err.println("Couldn't create parent directory");
        }
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(outputFile));
        dataOutputStream.write(MAGIC);
        dataOutputStream.write(MINOR_VERSION);
        dataOutputStream.write(MAJOR_VERSION);
        dataOutputStream.write(constantPoolCount);
        dataOutputStream.write(constantPool);
        dataOutputStream.write(ACCESS_FLAGS);
        dataOutputStream.write(THIS_CLASS);
        dataOutputStream.write(SUPER_CLASS);
        dataOutputStream.write(interfacesCount);
        dataOutputStream.write(interfaces);
        dataOutputStream.write(fieldsCount);
        dataOutputStream.write(fields);
        dataOutputStream.write(methodsCount);
        dataOutputStream.write(methods);
        dataOutputStream.write(attributesCount);
        dataOutputStream.write(attributes);
        // TODO: attributes
        dataOutputStream.close();
    }
}
