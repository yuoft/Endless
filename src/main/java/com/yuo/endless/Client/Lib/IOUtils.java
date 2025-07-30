package com.yuo.endless.Client.Lib;

import javax.annotation.WillNotClose;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.*;
import java.util.*;

/**
 * IOUtil
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/6/7 上午2:34
 */
public class IOUtils {
    //32k buffer.
    private static final ThreadLocal<byte[]> arrayCache = ThreadLocal.withInitial(() -> new byte[32 * 1024]);
    private static final ThreadLocal<ByteBuffer> directBufferCache = ThreadLocal.withInitial(() -> ByteBuffer.allocateDirect(16 * 1024));

    /**
     * Returns a static per-thread cached 32k buffer for IO operations.
     *
     * @return The buffer.
     */
    public static byte[] getCachedBuffer() {
        return arrayCache.get();
    }

    /**
     * Copies the content of an {@link InputStream} to an {@link OutputStream}.
     *
     * @param is The {@link InputStream}.
     * @param os The {@link OutputStream}.
     * @throws IOException If something is bork.
     */
    public static void copy(@WillNotClose InputStream is, @WillNotClose OutputStream os) throws IOException {
        byte[] buffer = arrayCache.get();
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
    }

    /**
     * Copies the content of an {@link ReadableByteChannel} into an {@link WritableByteChannel}.
     * <p>
     * This method makes use of direct {@link ByteBuffer} instances.
     *
     * @param rc The {@link ReadableByteChannel} to copy from.
     * @param wc The {@link WritableByteChannel} to copy to.
     * @throws IOException If an IO Error occurred whilst copying.
     */
    public static void copy(@WillNotClose ReadableByteChannel rc, @WillNotClose WritableByteChannel wc) throws IOException {
        ByteBuffer buffer = directBufferCache.get();
        buffer.clear();
        while (rc.read(buffer) != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                wc.write(buffer);
            }
            buffer.clear();
        }
    }

    /**
     * Creates the parent directories of the given path if they don't exist.
     *
     * @param path The path.
     * @return The same path.
     * @throws IOException If an error occurs creating the directories.
     */
    public static Path makeParents(Path path) throws IOException {
        path = path.toAbsolutePath();
        Path parent = path.getParent();
        if (Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
        return path;
    }

}
