public interface TransferPartOfFile {
    int getPartSize();
    int getCount();
    byte[] getPartFile();
    boolean isPacked();
}
