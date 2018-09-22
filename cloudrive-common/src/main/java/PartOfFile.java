public class PartOfFile implements TransferPartOfFile, TransferCommon {
    private byte[] part;
    private int count;

    public PartOfFile() {
        this.count = -1;
    }

    public PartOfFile(byte[] part) {
        this.part = part;
        this.count = -1;
    }

    public PartOfFile(byte[] part, int count) {
        this.part = part;
        this.count = count;
    }

    public TransferObjectType getType() {
        return TransferObjectType.FILE;
    }

    public int getPartSize() {
        return part.length;
    }

    public int getCount() {
        return count;
    }

    public byte[] getPartFile() {
        return part;
    }

    public boolean isPacked() {
        return false;
    }

    public boolean isReadyToTransfer(){
        return count > -1 && part != null;
    }

}
