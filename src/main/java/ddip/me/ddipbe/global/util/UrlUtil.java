package ddip.me.ddipbe.global.util;

public class UrlUtil {
    /**
     * join with "/".
     * Remove trailing or leading "/" if exists.
     * But, first path's leading "/" is not removed.
     * Also, last path's trailing "/" is not removed.
     */
    public static String join(String... paths) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];

            if (i > 0 && path.startsWith("/")) {
                path = path.substring(1);
            }

            if (i < paths.length - 1 && path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }

            sb.append(path);
            if (i < paths.length - 1) {
                sb.append("/");
            }
        }

        return sb.toString();
    }
}
