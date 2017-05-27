package pl.bushee.jcompiler.definition;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ConstantPool implements ConstantPoolAccessor {

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

    private final List<Constant> pool = new ArrayList<>();

    void add(final Constant constant) {
        if (!pool.contains(constant)) {
            pool.add(constant);
        }
    }

    @Override
    public int indexOf(final Constant constant) {
        return pool.indexOf(constant) + 1;
    }

    void writeToFile(final DataOutputStream dataOutputStream) throws IOException {
        final byte[][] constantPool = {{
            // 5: main method name
            CONSTANT_Utf8, // tag
            0, 4, // length
            'm', 'a', 'i', 'n',// class name
        }, {
            // 6: main method descriptor
            CONSTANT_Utf8, // tag
            0, 22, // length
            '(', '[', 'L', 'j', 'a', 'v', 'a', '/', 'l', 'a', 'n', 'g', '/', 'S', 't', 'r', 'i', 'n', 'g', ';', ')', 'V', // method descriptor
        }, {
            // 7: Code attribute name
            CONSTANT_Utf8, // tag,
            0, 4, // length
            'C', 'o', 'd', 'e', // attribute name
        }, {
            // 8: LocalVariableTable attribute name
            CONSTANT_Utf8, // tag,
            0, 18, // length
            'L', 'o', 'c', 'a', 'l', 'V', 'a', 'r', 'i', 'a', 'b', 'l', 'e', 'T', 'a', 'b', 'l', 'e', // attribute name
        }, {
            // 9: args parameter name
            CONSTANT_Utf8, // tag,
            0, 4, // length
            'a', 'r', 'g', 's', // parameter name
        }, {
            // 10: args parameter descriptor
            CONSTANT_Utf8, // tag,
            0, 19, // length
            '[', 'L', 'j', 'a', 'v', 'a', '/', 'l', 'a', 'n', 'g', '/', 'S', 't', 'r', 'i', 'n', 'g', ';', // parameter descriptor
        }, {
            // 11: System.out field reference
            CONSTANT_Fieldref, // tag
            0, 12, // class index
            0, 14, // field name and type
        }, {
            // 12: java.lang.System class
            CONSTANT_Class, // tag
            0, 13, // class name
        }, {
            // 13: java.lang.System
            CONSTANT_Utf8, // tag
            0, 16, // length
            'j', 'a', 'v', 'a', '/', 'l', 'a', 'n', 'g', '/', 'S', 'y', 's', 't', 'e', 'm', // class name
        }, {
            // 14: PrintStream out field
            CONSTANT_NameAndType, // tag
            0, 15, // field name
            0, 16, // field descriptor
        }, {
            // 15: out field name
            CONSTANT_Utf8, // tag
            0, 3, // length
            'o', 'u', 't', // field name
        }, {
            // 16: System.out field descriptor
            CONSTANT_Utf8, // tag
            0, 21, // length
            'L', 'j', 'a', 'v', 'a', '/', 'i', 'o', '/', 'P', 'r', 'i', 'n', 't', 'S', 't', 'r', 'e', 'a', 'm', ';', // class name
        }, {
            // 17: 'Bello!' string constant
            CONSTANT_String, // tag
            0, 18, // value index
        }, {
            // 18: 'Bello!' string constant value
            CONSTANT_Utf8, // tag
            0, 6, // length
            'B', 'e', 'l', 'l', 'o', '!', // string const
        }, {
            // 19: PrintStream.println method reference
            CONSTANT_Methodref, // tag
            0, 20, // class index
            0, 22, // method name and type
        }, {
            // 20: java.io.PrintStream class
            CONSTANT_Class, // tag
            0, 21, // class name
        }, {
            // 21: java.lang.System
            CONSTANT_Utf8, // tag
            0, 19, // length
            'j', 'a', 'v', 'a', '/', 'i', 'o', '/', 'P', 'r', 'i', 'n', 't', 'S', 't', 'r', 'e', 'a', 'm', // class name
        }, {
            // 22: PrintStream.println method
            CONSTANT_NameAndType, // tag
            0, 23, // field name
            0, 24, // field descriptor
        }, {
            // 23: println method name
            CONSTANT_Utf8, // tag
            0, 7, // length
            'p', 'r', 'i', 'n', 't', 'l', 'n', // string const
        }, {
            // 24: PrintStream.println method descriptor
            CONSTANT_Utf8, // tag
            0, 21, // length
            '(', 'L', 'j', 'a', 'v', 'a', '/', 'l', 'a', 'n', 'g', '/', 'S', 't', 'r', 'i', 'n', 'g', ';', ')', 'V', // method descriptor
        }};

        dataOutputStream.writeShort(pool.size() + constantPool.length + 1);
        for (Constant constant : pool) {
            constant.writeToFile(this, dataOutputStream);
        }
        for (byte[] constant : constantPool) {
            dataOutputStream.write(constant);
        }
    }

    static ConstantPool forJClass(final JClass jClass) {
        final ConstantPool pool = new ConstantPool();
        jClass.getClassName().addToPool(pool);
        jClass.getSuperClass().addToPool(pool);
        return pool;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ConstantPool[\n");
        for (int i = 0; i < pool.size(); ++i) {
            stringBuilder.append(i + 1)
                .append(": ")
                .append(pool.get(i))
                .append(",\n");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
