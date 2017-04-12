package com.dkelava.bgtrainer.controller;

import com.dkelava.bgtrainer.model.LearningParameters;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

class LearningParametersDialogController {

    private LearningParameters learningParameters;

    private JDialog dialog;
    private JTextField alphaTextField;
    private JTextField lambdaTextField;
    private JCheckBox isLearningEnabledCheckbox;
    private JCheckBox useAllGamesCheckbox;
    private JButton okButton;
    private JButton cancelButton;
    private boolean done = false;

    LearningParametersDialogController(LearningParameters learningParameters) {
        this.learningParameters = learningParameters;
    }

    public void show() {
        if(dialog == null) {
            constructDialog();
        }
        dialog.setVisible(true);
    }

    public void constructDialog() {
        alphaTextField = new JTextField();
        lambdaTextField = new JTextField();
        isLearningEnabledCheckbox = new JCheckBox("Enable learning");
        useAllGamesCheckbox = new JCheckBox("Use all games");
        okButton = new JButton(new OkAction());
        cancelButton = new JButton(new CancelAction());

        alphaTextField.setText(String.format("%f", learningParameters.getAlpha()));
        lambdaTextField.setText(String.format("%f", learningParameters.getLambda()));
        isLearningEnabledCheckbox.setSelected(learningParameters.isLearning());
        useAllGamesCheckbox.setSelected(learningParameters.useAllGames());

        dialog = new JDialog();
        dialog.setLayout(new GridBagLayout());
        dialog.add(new JLabel("Alpha:"), cellConstraint(0, 0));
        dialog.add(alphaTextField, cellConstraint(1, 0));
        dialog.add(new JLabel("Lambda:"), cellConstraint(0, 1));
        dialog.add(lambdaTextField, cellConstraint(1, 1));
        dialog.add(isLearningEnabledCheckbox, rowConstraint(2));
        dialog.add(useAllGamesCheckbox, rowConstraint(3));
        dialog.add(okButton, cellConstraint(0, 4));
        dialog.add(cancelButton, cellConstraint(1, 4));
        dialog.setModal(true);
        dialog.pack();
    }

    private static GridBagConstraints cellConstraint(int x, int y) {
        return new GridBagConstraints(
                x, y,
                1, 1,
                1.0, 1.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0,
                0);
    }

    private static GridBagConstraints rowConstraint(int y) {
        return new GridBagConstraints(
                0, y,
                2, 1,
                1.0, 1.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0,
                0);
    }

    public boolean isDone() {
        return done;
    }

    public LearningParameters getLearningParameters() {
        return new LearningParameters.Builder()
                .enableLearning(isLearningEnabled())
                .setUseAllGames(useAllGames())
                .setAlpha(getAlpha())
                .setLambda(getLambda()).build();
    }

    private double getAlpha() {
        try {
            return Double.parseDouble(alphaTextField.getText());
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    private double getLambda() {
        try {
            return Double.parseDouble(lambdaTextField.getText());
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    private boolean isLearningEnabled() {
        return isLearningEnabledCheckbox.isSelected();
    }

    private boolean useAllGames() {
        return useAllGamesCheckbox.isSelected();
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
            super("OK");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            done = false;
            dialog.setVisible(false);
            dialog.dispose();
        }
    }
}