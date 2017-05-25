package pl.bushee.jcompiler.definition.constant;

import pl.bushee.jcompiler.definition.Constant;

import java.io.DataOutputStream;

public final class InvokeDynamic extends Constant {
    @Override
    final protected int getTag() {
        return 18;
    }

    @Override
    protected Constant[] getDependencies() {
        return new Constant[0];
    }

    @Override
    protected void writeData(final ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) {
        // TODO
    }
}
