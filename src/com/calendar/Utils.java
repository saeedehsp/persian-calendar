package com.calendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.Color;
import java.awt.datatransfer.StringSelection;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

/**
 * utility functions used in classes
 */
class Utils {
    /**
     * object for reading and writing from and to Json file
     */
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    /**
     * load the information of months
     */
    static Month[] months = load();

    /**
     * declare default font
     */
    static Font font;

    /**
     * @param en text with enlglish numbers
     * @return texts with farsi numbers
     */
    static String en2fa(String en) {
        en = en.replaceAll("0", "\u06F0");
        en = en.replaceAll("1", "\u06F1");
        en = en.replaceAll("2", "\u06F2");
        en = en.replaceAll("3", "\u06F3");
        en = en.replaceAll("4", "\u06F4");
        en = en.replaceAll("5", "\u06F5");
        en = en.replaceAll("6", "\u06F6");
        en = en.replaceAll("7", "\u06F7");
        en = en.replaceAll("8", "\u06F8");
        en = en.replaceAll("9", "\u06F9");
        return en;
    }

    /**
     * @param fa texts with farsi numbers
     * @return texts with english numbers
     */
    static String fa2en(String fa) {
        fa = fa.replaceAll("\u06F0", "0");
        fa = fa.replaceAll("\u06F1", "1");
        fa = fa.replaceAll("\u06F2", "2");
        fa = fa.replaceAll("\u06F3", "3");
        fa = fa.replaceAll("\u06F4", "4");
        fa = fa.replaceAll("\u06F5", "5");
        fa = fa.replaceAll("\u06F6", "6");
        fa = fa.replaceAll("\u06F7", "7");
        fa = fa.replaceAll("\u06F8", "8");
        fa = fa.replaceAll("\u06F9", "9");
        return fa;
    }

