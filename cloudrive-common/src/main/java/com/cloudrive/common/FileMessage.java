package com.cloudrive.common;

public interface FileMessage {
    int getPartSize();
    int getCount();
    byte[] getPartFile();
    boolean isPacked();
    byte[] getDigest();
}
