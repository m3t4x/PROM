package GUITools;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Date;

public class GUITools {
    public static JLabel CreateImage(String path, int width, int height)
    {
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JLabel tempLabel = new JLabel(new ImageIcon(myPicture));
        tempLabel.setBounds(0, 0, width, height);
        Image dimg = myPicture.getScaledInstance(tempLabel.getWidth(),  tempLabel.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(dimg);
        return new JLabel(imageIcon);
    }
    public static class TimeInputWidget extends JPanel {
        private JSpinner hourSpinner;
        private JSpinner minuteSpinner;

        public TimeInputWidget() {
            setLayout(new FlowLayout());
            setBackground(Color.white);
            // Create SpinnerNumberModel for hour and minute spinners
            SpinnerNumberModel hourModel = new SpinnerNumberModel(0, 0, 23, 1);
            SpinnerNumberModel minuteModel = new SpinnerNumberModel(0, 0, 45, 15);

            // Create hour spinner
            hourSpinner = new JSpinner(hourModel);
            hourSpinner.setPreferredSize(new Dimension(60, 20));

            // Create minute spinner
            minuteSpinner = new JSpinner(minuteModel);
            minuteSpinner.setPreferredSize(new Dimension(60, 20));

            // Create labels
            JLabel hourLabel = new JLabel("Heures:");
            JLabel minuteLabel = new JLabel("Minutes:");

            // Add labels and spinners to the panel
            add(hourLabel);
            add(hourSpinner);
            add(minuteLabel);
            add(minuteSpinner);
        }

        public int getSelectedHour() {
            return (int) hourSpinner.getValue();
        }

        public int getSelectedMinute() {
            return (int) minuteSpinner.getValue();
        }

        public void main(String[] args) {
            JFrame frame = new JFrame("Time Input Widget");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new TimeInputWidget());
            frame.pack();
            frame.setVisible(true);
        }
    }

    public static class DateSelector extends JPanel {
        private JSpinner dateSpinner;

        public DateSelector() {
            setLayout(new FlowLayout());
            setBackground(Color.white);

            // Create SpinnerDateModel for the date spinner
            SpinnerDateModel dateModel = new SpinnerDateModel();
            dateSpinner = new JSpinner(dateModel);

            // Configure the spinner to display the date in a desired format
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
            dateSpinner.setEditor(dateEditor);

            // Add the spinner to the panel
            add(dateSpinner);
        }

        public Date getSelectedDate() {
            // Get the selected date from the spinner
            java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();

            // Convert the selected date to java.sql.Date
            return new Date(selectedDate.getTime());
        }
    }


}
