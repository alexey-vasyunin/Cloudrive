public class Command implements TransferCommon, TransferCommand{

    private TransferCommandType command;
    private String[] params;

    public TransferObjectType getType() {
        return TransferObjectType.COMMAND;
    }

    public TransferCommandType getCommandType() {
        return command;
    }

    public Command(TransferCommandType command) {
        this.command = command;
    }

    public Command(TransferCommandType command, String... params) {
        this.command = command;
        this.params = params;
    }

    public boolean isReadyToTransfer(){
        return command != null;
    }
}
