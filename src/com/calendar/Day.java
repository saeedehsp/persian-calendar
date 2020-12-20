package com.calendar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;


class Day extends JPanel{
    /**
     * Declare persian date
     */
    private JLabel solar;
    /**
     * Declare christian date
     */
    private JLabel christian;
    /**
     * Declare Arabic date
     */
    private JLabel lunar;
    /**
     * refrencing to the calendar contaning this day
     */
    private Calendar parent;
    /**
     * information of this day
     */
    private DayData dayData;

    /**
     * set Jpanel of each day in calendar table
     * @param parent calendar contaning the day
     * @param solar  Persian date number
     * @param christian Gregorian date number
     * @param lunar Arabic date number
     * @param dayData object of day information
     */
    Day(final Calendar parent, final Integer solar /*defining final for accessing in listener as an innerclass*/, Integer christian, Integer lunar, final DayData dayData){
        this.parent = parent;
        this.dayData = dayData;
        Font font = Utils.font;

        this.setOpaque(true);
        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 224, 80)); //JPanel background
        setBorder(new EmptyBorder(1, 1, 1, 1));

        Color textColor = Color.black;
        if (dayData.isHoliday) {
            textColor = Color.red;
        } //define holidays in table

        this.solar = new JLabel(Utils.en2fa(solar.toString()));
        this.solar.setHorizontalAlignment(SwingConstants.CENTER);
        this.solar.setFont(font);
        this.solar.setForeground(textColor);
        //design solar date


        this.christian = new JLabel(christian.toString());
        this.christian.setHorizontalAlignment(SwingConstants.CENTER);
        this.christian.setPreferredSize(new Dimension(30 , 15));
        this.christian.setFont(font);
        this.christian.setForeground(textColor);
        //design christian date


        this.lunar = new JLabel(Utils.en2fa(lunar.toString()));
        this.lunar.setPreferredSize(new Dimension(30 , 15));
        this.lunar.setHorizontalAlignment(SwingConstants.CENTER);
        this.lunar.setFont(font);
        this.lunar.setForeground(textColor);
        //design lunar date


        add(this.solar, BorderLayout.CENTER);
        JPanel notSolar = new JPanel();
        notSolar.setOpaque(false);
        notSolar.setLayout(new BorderLayout());

        notSolar.add(this.christian , BorderLayout.WEST);
        notSolar.add(this.lunar , BorderLayout.EAST);

        add(notSolar , BorderLayout.SOUTH);
        setPreferredSize(new Dimension(60,60));

        Menus menus = new Menus(null, dayData); //setting up a menu for eachday by rightclicking
        setComponentPopupMenu(menus.makePopupMenu());

        MouseListener rightClick =  new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    select();
                }
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
        };

        addMouseListener(rightClick);
        setToolTip();
        setVisible(true);
    }

    /**
     * designing calendar cells wether they are today or not
     * @param isToday check if it is today or not
     */
    void isToday(boolean isToday){
        if(isToday){
            setBackground(new Color(47, 200, 224, 149));
        }
        else{
            setBackground(new Color(255, 255, 224, 80));
        }

    }

    /**
     * disine and define selected day cell
     */
    void select() {
        parent.selectDay(this);
        setBorder(new LineBorder(new Color(199, 75, 64)));
    }

    /**
     * deselecting day
     */
    void deselect() {
        setBorder(new EmptyBorder(1, 1, 1, 1));
    }

    /**
     * @return dayData of this Day
     */
    DayData getDayData() {
        return dayData;
    }

    /**
     * setting up tooltip for each day
     */
    private void setToolTip(){

        String dayToolTip = dayData.getToolTipHtml();
        if(dayToolTip != null){
            this.setToolTipText(dayToolTip);
        }

    }

}


