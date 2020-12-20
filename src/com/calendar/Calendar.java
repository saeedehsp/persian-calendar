package com.calendar;

import com.ibm.icu.util.ULocale;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;


/**
 * this class shows the main frame of the calendar
 */
class Calendar extends JFrame {
    /**
     * saves today's datas
     */
    private DayData currentDayData; // TODO: Add "Go To Today" using this
    /**
     * saves selected day datas
     */
    private DayData selectedDayData;
    /**
     * index of the selected month
     */
    private Integer currentMonth;
    /**
     * contains the calendar table
     */
    private JPanel bodyCenter;
    /**
     * background image
     */
    private Image img;
    /**
     * main menu
     */
    private Menus menus;
    /**
     * selected month name
     */
    private JLabel nameOfMonth;
    /**
     * contains status of selected day
     */
    private JLabel statusText;
    /**
     * months' information
     */
    private Month[] months;
    /**
     * selected day by user
     */
    private Day selectedDay = null;
    /**
     * today's calendar cell
     */
    private Day today = null;
    /**
     * local info of persian calendar
     */
    private ULocale faLocate = new ULocale("fa_IR@calendar=persian");
    /**
     * local info of Arabic calendar
     */
    private ULocale arLocate = new ULocale("ar_SA@calendar=islamic");
    /**
     * local info of english US
     */
    private ULocale enLocate = new ULocale("en_US");

    /**
     * @param months the arrayList of months' informations
     * @param currentDayData data of today
     * Makes the main frame in the begining
     * Set the background
     * initialize private fields for calendar
     * Set the UI of the main frame of calendar
     */
    Calendar(Month[] months, final DayData currentDayData){
        super("تقویم");

        setSize(600, 620);

        setIconImage(Utils.getApplicationIcon());

        nameOfMonth = new JLabel(currentDayData.monthName);

        this.months = months;
        this.currentDayData = currentDayData;
        this.selectedDayData = currentDayData;
        this.currentMonth = currentDayData.month - 1;

        Font font = Utils.font;

        menus = new Menus(this, selectedDayData);

        this.setFont(font);
        img = new ImageIcon(Utils.getFileAddress("bc.jpg")).getImage();
        Image scaledImage = img.getScaledInstance(this.getWidth(),-1, Image.SCALE_SMOOTH);
        setContentPane(new JLabel(new ImageIcon(scaledImage)));

        Container contentPane = getContentPane(); // set container
        setLayout(new BorderLayout(100,20));


        JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(getWidth(), 70));
        header.setLayout(new BorderLayout());
        header.setOpaque(false);


        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setPreferredSize(new Dimension(getWidth(), 520));

        JPanel bodyEast = new JPanel();
        bodyEast.setOpaque(false);
        bodyEast.setLayout( new BorderLayout() );

        JPanel bodyWest = new JPanel();
        bodyWest.setOpaque(false);
        bodyWest.setLayout( new BorderLayout() );


        this.bodyCenter = new JPanel();
        bodyCenter.setOpaque(false);
        bodyCenter.setLayout(new GridLayout(0, 7, 1, 1));
        bodyCenter.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        String[] daysOfWeek = new String[]{                  // Declare table infos
                "شنبه",
                "یکشنبه",
                "دوشنبه",
                "سه‌شنبه",
                "چهارشنبه",
                "پنج‌شنبه",
                "جمعه"
        };
        for (String dayOfweek : daysOfWeek){                //day names Row
            JPanel jPanel = new JPanel(new BorderLayout());
            jPanel.setOpaque(false);
            jPanel.setPreferredSize(new Dimension(60,60));
            JLabel dayLabel = new JLabel(dayOfweek);
            dayLabel.setFont(font);
            dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
            jPanel.add(dayLabel);
            bodyCenter.add(jPanel);
        }

        body.add(bodyEast , BorderLayout.LINE_START);      //add body's component for aligning the calendar table
        body.add(bodyCenter , BorderLayout.CENTER);
        body.add(bodyWest , BorderLayout.LINE_END);



        JPanel footer = new JPanel(new BorderLayout());    //Bottom of the frame which contains statusBar
        footer.setPreferredSize(new Dimension(getWidth(), 30));
        footer.setBorder(new BevelBorder(BevelBorder.LOWERED));


