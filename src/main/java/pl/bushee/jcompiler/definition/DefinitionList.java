package pl.bushee.jcompiler.definition;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DefinitionList<T extends Definition> implements ConstantRegistering, ConstantUsingWriter {

    private final Set<T> definitions = new HashSet<>();

    @Override
    public final void addToPool(final ConstantPoolMutator constantPoolMutator) {
        definitions.forEach(definition -> definition.addToPool(constantPoolMutator));
    }

    @Override
    public final void writeToFile(final ConstantPoolAccessor constantPoolAccessor, final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(definitions.size());
        for (T definition : definitions) {
            definition.writeToFile(constantPoolAccessor, dataOutputStream);
        }
    }

    @SafeVarargs
    final void add(final T... definition) {
        Collections.addAll(this.definitions, definition);
    }

    final Set<T> copy() {
        return new HashSet<>(definitions);
    }
}
