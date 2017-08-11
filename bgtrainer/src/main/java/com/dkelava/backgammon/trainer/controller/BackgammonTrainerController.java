package com.dkelava.backgammon.trainer.controller;

import com.dkelava.backgammon.trainer.model.BackgammonTrainer;
import com.dkelava.backgammon.trainer.model.Player;
import com.google.common.io.Files;

import org.jfree.data.xy.XYDataset;

import java.nio.file.FileSystems;
import java.util.Vector;

import java.io.File;
import java.io.IOException;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BackgammonTrainerController {

    /* MODEL */

    private volatile boolean isRunning = false;
    private int selection = -1;
    private BackgammonTrainer backgammonTrainer;
    private BackgammonTrainer.Observer observer = new BackgammonTrainer.Observer() {
        @Override
        public void onUpdate() {
            updatePlayers();
            update();
        }

        @Override
        public boolean isDone() {
            return !isRunning;
        }
    };

    /* VIEW */

    private JFrame mainFrame;

    private JTable playerTable;

    private JPanel controlPanel;
    private JButton startButton;
    private JButton stopButton;
    private JButton createPlayerButton;
    private JButton loadPlayerButton;
    private JButton removePlayerButton;
    private JButton saveButton;
    private JButton setLearningParametersButton;
    private JButton showChartButton;

    private JTextField statusTextField;


    public void show() {
        if(mainFrame == null) {
            construct();
        }
        if(backgammonTrainer == null) {
            restore();
            backgammonTrainer.setObserver(observer);
        }
        update();
        updatePlayers();
        mainFrame.setVisible(true);
    }

    private void construct() {
        playerTable = createPlayerTable();
        startButton = new JButton(new StartAction());
        stopButton = new JButton(new StopAction());
        createPlayerButton = new JButton(new CreatePlayerAction());
        loadPlayerButton = new JButton(new LoadPlayerAction());
        saveButton = new JButton(new SavePlayerAction());
        removePlayerButton = new JButton(new RemovePlayerAction());
        setLearningParametersButton = new JButton(new SetLearningParametersAction());
        showChartButton = new JButton(new ShowChartAction());
        controlPanel = createControlPanel();
        statusTextField = new JTextField();
        mainFrame = createMainFrame();
    }

    private void restore() {
        backgammonTrainer = BackgammonTrainer.restore();
        if(backgammonTrainer == null) {
            backgammonTrainer = new BackgammonTrainer();
        }
    }

    private JTable createPlayerTable() {
        final String[] colName = { "Name", "Games Played", "Wins", "Win Rate", "Examples" };

        JTable playerTable = new JTable() {
            public boolean isCellEditable(int nRow, int nCol) {
                return false;
            }
        };

        playerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        playerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                selection = playerTable.getSelectedRow();
                update();
            }
        });

        DefaultTableModel tableModel = (DefaultTableModel) playerTable.getModel();
        tableModel.setColumnIdentifiers(colName);

        playerTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                Player player = backgammonTrainer.get(row);
                if (player.isChanged()) {
                    c.setForeground(Color.RED);
                } else {
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        return playerTable;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        panel.add(startButton);
        panel.add(stopButton);
        panel.add(createPlayerButton);
        panel.add(loadPlayerButton);
        panel.add(saveButton);
        panel.add(removePlayerButton);
        panel.add(setLearningParametersButton);
        panel.add(showChartButton);
        return panel;
    }

    private JFrame createMainFrame() {
        JFrame frame = new JFrame("Backgammon Trainer");
        frame.setLayout(new GridBagLayout());

        GridBagConstraints playerListConstraints = new GridBagConstraints();
        playerListConstraints.gridx = 0;
        playerListConstraints.gridy = 0;
        playerListConstraints.weightx = 4.0;
        playerListConstraints.weighty = 1.0;
        playerListConstraints.fill = GridBagConstraints.BOTH;
        frame.add(new JScrollPane(playerTable), playerListConstraints);

        GridBagConstraints controlPanelConstraints = new GridBagConstraints();
        controlPanelConstraints.gridx = 1;
        controlPanelConstraints.gridy = 0;
        controlPanelConstraints.weighty = 1.0;
        controlPanelConstraints.anchor = GridBagConstraints.PAGE_START;
        frame.add(controlPanel, controlPanelConstraints);

        GridBagConstraints statusConstraints = new GridBagConstraints();
        statusConstraints.gridx = 0;
        statusConstraints.gridy = 1;
        statusConstraints.gridwidth = GridBagConstraints.REMAINDER;
        statusConstraints.weightx = 1.0;
        statusConstraints.fill = GridBagConstraints.HORIZONTAL;
        frame.add(statusTextField, statusConstraints);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(new Dimension(600, 300));

        return frame;
    }

    private void start() {
        if(!isRunning) {
            isRunning = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        backgammonTrainer.play();
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
            update();
        }
    }

    private void stop() {
        isRunning = false;
        update();
    }

    private void createPlayer() {
        CreatePlayerDialogController dialogController = new CreatePlayerDialogController();
        dialogController.show();
        if(dialogController.isDone()) {
            final String dir = System.getProperty("user.dir");
            String path = FileSystems.getDefault().getPath(dir, dialogController.getName()).toString();
            Player player = Player.create(dialogController.getName(), path, dialogController.getNumberOfHiddenUnits());
            addPlayer(player);
        }
    }

    private void loadPlayer() {
        final JFileChooser fc = new JFileChooser();
        final String dir = System.getProperty("user.dir");
        final File currentFolder = new File(dir);
        fc.setCurrentDirectory(currentFolder);
        int ret = fc.showOpenDialog(mainFrame);
        if(ret == JFileChooser.APPROVE_OPTION) {
            String fileName = fc.getSelectedFile().getName();
            String name = Files.getNameWithoutExtension(fileName);
            String path = fc.getSelectedFile().getPath();
            try {
                Player player = Player.load(name, path);
                addPlayer(player);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void save() {
        backgammonTrainer.save();
    }

    private void removePlayer() {
        if(isPlayerSelected()) {
            backgammonTrainer.remove(selection);
            getTableModel().removeRow(selection);
            if(backgammonTrainer.size() == 0) {
                selection = -1;
            }
            update();
        }
    }

    private void setLearningParameters() {
        if(isPlayerSelected()) {
            LearningParametersDialogController dialog = new LearningParametersDialogController(getSelectedPlayer().getLearningParameters());
            dialog.show();

            if (dialog.isDone()) {
                getSelectedPlayer().setLearningParameters(dialog.getLearningParameters());
                updatePlayers();
                update();
            }
        }
    }

    public void showChart() {
        XYDataset dataSet = backgammonTrainer.createDataSet();
        ChartDialogController dialogController = new ChartDialogController(dataSet);
        dialogController.show();
    }

    private void addPlayer(Player player) {
        backgammonTrainer.add(player);
        getTableModel().addRow(createRow(player));
        updatePlayers();
        update();
    }

    private Vector createRow(Player player) {
        Vector row = new Vector();
        row.add(player.getName());
        row.add(player.getPlayerStatistic().getTotalGames());
        row.add(player.getPlayerStatistic().getWins());
        row.add(player.getPlayerStatistic().getWinPercent());
        row.add(player.getPlayerStatistic().getExamples());
        return row;
    }

    private boolean isPlayerSelected() {
        return selection >= 0 && selection < backgammonTrainer.size();
    }

    private Player getSelectedPlayer() {
        if(isPlayerSelected()) {
            return (Player) backgammonTrainer.get(selection);
        } else {
            return null;
        }
    }

    private DefaultTableModel getTableModel() {
        return (DefaultTableModel) playerTable.getModel();
    }

    private void update() {
        boolean playersLoaded = !backgammonTrainer.isEmpty();
        boolean playerSelected = selection >= 0;

        // Update buttons
        stopButton.setEnabled(isRunning);
        startButton.setEnabled(!isRunning && playersLoaded);
        createPlayerButton.setEnabled(!isRunning);
        loadPlayerButton.setEnabled(!isRunning);
        saveButton.setEnabled(!isRunning);
        removePlayerButton.setEnabled(!isRunning && playerSelected);
        setLearningParametersButton.setEnabled(!isRunning && playerSelected);
        showChartButton.setEnabled(!isRunning);

        // Update status
        StringBuilder builder = new StringBuilder();
        builder.append("Iteration: " + backgammonTrainer.getIteration());
        statusTextField.setText(builder.toString());
    }

    private void updatePlayers() {
        if(backgammonTrainer.size() != getTableModel().getDataVector().size()) {
            getTableModel().getDataVector().clear();
            for(int r = 0; r < backgammonTrainer.size(); ++r) {
                getTableModel().getDataVector().add(r, createRow(backgammonTrainer.get(r)));
            }
        } else {
            for (int r = 0; r < backgammonTrainer.size(); ++r) {
                getTableModel().getDataVector().set(r, createRow(backgammonTrainer.get(r)));
            }
        }
        getTableModel().fireTableDataChanged();
    }


    /* ACTIONS */

    private class StartAction extends AbstractAction {

        public StartAction() {
            super("Start");
            putValue(SHORT_DESCRIPTION, "Start learning");
            putValue(MNEMONIC_KEY, KeyEvent.VK_L);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            start();
        }
    }

    private class StopAction extends AbstractAction {

        public StopAction() {
            super("Stop");
            putValue(SHORT_DESCRIPTION, "Stop learning");
            putValue(MNEMONIC_KEY, KeyEvent.VK_T);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            stop();
        }
    }

    private class CreatePlayerAction extends AbstractAction {

        public CreatePlayerAction() {
            super("Create Player");
            putValue(SHORT_DESCRIPTION, "Create new player");
            putValue(MNEMONIC_KEY, KeyEvent.VK_C);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            createPlayer();
        }
    }

    private class LoadPlayerAction extends AbstractAction {

        public LoadPlayerAction() {
            super("Load Player");
            putValue(SHORT_DESCRIPTION, "Load player from file");
            putValue(MNEMONIC_KEY, KeyEvent.VK_L);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            loadPlayer();
        }
    }

    private class SavePlayerAction extends AbstractAction {

        public SavePlayerAction() {
            super("Save");
            putValue(SHORT_DESCRIPTION, "Save players");
            putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            save();
        }
    }

    private class RemovePlayerAction extends AbstractAction {

        public RemovePlayerAction() {
            super("Remove Player");
            putValue(SHORT_DESCRIPTION, "Remove player");
            putValue(MNEMONIC_KEY, KeyEvent.VK_R);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            removePlayer();
        }
    }

    private class SetLearningParametersAction extends AbstractAction {

        public SetLearningParametersAction() {
            super("Set Parameters");
            putValue(SHORT_DESCRIPTION, "Set learning parameters");
            putValue(MNEMONIC_KEY, KeyEvent.VK_P);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setLearningParameters();
        }
    }

    private class ShowChartAction extends AbstractAction {

        public ShowChartAction() {
            super("Show Chart");
            putValue(SHORT_DESCRIPTION, "Show learning chart");
            putValue(MNEMONIC_KEY, KeyEvent.VK_C);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            showChart();
        }
    }

}
