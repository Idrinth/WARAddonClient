package de.idrinth.waraddonclient;


public final class Main {

    private Main() {
        //not to be used
    }

    public static void main(String[] args) {
        get(args).run();
    }

    private static BaseMain get(String[] args) {
        return args.length > 0 ? new CliMain(args) : new GuiMain();
    }
}
