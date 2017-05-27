package pl.bushee.jcompiler.definition.constant.type;

public final class VoidType implements Type {
    @Override
    public String asConstantString() {
        return "V";
    }
}
