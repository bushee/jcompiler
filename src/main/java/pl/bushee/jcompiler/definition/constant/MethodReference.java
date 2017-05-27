package pl.bushee.jcompiler.definition.constant;

import pl.bushee.jcompiler.definition.Constant;
import pl.bushee.jcompiler.definition.ConstantPoolAccessor;

import java.io.DataOutputStream;

public final class MethodReference extends Constant {
    @Override
    final protected int getTag() {
        return 10;
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
