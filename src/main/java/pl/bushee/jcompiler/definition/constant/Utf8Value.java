package pl.bushee.jcompiler.definition.constant;

import pl.bushee.jcompiler.definition.Constant;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public final class Utf8Value extends Constant {
    private final String value;

    public Utf8Value(final String value) {
        this.value = value;
    }

    @Override
    final protected int getTag() {
        return 1;
    }

    @Override
    protected Constant[] getDependencies() {
        return new Constant[0];
    }

    @Override
    protected void writeData(final ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) throws IOException {
        final byte[] bytes = value.getBytes();
        dataOutputStream.writeShort(bytes.length);
        dataOutputStream.write(bytes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Utf8Value)) {
            return false;
        }

        Utf8Value utf8Value = (Utf8Value) obj;
        return Objects.equals(utf8Value.value, value);
    }

    @Override
    public String toString() {
        return "Utf8Value[" + value + "]";
    }
}
