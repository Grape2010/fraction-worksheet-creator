/****************************************************************************
 * Name: Fraction Worksheet Creator
 * Team: Elementary Engineers 
 * Date produced: 04/28/2016
 * ________________________________
 * Purpose of program:
 * The Fraction Worksheet Creator (FWC) is a new stand-alone product 
 * that allows teachers and students to create random exercise worksheets 
 * to practice operations with fractions.The generated worksheets can contain 
 * fraction problems of various difficulty levels, from basic addition and 
 * subtraction problems with visuals and images suitable for small children, 
 * to quite advanced fraction equations. 
 * ****************************************************************************
 */

package com.elementaryengineers.fwc.model;

/**
 * Created by sarahakk on 4/22/16.
 */
public class Difficulty {

    private int difficultyID;
    private String description;

    public Difficulty(int id, String description) {
        this.difficultyID = id;
        this.description = description;
    }

    public int getDifficultyID() {
        return difficultyID;
    }

    public String getDescription() {
        return description;
    }
}