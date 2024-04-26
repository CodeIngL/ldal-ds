package com.codeL.data.ds.atom.file;

import java.io.*;

public class IOUtils {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static String toString(InputStream input, String encoding)
            throws IOException {
        StringWriter sw = new StringWriter();
        copy(input, sw, encoding);
        return sw.toString();
    }

    public static void copy(InputStream input, Writer output, String encoding)
            throws IOException {
        if (encoding == null) {
            copy(input, output);
        } else {
            InputStreamReader in = new InputStreamReader(input, encoding);
            copy(in, output);
        }
    }

    public static void copy(InputStream input, Writer output)
            throws IOException {
        InputStreamReader in = new InputStreamReader(input);
        copy(in, output);
    }

    public static int copy(Reader input, Writer output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(Reader input, Writer output) throws IOException {
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static void closeQuietly(InputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    public static void closeQuietly(OutputStream output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
}
