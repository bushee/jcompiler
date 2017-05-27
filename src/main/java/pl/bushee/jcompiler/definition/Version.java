package pl.bushee.jcompiler.definition;

import java.io.DataOutputStream;
import java.io.IOException;

public class Version {
    // TODO: constants for predefined versions

    private final int major;
    private final int minor;

    public Version(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    void writeToFile(final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(minor);
        dataOutputStream.writeShort(major);
    }
}
