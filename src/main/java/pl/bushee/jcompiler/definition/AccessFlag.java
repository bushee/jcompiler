package pl.bushee.jcompiler.definition;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;

public enum AccessFlag {
    PUBLIC(0x0001),
    PRIVATE(0x0002),
    PROTECTED(0x0004),
    STATIC(0x0008),
    FINAL(0x0010),
    SYNCHRONIZED(0x0020),
    BRIDGE(0x0040),
    VARARGS(0x0080),
    NATIVE(0x0100),
    ABSTRACT(0x0400),
    STRICT(0x0800),
    SYNTHETIC(0x1000);

    private final int value;

    AccessFlag(final int value) {
        this.value = value;
    }

    static class AccessFlags {

        private final EnumSet<AccessFlag> flags = EnumSet.noneOf(AccessFlag.class);

        void writeToFile(final DataOutputStream dataOutputStream) throws IOException {
            final int flagsBin = flags.stream().map(accessFlag -> accessFlag.value)
                .reduce((flags, flag) -> flags | flag)
                .orElse(0);

            dataOutputStream.writeShort(flagsBin);
        }

        void setTo(final AccessFlag[] accessFlags) {
            flags.clear();
            Collections.addAll(flags, accessFlags);
        }

        EnumSet<AccessFlag> copy() {
            return flags.clone();
        }
    }
}