        JButton todayButton = new JButton("برو به امروز");  //Declare today's button for easier acces to today's info
        todayButton.setPreferredSize(new Dimension(100, 30));
        todayButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        todayButton.setVerticalAlignment(SwingConstants.CENTER);
        todayButton.setFont(font);
        todayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDayData = currentDayData;
                menus.setDay(selectedDayData);
                currentMonth = selectedDayData.getMonth()-1;
                setCalender();
            }
        });

        statusText = new JLabel();  //Declare statusBar showing the status of the day
        statusText.setPreferredSize(new Dimension(getWidth()-100, 30));
        statusText.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        statusText.setVerticalAlignment(SwingConstants.CENTER);
        statusText.setFont(font);

        footer.add(statusText , BorderLayout.EAST);
        footer.add(todayButton, BorderLayout.WEST);



        final JButton previousMonth =  new JButton("ماه قبل");  //Set access to previous month
        previousMonth.setFont(font);
        previousMonth.setPreferredSize(new Dimension(70, 70));
        previousMonth.setToolTipText("نمایش ماه قبل");
        if (currentMonth == 0) {
            previousMonth.setEnabled(false);
        }

        final JButton nextMonth =  new JButton("ماه بعد");  //Set access to next month
        nextMonth.setFont(font);
        nextMonth.setPreferredSize(new Dimension(70, 70));
        nextMonth.setToolTipText("نمایش ماه بعد");
        if (currentMonth == 11) {
            nextMonth.setEnabled(false);
        }


        previousMonth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentMonth--;
                setCalender();

                nextMonth.setEnabled(true);
                if (currentMonth == 0) {
                    previousMonth.setEnabled(false);
                }
            }
        });

        nextMonth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentMonth++;
                setCalender();

                previousMonth.setEnabled(true);
                if (currentMonth == 11) {
                    nextMonth.setEnabled(false);
                }
            }
        });


        JButton newEvent = new JButton("رویداد جدید");  //Set button for new events
        newEvent.setFont(font);
        newEvent.setToolTipText("ایجاد رویداد جدید");
        newEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Events(selectedDayData, -1);
                Main.setReminders();
                setCalender();
            }
        });

        JButton newNote = new JButton("یادداشت جدید");  //set button for new notes
        newNote.setFont(font);
        newNote.setToolTipText("ایجاد یادداشت جدید");
        newNote.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedDay != null) {
                    new Notes(selectedDayData, -1);
                    setCalender();
                }
            }
        });

        JButton editEventAndNotes = new JButton("ویرایش رویدادها و یادداشتها");  //set button for editing notes and events
        editEventAndNotes.setFont(font);
        editEventAndNotes.setToolTipText("ویرایش رویداد ها و یادداشت های روز انتخاب شده");
        editEventAndNotes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditEventsAndNotes(selectedDayData);
                setCalender();
                Main.setReminders();
            }
        });


        JToolBar toolBar = new JToolBar();  //Declare toolbar
        JPanel north = new JPanel();
        north.setLayout(new GridLayout(1, 3));

        toolBar.setLayout(new BorderLayout());

        toolBar.add(previousMonth , BorderLayout.AFTER_LINE_ENDS);  //Arrange toolbar components
        nameOfMonth.setHorizontalAlignment(SwingConstants.CENTER);
        nameOfMonth.setFont(font);
        toolBar.add(nameOfMonth , BorderLayout.CENTER);
        toolBar.add(nextMonth , BorderLayout.BEFORE_LINE_BEGINS);
        toolBar.add(north , BorderLayout.NORTH);
        north.add(newEvent);
        north.add(newNote);
        north.add(editEventAndNotes);
        toolBar.setPreferredSize(new Dimension(getWidth() , 70));
        header.add(toolBar, BorderLayout.CENTER);

        contentPane.add(header , BorderLayout.NORTH);  //Arrange contentpane
        contentPane.add(body , BorderLayout.CENTER);
        contentPane.add(footer , BorderLayout.SOUTH);

        setJMenuBar(menus.makeMenuBar());


        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                Image scaledImage = img.getScaledInstance(getWidth(), -1, Image.SCALE_SMOOTH);
                ((JLabel) getContentPane()).setIcon(new ImageIcon(scaledImage));
                if (getHeight() < 620) setSize(getWidth(), 620);
                if (getWidth() < 600) setSize(600, getHeight());
            } //Manage resizing the frame

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

        setCalender();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * selects given day in calendar
     * @param day a day to select
     *
     */
    void selectDay(Day day) {
        if (selectedDay != null) {
            selectedDay.deselect();
            bodyCenter.updateUI();
        }
        selectedDay = day;
        selectedDayData = selectedDay.getDayData();

        String dayStatusText = Utils.correctEncoding(selectedDayData.dayEvent);
        if (dayStatusText.length() > 0)
            dayStatusText += " - ";
        dayStatusText += String.valueOf(selectedDayData.events.size()) + " رویداد ";  //state the number of events in the selected day
        dayStatusText += String.valueOf(selectedDayData.notes.size()) + " یادداشت ";  //state the number of notes in the selected day

        statusText.setText(dayStatusText);
        statusText.setForeground(Color.black);
        if (selectedDayData.isHoliday) {
            statusText.setForeground(Color.red);
        }
        menus.setDay(selectedDayData);
    }

    /**
     * set the calemndar for selected month and shows it in the frame
     */
    void setCalender() {

        while (bodyCenter.getComponentCount() > 7)
            bodyCenter.remove(7);  //erase all the cells of the table except row of the week's days

        DayData[] days = months[currentMonth].days;

        nameOfMonth.setText(Utils.correctEncoding(months[currentMonth].name));


        com.ibm.icu.util.Calendar gregorianCalendar = com.ibm.icu.util.Calendar.getInstance(enLocate);
        com.ibm.icu.util.Calendar persianCalendar = com.ibm.icu.util.Calendar.getInstance(faLocate);
        com.ibm.icu.util.Calendar islamicCalendar = com.ibm.icu.util.Calendar.getInstance(arLocate);

        persianCalendar.set(1395, this.currentMonth, 1); // set persian calender
        gregorianCalendar.setTime(persianCalendar.getTime());
        islamicCalendar.setTime(persianCalendar.getTime());


        int emptyDays = persianCalendar.get(java.util.Calendar.DAY_OF_WEEK) % 7;

        for (int j=0 ; j<emptyDays ; j++){  //empty not needed cells before start of the month in first week of every month
            JPanel emptyDay = new JPanel(new BorderLayout());
            emptyDay.setOpaque(false);
            emptyDay.setPreferredSize(new Dimension(60,60));
            this.bodyCenter.add(emptyDay);
        }


        for (DayData day : days) {  //makes the days of every months and declare the current day and selected day
            Day theDay = new Day(
                    this,
                    persianCalendar.get(java.util.Calendar.DATE),
                    gregorianCalendar.get(java.util.Calendar.DATE),
                    islamicCalendar.get(java.util.Calendar.DATE),
                    day
            );

            if ((persianCalendar.get(java.util.Calendar.MONTH) == currentDayData.getMonth() - 1) && (persianCalendar.get(java.util.Calendar.DATE) == currentDayData.getDay())){
                theDay.isToday(true);
                today = theDay;
            }


            if ((persianCalendar.get(java.util.Calendar.MONTH) == selectedDayData.getMonth() - 1) && (persianCalendar.get(java.util.Calendar.DATE) == selectedDayData.getDay())){
                theDay.select();
                selectedDay = theDay;
            }

            this.bodyCenter.add(theDay);
            persianCalendar.add(java.util.Calendar.DATE, 1);
            gregorianCalendar.add(java.util.Calendar.DATE, 1);
            islamicCalendar.add(java.util.Calendar.DATE, 1);
        }

        bodyCenter.updateUI();
    }

    /**
     * @param dayData information of day
     * handeling day change to tomorrow
     */
    void onDayChange(DayData dayData) {
        today.isToday(false);
        this.currentMonth = dayData.getMonth() - 1;
        this.currentDayData = dayData;
        this.selectedDayData = dayData;
        menus.setDay(dayData);

        setCalender();
    } //
}
