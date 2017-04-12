package com.dkelava.bgtrainer.controller;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

class CreatePlayerDialogController {

    private JDialog dialog;
    private JTextField hiddenUnitsTextField;
    private JTextField nameTextField;

    private boolean done = false;

    public void show() {
        if(dialog == null) {
            constructDialog();
        }
        dialog.setVisible(true);
    }

    private void constructDialog() {
        hiddenUnitsTextField = new JTextField();
        nameTextField = new JTextField();
        JButton okButton = new JButton(new OkAction());
        JButton cancelButton = new JButton(new CancelAction());
        dialog = new JDialog();
        dialog.setLayout(new GridLayout(3,2));
        dialog.add(new JLabel("Name:"));
        dialog.add(nameTextField);
        dialog.add(new JLabel("Hidden units:"));
        dialog.add(hiddenUnitsTextField);
        dialog.add(okButton);
        dialog.add(cancelButton);
        dialog.setModal(true);
        dialog.pack();
    }

    public boolean isDone() {
        return done;
    }

    public int getNumberOfHiddenUnits() {
        try {
            return Integer.parseInt(hiddenUnitsTextField.getText());
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public String getName() {
        return nameTextField.getText();
    }

    private class OkAction extends AbstractAction {

        public OkAction() {
            super("OK");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            done = true;
            dialog.setVisible(false);
            dialog.dispose();
        }
    }

    private class CancelAction extends AbstractAction {

        public CancelAction() {
            super("Cancel");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            done = false;
            dialog.setVisible(false);
            dialog.dispose();
        }
    }
}