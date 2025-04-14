package cma;

public final class CMaUtils {
    private CMaUtils() {

    }

    public static boolean int2bool(int i) {
        return i != 0;
    }

    public static int bool2int(boolean b) {
        return b ? 1 : 0;
    }
}
