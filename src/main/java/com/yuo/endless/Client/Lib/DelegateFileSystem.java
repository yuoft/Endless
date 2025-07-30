package com.yuo.endless.Client.Lib;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;
/**
 * DelegateFileSystem
 *
 * @author cnlimiter
 * @version 1.0
 * &#064;date  2024/6/7 上午2:35
 */
public class DelegateFileSystem extends FileSystem {

    private final FileSystem delegate;

    public DelegateFileSystem(FileSystem delegate) {
        this.delegate = delegate;
    }

    //@formatter:off
    @Override public FileSystemProvider provider() { return delegate.provider(); }
    @Override public void close() throws IOException { delegate.close(); }
    @Override public boolean isOpen() { return delegate.isOpen(); }
    @Override public boolean isReadOnly() { return delegate.isReadOnly(); }
    @Override public String getSeparator() { return delegate.getSeparator(); }
    @Override public Iterable<Path> getRootDirectories() { return delegate.getRootDirectories(); }
    @Override public Iterable<FileStore> getFileStores() { return delegate.getFileStores(); }
    @Override public Set<String> supportedFileAttributeViews() { return delegate.supportedFileAttributeViews(); }
    @Override public@NotNull Path getPath(@NotNull String first, String@NotNull... more) { return delegate.getPath(first, more); }
    @Override public PathMatcher getPathMatcher(String syntaxAndPattern) { return delegate.getPathMatcher(syntaxAndPattern); }
    @Override public UserPrincipalLookupService getUserPrincipalLookupService() { return delegate.getUserPrincipalLookupService(); }
    @Override public WatchService newWatchService() throws IOException { return delegate.newWatchService(); }
    //@formatter:on
}