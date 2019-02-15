import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

import javax.swing.*;

import static java.lang.StrictMath.sin;

public class Program extends JFrame {

    private JLabel labelU = new JLabel("Введіть амплітуду(в вольтах): ");
    private JLabel labelT = new JLabel("Введіть період(в секундах): ");
    private JLabel labelTau = new JLabel("Введіть тау(в секундах): ");

    private JTextField textU = new JTextField(20);
    private JTextField textT = new JTextField(20);
    private JTextField textTau = new JTextField(20);

    private JButton button = new JButton("Виконати");


    JTextArea textArea = new JTextArea(20, 60);


    public Program() {
        super("АЧС послідовних прямокутних імпульсів");

        // create a new panel with GridBagLayout manager
        JPanel newPanel = new JPanel(new GridBagLayout());

        ActionListener actionListener = new TestActionListener();
        button.addActionListener(actionListener);

        textArea.setPreferredSize(new Dimension(500, 400));


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);


        // add components to the panel

        constraints.gridx = 1;
        newPanel.add(textU, constraints);
        constraints.gridx = 1;
        newPanel.add(textT, constraints);
        constraints.gridx = 1;
        newPanel.add(textTau, constraints);


        constraints.gridx = 0;
        constraints.gridy = 2;
        newPanel.add(labelTau, constraints);
        constraints.gridx = 0;
        constraints.gridy = 0;
        newPanel.add(labelU, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        newPanel.add(labelT, constraints);


        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        newPanel.add(button, constraints);


        constraints.gridx = 0;
        constraints.gridy = 4;
        newPanel.add(textArea, constraints);


        // set border for the panel
        newPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Розрахунки"));

        // add the panel to this frame
        add(newPanel);
        pack();
        setLocationRelativeTo(null);


    }

    public double u() {
        return Double.parseDouble(textU.getText());
    }

    public double t() {
        return Double.parseDouble(textT.getText());
    }

    public double tau() {
        return Double.parseDouble(textTau.getText());
    }

    public double q() {
        double q = t() / tau();
        return q;
    }


    public class TestActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            textArea.setText("");

            textArea.append("Амплітуда: " + u() + " В | Період: " + t() + " сек | Тау: " + tau() + " сек\n");
            textArea.append(("Скважність: " + t() / tau() + "\n"));

            if (t() <= tau() || q() > 50 || q() % 1 != 0) {
                textArea.append("\nНеправильно введенні данні!\nT має бути більшим ніж tau | q має бути меншим ніж 50\nСкважність має бути цілим числом.");
            } else {


                int arrayLength = (int) ((q() * 3) + 1);
                double[] f = new double[arrayLength];
                // Частоти усіх гармонік
                for (int i = 1; i < arrayLength; i++) {
                    NumberFormat nf = new DecimalFormat("#.######");
                    f[i] = 1 / t() * i;
                    textArea.append("Частота " + i + " гармоніки: " + nf.format(f[i]) + " Гц\n");
                }

                double uConst = u() / q();
                textArea.append("Ампітуда постійної складової: " + uConst + " B\n");

                double[] arrU = new double[arrayLength];
                for (int i = 1; i < arrayLength; i++) {
                    NumberFormat nf = new DecimalFormat("#.######");
                    arrU[i] = (2 * u()) / q() * Math.abs((sin(i * 3.14 / q())) / (i * 3.14 / q()));
                    if (i % q() == 0) arrU[i] = 0;
                    textArea.append("Амплітуда " + i + " гармоніки: " + nf.format(arrU[i]) + " В\n");
                }


                XYSeries seriesGraphics = new XYSeries(" АЧС ");

                double[] shapes = new double[ ((int) ((q() * 3)) + 1)];



                for (float i = 1; i <= 3 * q() + 0.1; i += 0.25) {


                    if (i % 1 == 0) {
                        shapes[(int) i] = (2 * u()) / q() * Math.abs((sin(i * 3.14 / q())) / (i * 3.14 / q()));
                        seriesGraphics.add(i, (2 * u()) / q() * Math.abs((sin(i * 3.14 / q())) / (i * 3.14 / q())));
                    } else seriesGraphics.add(i, (2 * u()) / q() * Math.abs((sin(i * 3.14 / q())) / (i * 3.14 / q())));


                }

                for (int i = 0; i <= (q() * 3) + 1; i= (int) (i+q())) {

                    shapes[(i)] = 0;
                }


                XYSeriesCollection xyDataset = new XYSeriesCollection(seriesGraphics);


                XYSeries seriesHarmonic = new XYSeries("");

                seriesHarmonic.add(0, uConst);
                seriesHarmonic.add(0,0);
                seriesHarmonic.add(0.01,0);
                seriesHarmonic.add(0.01, uConst);
                seriesHarmonic.add(0.01,0);
                seriesHarmonic.add(0.02,0);
                seriesHarmonic.add(0.02, uConst);
                seriesHarmonic.add(0.02,0);
                seriesHarmonic.add(0.03,0);
                seriesHarmonic.add(0.03, uConst);
                seriesHarmonic.add(0.03,0);
                seriesHarmonic.add(0.04,0);
                seriesHarmonic.add(0.04, uConst);
                seriesHarmonic.add(0.04,0);

                for (int i = 0; i < (q() * 3) + 1; i++) {
                    seriesHarmonic.add(i,0);
                    seriesHarmonic.add(i,shapes[i]);
                    seriesHarmonic.add(i,0);
                }


                xyDataset.addSeries(seriesHarmonic);




                JFreeChart chart = ChartFactory
                        .createXYLineChart("Графік АЧС послідовних прямокутних імпульсів", "n", "U",
                                xyDataset,
                                PlotOrientation.VERTICAL,
                                true, true, true);


                JFrame frame = new JFrame("Графік АЧС послідовних прямокутних імпульсів");
                // Помещаем график на фрейм
                frame.getContentPane()
                        .add(new ChartPanel(chart));
                frame.setSize(1280, 720);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);


                XYPlot plot = (XYPlot) chart.getPlot();
                plot.getRangeAxis().setAutoRange(false);


                XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
                renderer.setSeriesStroke(
                        0, new BasicStroke(
                                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                                1.0f, new float[]{2.0f, 10.0f}, 0.0f
                        )
                );

                renderer.setPaint(Color.red);


                for (int i = 0; i < (q() * 3) + 1; i++) {
                    NumberFormat nf = new DecimalFormat("#.###");
                    XYTextAnnotation textAnnotation = new XYTextAnnotation(String.valueOf(nf.format(shapes[i])), i, shapes[i]);
                    plot.addAnnotation(textAnnotation);
                }


                plot.setRenderer(renderer);



            }
        }
    }


    public static void main(String[] args) {
        // set look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Program frame = new Program();
                frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

            }
        });


    }
}