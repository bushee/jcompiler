package pl.bushee.jcompiler.definition;

import java.io.DataOutputStream;
import java.io.IOException;

public interface ConstantUsingWriter {
    void writeToFile(final ConstantPoolAccessor constantPoolAccessor, DataOutputStream dataOutputStream) throws IOException;
}
