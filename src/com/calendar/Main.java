package com.calendar;

import com.ibm.icu.util.ULocale;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.*;


public class Main {

    /**
     * declare timers for reminders
     */
    private static ArrayList<Timer> timers = new ArrayList<Timer>();
    /**
     * declare a calendar
     */
    private static com.calendar.Calendar calendar;
    /**
     * declare persian calendar
     */
    private static com.ibm.icu.util.Calendar persianCalendar = com.ibm.icu.util.Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
    /**
     * declare trayIcon
     */
    private static TrayIcon trayIcon;

    /**
     * declare array to get index of each month
     */
    private static Month[] months = Utils.months;
    /**
     * declare a daydata object
     */
    private static DayData dayData;


    /**
     * setting up reminders of today by calculating the difference of event reminder time from now
     */
    static void setReminders() {
        for (Timer oldTimer : timers) {
            oldTimer.cancel();
        }
        timers.clear();

        for (final com.calendar.Event event : dayData.events) {
            if (event.reminder != null && event.reminder.length() > 0) {
                String[] strings = event.reminder.split(":");
                int selectedHour = Integer.valueOf(strings[0]);
                int selectedMinute = Integer.valueOf(strings[1]);

                int seconds = Utils.getSecondsUntil(persianCalendar, selectedHour, selectedMinute);
                persianCalendar.setTime(new Date()); // Update current time
                if (seconds > -1) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            trayIcon.displayMessage(event.title, event.text, TrayIcon.MessageType.INFO);
                            Toolkit.getDefaultToolkit().beep();
                        }
                    }, seconds * 1000);
                    timers.add(timer);
                }
            }
        }
    }

    /**
     * handle the change of date when tomorrow comes
     */
    private static class HandleDayChanges extends TimerTask {
        /**
         * The action to be performed by this timer task.
         */
        @Override
        public void run() {
            new Timer().schedule(new HandleDayChanges(), 86400 * 1000); //set this method for the next day as well

            persianCalendar.setTime(new Date());
            int month = persianCalendar.get(java.util.Calendar.MONTH);
            int day = persianCalendar.get(java.util.Calendar.DATE);


            trayIcon.setImage(Utils.makeIcon(day));

            calendar.onDayChange(months[month].days[day-1]);

            setToolTip();
            setReminders();
        }
    }


    /**
     * runs the calendar
     * @param args input arguments
     */
    public static void main(String[] args) {
        int secondsUntilTomorrow = Utils.getSecondsUntilTomorrow(persianCalendar);
        new Timer().schedule(new HandleDayChanges(), secondsUntilTomorrow * 1000);

        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, Utils.getFileAddress("IRAN Sans.ttf").openStream());
            font = font.deriveFont(11.0f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font); // Register font to be used in HTMLs
        } catch (Exception e) {
            font = new Font("Tahoma", Font.PLAIN, 11);
        }
        Utils.font = font;

        persianCalendar.setTime(new Date());

        dayData = months[persianCalendar.get(java.util.Calendar.MONTH)].days[persianCalendar.get(java.util.Calendar.DATE) - 1];
        if (dayData.events == null) {
            dayData.events = new ArrayList<com.calendar.Event>();
            Utils.save();
        }
        if (dayData.notes == null) {
            dayData.notes = new ArrayList<Note>();
            Utils.save();
        }  //setting daydata information of the day

        calendar = new com.calendar.Calendar(months, dayData);


        trayIcon =  new TrayIcon(Utils.makeIcon(persianCalendar.get(java.util.Calendar.DATE))); //setting trayIcon
        trayIcon.setImageAutoSize(true);
        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
            System.exit(1);
        }

        trayIcon.setPopupMenu(createPopupMenu(calendar, font)); //setting trayIcon' popup menu
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                if(mouseEvent.getButton() == MouseEvent.BUTTON1 && mouseEvent.getClickCount() == 2){
                    calendar.setVisible(true);
                }
            }
        });
        
        setToolTip();

        setReminders();

    }

    /**
     * @param calendar the day
     * @param font default font
     * @return popup for the trayIcon
     */
    private static PopupMenu createPopupMenu(final com.calendar.Calendar calendar, Font font) {
        final PopupMenu trayPopup = new PopupMenu();
        trayPopup.setFont(font);

        MenuItem showWindowMenu = new MenuItem("نمایش تقویم");
        showWindowMenu.setFont(font);
        showWindowMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calendar.setVisible(true);
            }
        });

        MenuItem newEventMenu = new MenuItem("رویداد جدید");
        newEventMenu.setFont(font);
        newEventMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Events(dayData, -1);
                setReminders();
            }
        });

        MenuItem newNoteMenu = new MenuItem("یادداشت جدید");
        newNoteMenu.setFont(font);
        newNoteMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Notes(dayData, -1);
            }
        });

        MenuItem editNoteAndEventMenu = new MenuItem("ویرایش متن و رویداد");
        editNoteAndEventMenu.setFont(font);
        editNoteAndEventMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditEventsAndNotes(dayData);
                setReminders();
            }
        });

        MenuItem copyDateMenu = new MenuItem("ک‍پی تاریخ روز");
        copyDateMenu.setFont(font);
        copyDateMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utils.copyDayToClipboard(dayData);
            }
        });

        MenuItem copyDayEventMenu = new MenuItem("ک‍پی مناسبت روز");
        copyDayEventMenu.setFont(font);
        copyDayEventMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(dayData.dayEvent);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            }
        });

        MenuItem exitMenu = new MenuItem("خروج");
        exitMenu.setFont(font);
        exitMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        trayPopup.add(showWindowMenu);
        trayPopup.addSeparator();
        trayPopup.add(newEventMenu);
        trayPopup.add(newNoteMenu);
        trayPopup.addSeparator();
        trayPopup.add(editNoteAndEventMenu);
        trayPopup.add(copyDateMenu);
        trayPopup.add(copyDayEventMenu);
        trayPopup.addSeparator();
        trayPopup.add(exitMenu);

        return trayPopup;
    }

    /**
     * set trayIcon tooltip
     */
    private static void setToolTip(){
        String dayToolTip = dayData.getToolTipText();
        if(dayToolTip != null){
            trayIcon.setToolTip(dayToolTip);
        }

    }
}
