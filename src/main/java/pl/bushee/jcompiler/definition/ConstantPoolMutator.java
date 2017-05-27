package pl.bushee.jcompiler.definition;

import pl.bushee.jcompiler.definition.constant.Constant;

public interface ConstantPoolMutator {
    void add(final Constant constant);
}
