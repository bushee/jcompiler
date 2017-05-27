package pl.bushee.jcompiler.definition;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class Attribute {
    // TODO

    static class Attributes {

        private final Set<Attribute> attributes = new HashSet<>();

        void writeToFile(final DataOutputStream dataOutputStream) throws IOException {
            dataOutputStream.writeShort(attributes.size());
            // TODO: write attributes
        }

        void add(final Attribute... attributes) {
            Collections.addAll(this.attributes, attributes);
        }

        Set<Attribute> copy() {
            return new HashSet<>(attributes);
        }
    }
}