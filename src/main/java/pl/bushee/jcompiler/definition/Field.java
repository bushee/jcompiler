package pl.bushee.jcompiler.definition;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Field {
    // TODO

    static class Fields {

        private final Set<Field> fields = new HashSet<>();

        void writeToFile(final DataOutputStream dataOutputStream) throws IOException {
            dataOutputStream.writeShort(fields.size());
            // TODO: write field infos
        }

        void add(final Field... fields) {
            Collections.addAll(this.fields, fields);
        }

        Set<Field> copy() {
            return new HashSet<>(fields);
        }
    }
}