package com.calendar;

import java.util.ArrayList;

/**
 * conserves days' informations
 */
class DayData {
    /**
     * calendar event of the day
     */
    String dayEvent;
    /**
     * Array of saved events
     */
    ArrayList<Event> events = new ArrayList<Event>();
    /**
     * Array of saved notes
     */
    ArrayList<Note> notes = new ArrayList<Note>();
    /**
     * defining the state of the day
     */
    boolean isHoliday;
    /**
     * shamsi date of the day
     */
    transient Integer number; // Don't save this field
    /**
     * shamsi month date of the day
     */
    transient Integer month; // Don't save this field
    /**
     * shamsi name of the month
     */
    transient String monthName; // Don't save this field

    /**
     * default constructor
     */
    DayData() {

    }

    /**
     * @return month
     */
    int getMonth() {
        return month;
    }

    /**
     * @return shamsi date
     */
    int getDay() {
        return number;
    }

    /**
     * @return formatted full date
     */
    String getFullDate() {
        return "1395/" + month.toString() + "/" + number.toString();
    }

    /**
     * @return tooltip in form of html
     */
    String getToolTipHtml() {
        String dayToolTip = "<html>" + dayEvent + "<br>";
        for (Event event : events) {
            dayToolTip += event.text + "<br>";
        }
        for (Note note : notes) {
            dayToolTip += note.note + "<br>";
        }
        dayToolTip += "</html>";

        if (!dayToolTip.equals("<html><br></html>")) {
            return Utils.correctEncoding(dayToolTip);
        }
        return null;

    }

    /**
     * @return tooltip in form of text
     */
    String getToolTipText() {
        String dayToolTip =  dayEvent + "\n";
        dayToolTip += notes.size() + " یادداشت \n";
        dayToolTip += events.size() + " رویداد";
        
        return dayToolTip;

    }
}
