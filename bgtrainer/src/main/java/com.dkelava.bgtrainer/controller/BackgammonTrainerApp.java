package com.dkelava.bgtrainer.controller;

class BackgammonTrainerApp {
    public static void main(String[] args) throws Exception {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            BackgammonTrainerController controller = new BackgammonTrainerController();
            public void run() {
                controller.show();
            }
        });
    }
}