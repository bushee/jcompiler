package pl.bushee.jcompiler.definition;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Interface {
    // TODO

    static class Interfaces {

        private final Set<Interface> interfaces = new HashSet<>();

        void writeToFile(final DataOutputStream dataOutputStream) throws IOException {
            dataOutputStream.writeShort(interfaces.size());
            // TODO: write interface references
        }

        void setTo(final Interface[] interfacesArray) {
            interfaces.clear();
            Collections.addAll(interfaces, interfacesArray);
        }

        Set<Interface> copy() {
            return new HashSet<>(interfaces);
        }
    }
}