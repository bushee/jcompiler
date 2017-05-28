package pl.bushee.jcompiler.definition.attribute;

import pl.bushee.jcompiler.definition.ConstantPoolAccessor;
import pl.bushee.jcompiler.definition.ConstantPoolMutator;

import java.io.DataOutputStream;
import java.io.IOException;

public class LocalVariableTable extends Attribute {
    @Override
    String getName() {
        return "LocalVariableTable";
    }

    @Override
    int getLength() {
        return 12;
    }

    @Override
    void addConstantsToPool(final ConstantPoolMutator constantPoolMutator) {
    }

    @Override
    void writeData(final ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.write(new byte[]{
            0, 1, // local variable table length
            // 1: String[] args
            0, 0, // start pc
            0, 1, // length
            0, 9, // name index
            0, 10, // descriptor index
            0, 0, // index
        });
    }
}
