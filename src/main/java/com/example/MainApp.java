package com.example;

import org.apache.camel.main.Main;

public class MainApp {
    public static void main(String[] args) throws Exception {
        Main main = new Main();

        main.configure().addRoutesBuilder(new FileTransferRoute()); // File Transfer
        main.configure().addRoutesBuilder(new App()); // API REST

        main.run(args);
    }
}
