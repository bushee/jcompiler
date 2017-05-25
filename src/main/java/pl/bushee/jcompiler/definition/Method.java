package pl.bushee.jcompiler.definition;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Method {
    // TODO

    static class Methods {

        private final Set<Method> methods = new HashSet<>();

        void writeToFile(final DataOutputStream dataOutputStream) throws IOException {
            byte[] methods = {
                // 0: public static void main
                0, 0x09, // access flags
                0, 5, // name index
                0, 6, // descriptor index
                0, 2, // attributes count
                // attributes
                // 0:0: Code
                0, 7, // name index
                0, 0, 0, 21, // length
                0, 2, // max stack
                0, 1, // max locals
                0, 0, 0, 9, // code length
                (byte) 0xb2, 0, 11, // getstatic
                (byte) 0x12, 17, // ldc
                (byte) 0xb6, 0, 19, // invokevirtual
                (byte) 0xb1, // return
                0, 0, // exception table length
                // exception table
                0, 0, // attributes count
                // attribute info
                // 0:1: LocalVariableTable
                0, 8, // name index
                0, 0, 0, 12, // length
                0, 1, // local variable table length
                // 0:1:0: String[] args
                0, 0, // start pc
                0, 1, // length
                0, 9, // name index
                0, 10, // descriptor index
                0, 0, // index
            };
            int methodsCount = 1;
            dataOutputStream.writeShort(methodsCount);
            dataOutputStream.write(methods);
        }

        void setTo(final Method[] methodsArray) {
            methods.clear();
            Collections.addAll(methods, methodsArray);
        }

        Set<Method> copy() {
            return new HashSet<>(methods);
        }
    }
}