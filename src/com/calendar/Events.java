package com.calendar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 *setting up events Jdialog
 */
class Events extends JDialog {
    private JCheckBox reminder;
    private JToggleButton ampm;
    private Integer selectedHour = 0;
    private Integer selectedMinute = 0;
    private boolean isAm = true;

    /**
     * @param dayData information of day
     * @param eventId id of each event which is defined by
     */
    Events(final DayData dayData, final int eventId) {
        super(new Frame(), "رویداد", true);

        setIconImage(Utils.getApplicationIcon());

        setSize(new Dimension(200, 260));

        final com.calendar.Event eventData;
        if (dayData.events == null) dayData.events = new ArrayList<com.calendar.Event>();
        if (eventId == -1)
            eventData = new com.calendar.Event();
        else
            eventData = dayData.events.get(eventId);

        final Container contentPane = getContentPane(); // set container
        final SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        Font font = Utils.font;

        final JTextField title = new JTextField("عنوان");
        title.setPreferredSize(new Dimension(getWidth(), 30));
        title.setFont(font);
        title.setMargin(new Insets(5, 5, 5, 5));
        title.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        title.addFocusListener(new FocusAdapter() {
            /**
             * Invoked when a component gains the keyboard focus.
             *
             * @param e Event
             */
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (title.getText().equals("عنوان"))
                    title.setText("");
            }

            /**
             * Invoked when a component loses the keyboard focus.
             *
             * @param e Event
             */
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (title.getText().equals(""))
                    title.setText("عنوان");
            }
        });

        final JTextArea event = new JTextArea("متن");
        event.setPreferredSize(new Dimension(getWidth(), 140));
        event.setFont(font);
        event.setMargin(new Insets(5, 5, 5, 5));
        event.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        event.addFocusListener(new FocusAdapter() {
            /**
             * Invoked when a component gains the keyboard focus.
             *
             * @param e Event
             */
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (event.getText().equals("متن"))
                    event.setText("");
            }

            /**
             * Invoked when a component loses the keyboard focus.
             *
             * @param e Event
             */
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (event.getText().equals(""))
                    event.setText("متن");
            }
        });

        reminder = new JCheckBox("بهم یادآوری کن");  // set reminder checkbox
        reminder.setPreferredSize(new Dimension(getWidth(), 30));
        reminder.setBorder(new EmptyBorder(0,0,0,0));
        reminder.setFont(font);
        reminder.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        final int hoursRadius = 50;  //set radius for hour circle
        final int minutesRadius = 80; ////set radius for hour circle
        final int clockWidth = getWidth();
        final int clockHeight = 200;


        SpringLayout clockLayout = new SpringLayout(); //designing clock
        final JPanel clock = new JPanel(clockLayout);
        clock.setBorder(null);
        clock.setSize(clockWidth, clockHeight);
        clock.setPreferredSize(new Dimension(clockWidth, clockHeight));

        final JLabel hourHand = new JLabel(Utils.makeClockHand(clockWidth, clockHeight, 50, true, ""));
        hourHand.setBorder(new EmptyBorder(0, 0, 0, 0));
        hourHand.setPreferredSize(new Dimension(clockWidth, clockHeight));
        clockLayout.putConstraint(SpringLayout.NORTH, hourHand, 0, SpringLayout.NORTH, clock);
        clockLayout.putConstraint(SpringLayout.WEST, hourHand, 0, SpringLayout.WEST, clock);

        final JLabel minuteHand = new JLabel(Utils.makeClockHand(clockWidth, clockHeight, 80, false, ""));
        minuteHand.setBorder(new EmptyBorder(0, 0, 0, 0));
        minuteHand.setPreferredSize(new Dimension(clockWidth, clockHeight));
        clockLayout.putConstraint(SpringLayout.NORTH, minuteHand, 0, SpringLayout.NORTH, clock);
        clockLayout.putConstraint(SpringLayout.WEST, minuteHand, 0, SpringLayout.WEST, clock);

        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JLabel source = (JLabel) e.getSource();
                int radius = (int) Math.ceil(Math.sqrt(
                        Math.pow((clockWidth / 2 - (source.getBounds().getX() + 5)), 2) +
                                Math.pow((clockHeight / 2 - (source.getBounds().getY() + 5)), 2)
                ));

                int value = Integer.valueOf(Utils.fa2en(source.getText()));
                if (radius == hoursRadius) {
                    setSelectedHour(value);
                } else {
                    setSelectedMinute(value);
                }

                updateTime();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                JLabel source = (JLabel) e.getSource();

                // r^2 = a^2 + b^2
                int radius = (int) Math.ceil(Math.sqrt(
                        Math.pow((clockWidth / 2 - (source.getBounds().getX() + 5)), 2) +
                                Math.pow((clockHeight / 2 - (source.getBounds().getY() + 5)), 2)
                ));

                if (radius == hoursRadius) {
                    hourHand.setIcon(Utils.makeClockHand(clockWidth, clockHeight, radius - 10, true, source.getText()));
                } else {
                    minuteHand.setIcon(Utils.makeClockHand(clockWidth, clockHeight, radius - 20, false, source.getText()));
                }

                clock.repaint();
            } //to find which of the clock hands user is using

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };


        int centerX = clock.getWidth() / 2 - 5;
        int centerY = clock.getHeight() / 2 - 5;

        for (int i = 1; i <= 12; i++) {
            JLabel label = new JLabel(Utils.en2fa(String.valueOf(i)));
            label.setFont(Utils.font);
            label.addMouseListener(mouseListener);
            int x = (int) (hoursRadius * Math.cos(Math.toRadians((double) (i * 30 - 90))));
            int y = (int) (hoursRadius * Math.sin(Math.toRadians((double) (i * 30 - 90))));
            label.setPreferredSize(new Dimension(20, 20));
            clock.add(label);
            clockLayout.putConstraint(SpringLayout.NORTH, label, centerY + y, SpringLayout.NORTH, clock);
            clockLayout.putConstraint(SpringLayout.WEST, label, centerX + x, SpringLayout.WEST, clock);
        }
        //define each hour place in the clock

        for (int i = 0; i < 60; i += 5) {
            JLabel label = new JLabel(Utils.en2fa(String.valueOf(i)));
            label.setFont(Utils.font);
            label.addMouseListener(mouseListener);
            int x = (int) (minutesRadius * Math.cos(Math.toRadians((double) (i * 6 - 90))));
            int y = (int) (minutesRadius * Math.sin(Math.toRadians((double) (i * 6 - 90))));
            label.setPreferredSize(new Dimension(20, 20));
            clock.add(label);
            clockLayout.putConstraint(SpringLayout.NORTH, label, centerY + y, SpringLayout.NORTH, clock);
            clockLayout.putConstraint(SpringLayout.WEST, label, centerX + x, SpringLayout.WEST, clock);
        }
        //define each 5 minuts place in the clock

        clock.add(hourHand);
        clock.add(minuteHand);

        clock.setBackground(new Color(221, 236, 233));

        clock.repaint();
        clock.setVisible(false);

        ampm = new JToggleButton(new ImageIcon(Utils.getFileAddress("pm.png"))); //when am is not selected
        ampm.setSelectedIcon(new ImageIcon(Utils.getFileAddress("am.png")));
        ampm.setPreferredSize(new Dimension(getWidth(), 30));
        ampm.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        ampm.setBorder(null);
        ampm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isAm = !isAm;
                setAmPm();
                updateTime();
            }
        });
        ampm.setVisible(false);

        final JButton save = new JButton("ذخیره");
        save.setPreferredSize(new Dimension(getWidth(), 30));
        save.setFont(font);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String eventTitle = title.getText();
                String eventText = event.getText();
                if (eventTitle.equals("عنوان")) eventTitle = "";
                if (eventText.equals("متن")) eventText = "";

                if (eventTitle.isEmpty() && eventText.isEmpty())
                {
                    // Do not save if neither title nor text is set
                    dispose();
                    return;
                }

                String eventReminder = "";

                if (reminder.isSelected()) {
                    Integer hour = selectedHour;
                    if (!isAm) hour += 12;
                    if (hour == 24) hour = 0;
                    eventReminder = hour.toString() + ":" + selectedMinute.toString();
                }

                eventData.title = eventTitle;
                eventData.text = eventText;
                eventData.reminder = eventReminder;

                if (eventId == -1) {
                    dayData.events.add(eventData);
                }
                Utils.save();
                dispose();
            }
        });

        final JSeparator separator1 = new JSeparator(); //draws a line
        separator1.setPreferredSize(new Dimension(getWidth(), 5));
        separator1.setVisible(false);

        final JSeparator separator2 = new JSeparator();
        separator2.setPreferredSize(new Dimension(getWidth(), 5));
        separator2.setVisible(false);

        contentPane.add(title);
        contentPane.add(event);
        contentPane.add(reminder);
        contentPane.add(separator1);
        contentPane.add(clock);
        contentPane.add(ampm);
        contentPane.add(separator2);
        contentPane.add(save);

        layout.putConstraint(SpringLayout.NORTH, event, 0, SpringLayout.SOUTH, title);
        layout.putConstraint(SpringLayout.NORTH, reminder, 0, SpringLayout.SOUTH, event);
        layout.putConstraint(SpringLayout.NORTH, separator1, 0, SpringLayout.SOUTH, reminder);
        layout.putConstraint(SpringLayout.NORTH, clock, 0, SpringLayout.SOUTH, separator1);
        layout.putConstraint(SpringLayout.NORTH, ampm, 0, SpringLayout.SOUTH, clock);
        layout.putConstraint(SpringLayout.NORTH, separator2, 0, SpringLayout.SOUTH, ampm);
        layout.putConstraint(SpringLayout.NORTH, save, 0, SpringLayout.SOUTH, reminder);
        //arrange springLayout

        reminder.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                boolean visibility = reminder.isSelected();
                clock.setVisible(visibility);
                ampm.setVisible(visibility);
                separator1.setVisible(visibility);
                separator2.setVisible(visibility);
                if (!visibility) {
                    layout.putConstraint(SpringLayout.NORTH, save, 0, SpringLayout.SOUTH, reminder);
                    setSize(getWidth(), 260);
                    reminder.setText("بهم یادآوری کن");
                } else {
                    layout.putConstraint(SpringLayout.NORTH, save, 0, SpringLayout.SOUTH, separator2);
                    setSize(getWidth(), 490);
                }
            }
        });

        setLocationRelativeTo(null);

        String loadedReminder = eventData.reminder;
        if (!loadedReminder.isEmpty())
        {
            String[] strings = loadedReminder.split(":");
            selectedHour = Integer.valueOf(strings[0]);
            selectedMinute = Integer.valueOf(strings[1]);

            if (selectedHour == 0 || selectedHour > 12) {
                if (selectedHour != 0) selectedHour -= 12;
                isAm = false;
                setAmPm();
            }

            reminder.setSelected(true);
            updateTime();
            hourHand.setIcon(Utils.makeClockHand(clockWidth, clockHeight, hoursRadius - 10, true, selectedHour.toString()));
            minuteHand.setIcon(Utils.makeClockHand(clockWidth, clockHeight, minutesRadius - 20, false, selectedMinute.toString()));
        }

        if (!eventData.title.isEmpty())
            title.setText(eventData.title);

        if (!eventData.text.isEmpty())
            event.setText(eventData.text);

        setAmPm();
        reminder.requestFocus();

        setResizable(false);

        setVisible(true);
    }

    /**
     * choose the hour number
     * @param selectedHour selected hour
     */
    private void setSelectedHour(int selectedHour) {
        this.selectedHour = selectedHour;
    }

    /**
     * choose the minute number
     * @param selectedMinute selected minute
     */
    private void setSelectedMinute(int selectedMinute) {
        this.selectedMinute = selectedMinute;
    }

    /**
     * change toggle button
     */
    private void setAmPm() {
        ampm.setSelected(isAm);
    }

    /**
     * set the state of hour to be pm or am
     */
    private void updateTime() {
        Integer hour = this.selectedHour;
        if (!this.isAm) hour += 12;
        if (hour == 24) hour = 0;

        reminder.setText("ساعت " + Utils.en2fa(hour.toString()) + ":" + Utils.en2fa(this.selectedMinute.toString()) + " بهم یادآوری کن");
    }
}
