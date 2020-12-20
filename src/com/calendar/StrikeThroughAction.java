package com.calendar;

import javax.swing.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.event.ActionEvent;

class StrikeThroughAction extends HTMLEditorKit.StyledTextAction {

    /**
     * Constructs a new StrikeThroughAction.
     */
    public StrikeThroughAction() {
        super("font-strikethrough");
    }

    /**
     * Toggles the strikethrough attribute.
     *
     * @param e the action event
     */
    public void actionPerformed(ActionEvent e) {
        JEditorPane editor = getEditor(e);
        if (editor != null) {
            HTMLEditorKit kit = (HTMLEditorKit) getStyledEditorKit(editor);
            MutableAttributeSet attr = kit.getInputAttributes();
            boolean strikethrough = !StyleConstants.isStrikeThrough(attr);
            SimpleAttributeSet sas = new SimpleAttributeSet();
            StyleConstants.setStrikeThrough(sas, strikethrough);
            setCharacterAttributes(editor, sas, false);
        }
    }
}