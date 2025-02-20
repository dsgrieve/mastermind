package com.kodewerk.mastermind;

import com.kodewerk.math.Arrangement;
import com.kodewerk.math.Index;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * Swing based user interface for MasterMind
 * Accepts up to five input fields
 * Number of Symbols in the Arrangement
 * The Length of the Arrangement (must be less then the number of symbols)
 * number of threads (or Players)
 * Chunk sise to pass out from the CandidateSolutionStack
 * Optional index for solution. If not provided will be choosen randomly.
 *
 * Once spawning off the game, this interface becomes an observer and reports on progress
 * as reported by MasterMind in a text box.
 *
 */
    
public class MasterMindGUI extends JFrame implements MasterMindGameObserver {
    private static final String TITLE = "Mastermind";

    private final JTextField numberOfSymbolsField = new JTextField("100000", 10);
    private final JTextField arrangementLengthField = new JTextField("3", 10);
    private final JTextField numberOfPlayersField = new JTextField("8", 10);
    private final JTextField batchSizeField = new JTextField( "5000", 10);
    private final JTextField indexField = new JTextField("642000", 10);
    private final JTextField solution = new JTextField(10);
    private final JTextField timer = new JTextField(10);
    private final JTextArea output = new JTextArea();
    private JButton runButton;
    //private boolean running;
    private MasterMind game;
    private long startTime;

    public MasterMindGUI() {
        super.setTitle( TITLE);
        super.setLayout( new BorderLayout());
        JPanel listPane = new JPanel();
        listPane.setLayout( new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        listPane.setBorder(BorderFactory.createEmptyBorder(30, 70, 10, 70));
        listPane.add( buildSettingsPanel());
        listPane.add( buildTextArea());
        listPane.add( buildButtonsView());
        super.add( listPane, BorderLayout.CENTER);
    }

    public JPanel buildButtonsView() {
        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        runButton = new JButton("Run Test");
        buttons.add(runButton);
        JButton clearButton = new JButton("Clear");
        buttons.add(clearButton);
        JButton abandonButton = new JButton("Abandon");
        buttons.add(abandonButton);
        JButton exitButton = new JButton("Exit");
        buttons.add(exitButton);

        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                execute();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        abandonButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abort();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        return buttons;
    }

    public JPanel buildTextArea() {

        JPanel panel = new JPanel();
        panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Guess made - Correct Color & Position - Correct Color"));
        output.setRows( 10);
        panel.add( new JScrollPane( output));
        return panel;
    }

    public JPanel buildSettingsPanel() {

        JLabel label;

        JPanel settingView = new JPanel(new GridLayout(0, 2, 10, 10));

        settingView.setBorder(BorderFactory.createTitledBorder("Settings:"));
        settingView.setOpaque(false);

        label = new JLabel("# Symbols:", JLabel.RIGHT);
        label.setToolTipText("Number of symbols the game will use to generate a pattern");
        settingView.add( label);
        settingView.add(numberOfSymbolsField);

        label = new JLabel("Solution Length:", JLabel.RIGHT);
        label.setToolTipText("Number of symbols in the solution");
        settingView.add( label);
        settingView.add(arrangementLengthField);

        label = new JLabel("# Players:", JLabel.RIGHT);
        label.setToolTipText("The role of a player is to make guesses. Each player will have it's own thread");
        settingView.add( label);
        settingView.add(numberOfPlayersField);

        label = new JLabel("Batch size", JLabel.RIGHT);
        label.setToolTipText("Allow the player to take more than 1 candiate solution from the solution stack.");
        settingView.add( label);
        settingView.add( batchSizeField);

        label = new JLabel("Select Solution Index:", JLabel.RIGHT);
        label.setToolTipText("Set the solution rather than having a random solution choosen. The value is an index into the set of all possible solutions");
        settingView.add(label);
        settingView.add(indexField);
        this.timer.setEditable(false);
        label = new JLabel("Time (ms):", JLabel.RIGHT);
        label.setToolTipText("Run time in ms");
        settingView.add(label);

        settingView.add(timer);
		
        label = new JLabel("Solution:", JLabel.RIGHT);
        label.setToolTipText("Solution to the last game");
        settingView.add(label);
        this.solution.setEditable(false);
        settingView.add(solution);

        return settingView;
    }

    private void setRunning( boolean value) {
        try {
            if ( value) {
                this.startTime = System.currentTimeMillis();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        solution.setText("Running");
                        solution.setBackground(Color.GREEN);
                    }
                });
            } else {
                SwingUtilities.invokeLater(() -> solution.setBackground(Color.WHITE));
	    }
        } finally {
            runButton.setEnabled( ! value);
            //this.running = value;
        }
    }

    //private boolean isRunning() { return this.running; }

    public void guessMade(final Arrangement arrangement, final Score score) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                output.append(arrangement.toString() + " : " + score.toString() + "\n");
            }
        });
    }

    public void jackpotted(final Arrangement answer) {
        this.setRunning(false);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                solution.setText(answer.toString());
	        timer.setText( Long.toString(System.currentTimeMillis() - startTime));
            }
        });
    }

    public void execute() {
        System.gc();
        game = new MasterMind();
        int numberOfSymbols = Integer.parseInt(numberOfSymbolsField.getText());
        int arrangementLength = Integer.parseInt(arrangementLengthField.getText());
        int numberOfPlayers = Integer.parseInt(numberOfPlayersField.getText());
        int batchSize = Integer.parseInt( batchSizeField.getText());

        boolean indexSet = !indexField.getText().equals("");
        Index index = null;
        if (indexSet) {
            index = new Index(indexField.getText());
        }

        game.addMasterMindGameObserver( this);
        this.setRunning( true);
        game.play(numberOfSymbols, arrangementLength, index, numberOfPlayers, batchSize);
    }

    public void clear() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                solution.setText("");
                output.setText("");
                timer.setText("");
            }
        });
    }

    public void abort() {
        this.game.abandon();
        this.clear();
        this.setRunning(false);
    }

    public static void main(String[] args) {
        MasterMindGUI frame = new MasterMindGUI();
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
