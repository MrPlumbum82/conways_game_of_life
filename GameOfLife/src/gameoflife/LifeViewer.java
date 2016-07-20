package gameoflife;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A frame with a LifeComopnent and a number of GUI controls for watching Life
 * evolve.
 *
 * @author sm52192
 * @vesion Apr 15, 2014
 */
public class LifeViewer extends JFrame {

    private final LifeComponent lifeComp;

    public LifeViewer(int n) {
        lifeComp = new LifeComponent(n);
        lifeComp.setBorder(new EtchedBorder());
        add(lifeComp, BorderLayout.CENTER);

        setTitle("Game of Life");
        add(getNorthPanel(), BorderLayout.NORTH);
        add(getSouthPanel(), BorderLayout.SOUTH);
        add(getEastPanel(), BorderLayout.EAST);

        // menus 
        JMenuBar mBar = new JMenuBar();
        setJMenuBar(mBar);
        mBar.add(getInitConfigMenus());

    }

    public static void main(String[] args) {
        LifeViewer viewer = new LifeViewer(100);
        viewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewer.setSize(750, 720);

        viewer.setAlwaysOnTop(true);
        viewer.setVisible(true);

    }

    /**
     * @return a panel with buttons for updating generations
     */
    private Component getNorthPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(7, 0, 7, 0));
        final JButton stepButton = new JButton("Step");
        final JButton runButton = new JButton("Run");
        final JButton stopButton = new JButton("Stop");
        final JButton clearButton = new JButton("Clear");
        panel.add(stepButton);
        panel.add(runButton);
        panel.add(stopButton);
        panel.add(clearButton);

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object src = e.getSource();
                if (src == stopButton) {
                    lifeComp.stop();
                }
                if (src == stepButton) {
                    lifeComp.nextGeneration();
                }
                if (src == runButton) {
                    lifeComp.run();
                }
                if (src == clearButton) {
                    lifeComp.clear();
                }
            }

        };

        stopButton.addActionListener(listener);
        stepButton.addActionListener(listener);
        runButton.addActionListener(listener);
        clearButton.addActionListener(listener);

        return panel;
    }

    /**
     * @return a panel with controls for randomizing the world
     */
    private Component getSouthPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        //panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.setBorder(new TitledBorder(
                new EtchedBorder(), "Randomize Population"));

        JPanel innerPanel = new JPanel();
        JButton button = new JButton("Populate");
        innerPanel.add(button);

        final JSlider slider = new JSlider();
        final JLabel label = new JLabel("Probability of life: "
                + slider.getValue() + "%");

        panel.add(innerPanel);
        panel.add(slider);
        panel.add(label);

        slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                int x = slider.getValue();
                String msg = "Probability of life: " + x + "%";
                label.setText(msg);
            }
        });

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                double p = slider.getValue() / 100.0;
                lifeComp.randomize(p);
            }
        });

        return panel;
    }

    /**
     * @return a panel with radio buttons for controlling the speed of updates
     */
    private Component getEastPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(
                new EtchedBorder(), "Speed"));

        final JRadioButton slowButton = new JRadioButton("Slow");
        final JRadioButton mediumButton = new JRadioButton("Medium");
        final JRadioButton fastButton = new JRadioButton("Fast");
        mediumButton.setSelected(true);

        panel.setLayout(new GridLayout(3, 1));
        //panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(slowButton);
        panel.add(mediumButton);
        panel.add(fastButton);

        ButtonGroup group = new ButtonGroup();
        group.add(slowButton);
        group.add(mediumButton);
        group.add(fastButton);

        ActionListener listener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Object src = e.getSource();
                if (src == slowButton) {
                    lifeComp.setDelay(LifeComponent.SLOW);
                }
                if (src == mediumButton) {
                    lifeComp.setDelay(LifeComponent.MEDIUM);
                }
                if (src == fastButton) {
                    lifeComp.setDelay(LifeComponent.FAST);
                }
            }
        };

        slowButton.addActionListener(listener);
        mediumButton.addActionListener(listener);
        fastButton.addActionListener(listener);

        return panel;
    }

    /**
     * @return a menu with three items for initial Life configurations
     */
    private JMenu getInitConfigMenus() {
        JMenu menu = new JMenu("Initial Configuration");

        JMenuItem[] menuItems = {
            new JMenuItem("Glider"),
            new JMenuItem("Exploder"),
            new JMenuItem("Tumbler"),};

        menu.add(menuItems[0]);
        menu.add(menuItems[1]);
        menu.add(menuItems[2]);

        menuItems[0].addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                lifeComp.clear();
                int x = 2;
                int y = 2;
                lifeComp.toggle(x, y);
                lifeComp.toggle(x + 1, y + 1);
                lifeComp.toggle(x + 2, y - 1);
                lifeComp.toggle(x + 2, y);
                lifeComp.toggle(x + 2, y + 1);
            }
        });

        ActionListener listener = (new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(lifeComp, "Not supported yet.");
            }
        });
        menuItems[1].addActionListener(listener);
        menuItems[2].addActionListener(listener);

        return menu;
    }

}
