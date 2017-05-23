package pl.bushee.jcompiler.definition;

public class Version {
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
}
