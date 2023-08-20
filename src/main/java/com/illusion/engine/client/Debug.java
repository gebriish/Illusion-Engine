package com.illusion.engine.client;


public class Debug {
    private static String col = "\0";

    public static void Log(LogLevel level, Object message) {

        if(message == null)
            return;

        switch (level) {
            case MESSAGE -> {
                col = "\u001B[37m";
            }
            case INFO -> {
                col = "\u001B[32m";
            }
            case WARNING -> {
                col = "\u001B[33m";
            }
            case ERROR -> {
                col = "\u001B[31m";
            }
            case FATAL -> {
                col = "\u001B[35m" + "[!] ";
            }
        }


        System.out.println(col + message.toString()+ "\u001B[37m");
    }

    public static void Log(Object message) {
        if(message == null)
            return;

        Log(LogLevel.MESSAGE, message.toString());
    }

    public static void ClearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.println("CONSOLE CLEAR");
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
