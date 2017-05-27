package pl.bushee.jcompiler.definition;

import pl.bushee.jcompiler.definition.constant.Constant;

public interface ConstantPoolAccessor {
    int indexOf(final Constant constant);
}