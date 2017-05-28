package pl.bushee.jcompiler.definition.attribute;

import pl.bushee.jcompiler.definition.ConstantPoolAccessor;
import pl.bushee.jcompiler.definition.ConstantPoolMutator;

import java.io.DataOutputStream;
import java.io.IOException;

public class Code extends Attribute {
    @Override
    String getName() {
        return "Code";
    }

    @Override
    int getLength() {
        return 21;
    }

    @Override
    void addConstantsToPool(final ConstantPoolMutator constantPoolMutator) {
    }

    @Override
    void writeData(final ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.write(new byte[]{
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
        });
    }
}
