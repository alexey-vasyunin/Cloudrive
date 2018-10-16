package com.cloudrive.common.interfaces;

public interface FileMessage {
    int getPartSize();
    int getCount();
    byte[] getPartFile();
    boolean isPacked();
    byte[] getDigest();
}
