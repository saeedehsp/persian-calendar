package com.calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

/**
 * setting up menus of the calendar components
 */
class Menus {
    private DayData day;
    private Font font;
    private Calendar calendar;

    /**
     * @param calendar selected day
     * @param day get information in case of editing and moreover
     */
    Menus(Calendar calendar, DayData day) {
        this.calendar = calendar;
        this.day = day;
        this.font = Utils.font;
    }

    /**
     * @param day setting the day in case of change in selected day for setting up its menu
     */
    void setDay(DayData day) {
        this.day = day;
    }

    /**
     * menu for creating new event
     * @return menuItem
     */
    private JMenuItem newEventMenu() {
        JMenuItem menuItem = new JMenuItem("رویداد جدید");

        menuItem.setFont(font);
        menuItem.setAccelerator(KeyStroke.getKeyStroke('e', InputEvent.CTRL_DOWN_MASK));
        menuItem.setMnemonic('ر');
        menuItem.setDisplayedMnemonicIndex(0);
        menuItem.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Events(day, -1);
                Main.setReminders();
                if (calendar != null) calendar.setCalender();
            }
        });

        return menuItem;
    }

    /**
     * menu for creating new note
     * @return menuItem
     */
    private JMenuItem newNoteMenu() {
        JMenuItem menuItem = new JMenuItem("یادداشت جدید");

        menuItem.setFont(font);
        menuItem.setAccelerator(KeyStroke.getKeyStroke('n', InputEvent.CTRL_DOWN_MASK));
        menuItem.setMnemonic('ی');
        menuItem.setDisplayedMnemonicIndex(0);
        menuItem.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Notes(day, -1);
                if (calendar != null) calendar.setCalender();
            }
        });

        return menuItem;
    }

    /**
     * menu for editing events and notes saved
     * @return menuItem
     */
    private JMenuItem editNoteAndEventMenu() {
        JMenuItem menuItem = new JMenuItem("ویرایش یادداشت ها و رویدادها");

        menuItem.setFont(font);
        menuItem.setAccelerator(KeyStroke.getKeyStroke('E', InputEvent.CTRL_DOWN_MASK));
        menuItem.setMnemonic('و');
        menuItem.setDisplayedMnemonicIndex(0);
        menuItem.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditEventsAndNotes(day);
                Main.setReminders();
                if (calendar != null) calendar.setCalender();
            }
        });

        return menuItem;
    }

    /**
     * menu for copying date of the day
     * @return menuItem
     */
    private JMenuItem copyDateMenu() {
        JMenuItem menuItem = new JMenuItem("ک‍پی تاریخ روز");

        menuItem.setFont(font);
        menuItem.setAccelerator(KeyStroke.getKeyStroke('c', InputEvent.CTRL_DOWN_MASK));
        menuItem.setMnemonic('ت');
        menuItem.setDisplayedMnemonicIndex(5);
        menuItem.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utils.copyDayToClipboard(day);
            }
        });

        return menuItem;
    }

    /**
     * menu for copying events of the day
     * @return menuItem
     */
    private JMenuItem copyDayEventMenu() {
        JMenuItem menuItem = new JMenuItem("ک‍پی مناسبت روز");

        menuItem.setFont(font);
        menuItem.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));
        menuItem.setMnemonic('م');
        menuItem.setDisplayedMnemonicIndex(5);
        menuItem.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(day.dayEvent);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            }
        });

        return menuItem;
    }

    /**
     * Declaring menubar and its menus
     * @return menuBar
     */
    JMenuBar makeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JMenu insertMenu = new JMenu("درج");
        insertMenu.setFont(font);
        insertMenu.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        insertMenu.add(newEventMenu());
        insertMenu.add(newNoteMenu());


        JMenu editMenu = new JMenu("ویرایش");
        editMenu.setFont(font);
        editMenu.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        editMenu.add(editNoteAndEventMenu());
        editMenu.add(copyDateMenu());
        editMenu.add(copyDayEventMenu());

        menuBar.add(insertMenu);
        menuBar.add(editMenu);

        return menuBar;
    }

    /**
     * Declaring PopupMenu
     * @return JpopupMenu
     */
    JPopupMenu makePopupMenu() {
        JPopupMenu jPopupMenu = new JPopupMenu();

        jPopupMenu.add(newEventMenu());
        jPopupMenu.add(newNoteMenu());
        jPopupMenu.add(new JPopupMenu.Separator());
        jPopupMenu.add(editNoteAndEventMenu());
        jPopupMenu.add(copyDateMenu());
        jPopupMenu.add(copyDayEventMenu());

        return jPopupMenu;
    }
}
