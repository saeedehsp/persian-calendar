package com.calendar;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * setting up  notes Jdialog
 */
class Notes extends JDialog {
    private JEditorPane note;

    /**
     * @param dayData information
     * @param noteId id of each note which is defined by
     */
    Notes(final DayData dayData, final int noteId) {
        super(new Frame(), "یادداشت", true);

        setIconImage(Utils.getApplicationIcon());

        final Note noteData;
        if (dayData.notes == null) dayData.notes = new ArrayList<Note>();
        if (noteId == -1)
            noteData = new Note();
        else
            noteData = dayData.notes.get(noteId);

        setLayout(new BorderLayout());
        Container contentPane = getContentPane(); // set container

        JPanel colorBox = new JPanel(new GridLayout(1, 8));   // design colorBox for coloring the text writen
        colorBox.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        colorBox.setPreferredSize(new Dimension(getWidth(), 30));

        colorBox.add(
                focusOnNoteAfterClick(Utils.makeColoredButton(Color.black))
        );
        colorBox.add(
                focusOnNoteAfterClick(Utils.makeColoredButton(new Color(253, 116, 110)))
        );
        colorBox.add(
                focusOnNoteAfterClick(Utils.makeColoredButton(new Color(254, 200, 109)))
        );
        colorBox.add(
                focusOnNoteAfterClick(Utils.makeColoredButton(new Color(255, 255, 123)))
        );
        colorBox.add(
                focusOnNoteAfterClick(Utils.makeColoredButton(new Color(194, 255, 126)))
        );
        colorBox.add(
                focusOnNoteAfterClick(Utils.makeColoredButton(new Color(154, 255, 230)))
        );
        colorBox.add(
                focusOnNoteAfterClick(Utils.makeColoredButton(new Color(112, 205, 255)))
        );
        colorBox.add(
                focusOnNoteAfterClick(Utils.makeColoredButton(new Color(196, 207, 212)))
        );

        JPanel formatBox = new JPanel(new GridLayout(1, 4)); //design the panel of formatting text writen
        formatBox.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        formatBox.setPreferredSize(new Dimension(getWidth(), 30));

        Font font = Utils.font;
        Font smallerFont = font.deriveFont(10.0f);

        JButton bold = new JButton("درشت کن");
        bold.setFont(smallerFont);
        bold.addActionListener(new HTMLEditorKit.BoldAction());
        focusOnNoteAfterClick(bold);
        bold.setToolTipText("درشت (Bold) کردن متن انتخاب شده");

        JButton strike = new JButton("خط بزن");
        strike.setFont(smallerFont);
        strike.setToolTipText("خط کشیدن (Strike-Through) روی متن انتخاب شده");
        strike.addActionListener(new StrikeThroughAction());
        focusOnNoteAfterClick(strike);

        JButton link = new JButton("لینک بذار");
        link.setFont(smallerFont);
        link.setToolTipText("اضافه کردن لینک به متن انتخاب شده");
        link.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getUrlAndInsert();
            }
        });

        JButton image = new JButton("عکس بذار");
        image.setFont(smallerFont);
        image.setToolTipText("پیاده سازی نشده"); // TODO: Implement it
        image.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseImageAndInsert();
            }
        });

        formatBox.add(bold);
        formatBox.add(strike);
        formatBox.add(link);
        formatBox.add(image);

        note = new JEditorPane("text/html", "");
        note.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        note.setFont(font);
        note.setMargin(new Insets(5, 5, 5, 5));
        note.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        note.setPreferredSize(new Dimension(getWidth(), 150));
        note.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


        JButton save = new JButton("ذخیره");
        save.setPreferredSize(new Dimension(getWidth(), 30));
        save.setFont(font);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noteData.note = note.getText();
                if (noteId == -1) {
                    dayData.notes.add(noteData);
                }
                Utils.save();
                dispose();
            }
        });

        JPanel north = new JPanel(new BorderLayout());
        north.setPreferredSize(new Dimension(getWidth(), 60));
        north.add(colorBox, BorderLayout.NORTH);
        north.add(formatBox, BorderLayout.SOUTH);

        contentPane.add(north, BorderLayout.NORTH);
        contentPane.add(note, BorderLayout.CENTER);
        contentPane.add(save, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setSize(new Dimension(350, 240));

        if (noteId != -1)
            setText(noteData.note);

        note.requestFocus();

        setResizable(false);

        setVisible(true);
    }

    private void getUrlAndInsert() {
        String url = (String) JOptionPane.showInputDialog(
                this,
                ":آدرس اینترنتی",
                "اضافه کردن لینک",
                JOptionPane.QUESTION_MESSAGE,
                new ImageIcon(Utils.getFileAddress("link.png")),
                null,
                "http://"
        );

        if ((url == null) || (url.length() > 0))
            return;

        String text = note.getSelectedText();
        if (text.isEmpty()) text = url;
        String linkHtml = "<a href=\"" + url + "\">" + text + "</a>";
        note.replaceSelection("");
        try {
            ((HTMLEditorKit)note.getEditorKit()).insertHTML(
                    (HTMLDocument) note.getDocument(), note.getCaretPosition(), linkHtml, 0, 0, HTML.Tag.A
            );
        } catch (BadLocationException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * inserting image in textpane
     */
    private void chooseImageAndInsert() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            int width = 100;
            int height = 100;
            HTMLDocument doc = (HTMLDocument) note.getDocument();
            try {
                Image image = new ImageIcon(selectedFile.toURI().toURL()).getImage();
                width = image.getWidth(null);
                height = image.getHeight(null);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Element body = doc.getDefaultRootElement().getElement(1);
//            selectedFile.getAbsolutePath()

            try {
                doc.insertAfterStart(body, "<img width=\"" + width + "\" height=\"" + height + "\" src=\"" + selectedFile.toURI().toString() + "\" />");
            } catch (BadLocationException exception) {
                exception.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }


    }

    /**
     * go directly to the textpane after click
     * @param button the button to add listener to
     * @return button
     */
    private JButton focusOnNoteAfterClick(JButton button) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                note.requestFocus();
            }
        });
        return button;
    }

    /**
     * detting the text in textpane
     * @param text input
     */
    private void setText(String text) {
        note.setText(text);
    }
}

