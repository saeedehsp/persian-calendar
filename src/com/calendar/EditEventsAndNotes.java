package com.calendar;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * editing notes and events
 */
class EditEventsAndNotes extends JFrame {
    /**
     * default font
     */
    private Font font;
    /**
     * selected day information
     */
    private DayData dayData;
    private final JScrollPane eventsScrollPane;
    private final JScrollPane notesScrollPane;
    private DefaultListModel eventsList;
    private DefaultListModel notesList;

    /**
     * setting up Jframe
     * setting up panels of notes and events
     * desining items of panles by using html
     * @param dayData of the day selected for editing notes and events
     */
    EditEventsAndNotes(final DayData dayData) {
        super("ویرایش اطلاعات " + Utils.en2fa(dayData.getFullDate())); // showing selected day as Jframe title
        setSize(300, 600);
        setIconImage(Utils.getApplicationIcon());
        setLayout(new BorderLayout());

        this.dayData = dayData;

        font = Utils.font;

        eventsList = new DefaultListModel();
        //setting up a list of events
        final JList eventsPanel = new JList(eventsList);
        eventsPanel.setDragEnabled(false);
        eventsPanel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!eventsPanel.isSelectionEmpty()) {
                    new Events(dayData, e.getFirstIndex());
                    loadData();
                }
                eventsPanel.clearSelection();
            }
        });
        eventsPanel.setFont(font);
        eventsPanel.setOpaque(false);

        notesList = new DefaultListModel();
        //setting up a list of notes
        final JList notesPanel = new JList(notesList);
        notesPanel.setDragEnabled(false);
        notesPanel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!notesPanel.isSelectionEmpty()) {
                    new Notes(dayData, e.getFirstIndex());
                    loadData();
                }
                notesPanel.clearSelection();
            }
        });
        notesPanel.setFont(font);
        notesPanel.setOpaque(false);

        // using JScrollPane in case of need for accessing upper and lower events and notes
        eventsScrollPane = new JScrollPane(
                eventsPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        eventsScrollPane.setPreferredSize(new Dimension(getWidth(), getHeight()/2 - 10));
        TitledBorder eventsTitle = new TitledBorder("رویداد ها");
        eventsTitle.setTitleFont(font);
        eventsScrollPane.setBorder(eventsTitle);
        eventsScrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        eventsScrollPane.setFont(font);




        notesScrollPane = new JScrollPane(notesPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        notesScrollPane.setPreferredSize(new Dimension(getWidth(), getHeight()/2 - 10));
        TitledBorder notesTitle = new TitledBorder("یادداشت ها");
        notesTitle.setTitleFont(font);
        notesScrollPane.setBorder(notesTitle);
        notesScrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        notesScrollPane.setFont(font);

        loadData();

        Container container = getContentPane();
        container.add(eventsScrollPane, BorderLayout.NORTH); // demonstrating list of events
        container.add(notesScrollPane, BorderLayout.SOUTH);  // demonstrating list of notes

        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadData() {
        eventsList.removeAllElements();
        if (dayData.events == null) dayData.events = new ArrayList<Event>(); //setting up panel if there exists no events

        String bodyReplacement = "<body style='width: 210px;'>" +
                "<div style='background: #f3f3f3; border: 1px solid #e0e0e0; direction: rtl; text-align: right; padding: 2px 5px;'>";
        String endBodyReplacement = "</div></body>";

        if (dayData.events.size() == 0) {
            eventsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            eventsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            JLabel label = new JLabel("رویدادی موجود نیست");
            label.setPreferredSize(new Dimension(getWidth() - 10, getHeight() / 2 - 30));
            label.setFont(font);
            label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            eventsList.addElement(label);
        } else {
            for (int i=0; i<dayData.events.size(); i++) {
                Event event = dayData.events.get(i);
                String text = "<html><body>";
                if (event.title != null && event.title.length() > 0)
                    text += "<h3>" + event.title + "</h3>";
                if (event.text != null && event.text.length() > 0)
                    text += "<span>" + event.text.replaceAll("\n", "<br>") + "</span>";
                text += "</body></html>";
                text = text.replace("<body>", bodyReplacement);
                text = text.replace("</body>", endBodyReplacement);
                text = Utils.correctEncoding(text);
                eventsList.addElement(text);
            }
        }

        notesList.removeAllElements();
        if (dayData.notes == null) dayData.notes = new ArrayList<Note>();
        if (dayData.notes.size() == 0) {
            notesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            notesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            notesList.addElement("یادداشتی موجود نیست");
        } else {
            for (int i=0; i<dayData.notes.size(); i++) {
                Note note = dayData.notes.get(i);
                String text = note.note;

                text = text.replace("<body>", bodyReplacement);
                text = text.replace("</body>", endBodyReplacement);
                text = Utils.correctEncoding(text);

                JLabel label = new JLabel(text);
                label.setFont(font);
                label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                final int index = i;
                label.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        new Notes(dayData, index);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
                notesList.addElement(text);

//                eventsList.updateUI();
//                notesList.updateUI();
            }
        }
    }
}
