package pl.bushee.jcompiler.definition.constant.type;

public final class ArrayOf implements InternalType {

    private final Type type;

    public ArrayOf(Type type) {
        this.type = type;
    }

    @Override
    public String asConstantString() {
        return "[" + type.asConstantString();
    }
}
