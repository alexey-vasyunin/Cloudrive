public class Main {

    public static void main(String[] args) {
        PartOfFile pof = new PartOfFile();
        System.out.println(pof.isReadyToTransfer());

        Command c = new Command(TransferCommandType.RENAME, "was", "will");
        System.out.println(c.isReadyToTransfer());
    }

}