    /**
     * @return load the array of information of each month
     */
    private static Month[] load() {
        try {
            FileReader fileReader = new FileReader("data.json");
            Month[] months = gson.fromJson(fileReader, Month[].class);
            fileReader.close();

            int i = 0;
            for (Month month : months) {
                i++;
                int j = 0;
                for (DayData day : month.days) {
                    j++;
                    day.number = j;
                    day.month = i;
                    day.monthName = month.name;
                }
            }

            return months;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Month[0];
    }

    /**
     * write information to json file
     */
    static void save() {
        try {
            FileWriter fileWriter = new FileWriter("data.json");
            gson.toJson(months, fileWriter);
            fileWriter.close();
            Main.setReminders();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param number the date of the day
     * @return buffered image
     */
    static Image makeIcon(int number) {
        String text = Utils.en2fa(String.valueOf(number));

        Image baseImage = new ImageIcon(getFileAddress("calendar-icon.png")).getImage();

        BufferedImage bufferedImage = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();

        graphics.drawImage(baseImage,0, 0, null);

        graphics.setFont(font.deriveFont(470.0f));
        int x = (1024-graphics.getFontMetrics().stringWidth(text))/2;
        graphics.setPaint(Color.black);
        graphics.drawString(text, x, 830);

        return bufferedImage;
    }

    /**
     * change the color of the texts by choosing each button
     * @param color color of button
     * @return button
     */
    static JButton makeColoredButton(Color color) {
        int width = 30;
        int height = 30;
        int radius = 10;

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Shape circle = new Ellipse2D.Double(width/2 - radius, height/2 - radius, 2.0 * radius, 2.0 * radius);

        graphics.setColor(color);
        graphics.fill(circle);

        graphics.setColor(Color.gray);
        graphics.draw(circle);

        JButton button = new JButton(new ImageIcon(bufferedImage));
        button.setBorder(new EmptyBorder(0,0,0,0));

        button.addActionListener(new HTMLEditorKit.ForegroundAction("Color", color));

        return button;
    }

    /**
     * @param width of the clock
     * @param height of the clock
     * @param radius length of the hand
     * @param isHourHand whether is showing the hour
     * @param numberText pointing number
     * @return ImageIcon
     */
    static ImageIcon makeClockHand(int width, int height, int radius, boolean isHourHand, String numberText) {

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // fix the default aliasing mode
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY); // giving the priority to quality for loading the image
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        int stroke = 2;
        graphics.setColor(Color.red);

        if (isHourHand) {
            stroke = 4;
            graphics.setColor(Color.black);
        }

        graphics.setStroke(new BasicStroke(stroke));

        int centerX = width / 2 - 1;
        int centerY = height / 2 + 3;

        if (!numberText.isEmpty()) {
            int number = Integer.valueOf(fa2en(numberText));

            if (!isHourHand) {
                number /= 5;
            }

            int x = (int) (radius * Math.cos(Math.toRadians((double) (number * 30 - 90))));
            int y = (int) (radius * Math.sin(Math.toRadians((double) (number * 30 - 90))));

            graphics.drawLine(centerX, centerY, centerX + x, centerY + y);
        }

        graphics.setColor(Color.black);
        Shape hourCircle = new Ellipse2D.Double(centerX - 2, centerY - 2, 4.0d, 4.0d);
        graphics.draw(hourCircle);

        graphics.setColor(Color.red);
        Shape minuteCircle = new Ellipse2D.Double(centerX - 1, centerY - 1, 2.0d, 2.0d);
        graphics.draw(minuteCircle);

        return new ImageIcon(bufferedImage);
    }

    /**
     * @param calendar default
     * @return second remains until tomorrow
     */
    static int getSecondsUntilTomorrow(com.ibm.icu.util.Calendar calendar) {
        com.ibm.icu.util.Calendar tomorrow = (com.ibm.icu.util.Calendar) calendar.clone();

        tomorrow.add(java.util.Calendar.DATE, 1);
        tomorrow.set(java.util.Calendar.HOUR_OF_DAY, 0);
        tomorrow.set(java.util.Calendar.MINUTE, 0);
        tomorrow.set(java.util.Calendar.SECOND, 0);

        return calendar.fieldDifference(tomorrow.getTime(), java.util.Calendar.SECOND);
    }

    /**
     * @param calendar default
     * @param hour given
     * @param minute given
     * @return seconds until the time given
     */
    static int getSecondsUntil(com.ibm.icu.util.Calendar calendar, int hour, int minute) {
        int hourNow = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        int minuteNow = calendar.get(java.util.Calendar.MINUTE);
        if (hour < hourNow || (hour == hourNow && minute < minuteNow)) return -1; // Ignore it if it is from past

        com.ibm.icu.util.Calendar theTime = (com.ibm.icu.util.Calendar) calendar.clone();
        theTime.set(java.util.Calendar.HOUR_OF_DAY, hour);
        theTime.set(java.util.Calendar.MINUTE, minute);
        theTime.set(java.util.Calendar.SECOND, 0);

        return calendar.fieldDifference(theTime.getTime(), java.util.Calendar.SECOND);
    }

    /**
     * copy information of the day
     * @param dayData day information
     */
    static void copyDayToClipboard(DayData dayData) {
        String text = dayData.getFullDate();
        text = Utils.en2fa(text);
        StringSelection stringSelection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }

    /**
     * get absolute path of files
     * @param filename filename
     * @return resource
     */
    static URL getFileAddress(String filename) {
        return ClassLoader.getSystemResource("files/" + filename);
    }

    /**
     * @return application default icon image
     */
    static Image getApplicationIcon() {
        return new ImageIcon(Utils.getFileAddress("calendar-icon.png")).getImage();
    }

    /**
     * @param html input html string
     * @return same html with utf-8 encoding meta added
     */
    static String correctEncoding(String html) {
        return html.replace("<head>", "<head><meta http-equiv=\"content-type\" content=\"text/html\" charset=\"UTF-8\" />");
    }
}
