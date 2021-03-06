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
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//  Class       :  Fraction
//  Author      :  Eric Holm
//  Version     :  1.2.0 (FINAL)
//  Description :  Class to contain a fraction
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

//  Package Declaration
//------------------------------------------------------------------------------
package com.elementaryengineers.fwc.random;
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
public final class Fraction
{
    //  Class Variables  //
    //==========================================================================
    private int mixedWhole;             //  Contains mixed value whole portion
    private int mixedNum;               //  Contains mixed value numerator
    private int mixedDen;               //  Contains mixed value denominator
    private int numerator;              //  Contains the numerator
    private int denominator;            //  Contains the denominator
    //==========================================================================
    
    //  Constructor  //
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //  Creates a Fraction based on the input given to the class
    public Fraction(int numerator, int denominator)
    {
        setFraction(numerator, denominator);
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    //  setFraction  //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //  Sets the values of the numerator and denominator
    public void setFraction (int numerator, int denominator)
    {
        this.numerator   = numerator;
        this.denominator = denominator;
        setMixedFraction();
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    //  setMixedFraction  //
    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    //  Sets the values of the mixed version of the fraction
    private void setMixedFraction()
    {
        mixedNum = numerator;
        mixedDen = denominator;
        convertMixed();
    }
    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    
    //  convertLowestTerms  //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //  Reduces a fraction to lowest terms
    public void convertLowestTerms()
    {
        //  Method Variables  //
        //======================================================================
        int tempNum = numerator;
        int tempDen = denominator;
        int gcf = 1;
        //======================================================================
        
        //  Use modulus to determine the gcf
        //  Loop through reducing until there is no common divisor
        while (tempNum > 0)
        {
            gcf = tempNum;
            tempNum = tempDen % tempNum;
            tempDen = gcf;
        }
        
        //  Divide both numerator and denominator by gcf to put faction into
        //  lowest terms.
        numerator = numerator / gcf;
        denominator = denominator / gcf;
        mixedNum = mixedNum / gcf;
        mixedDen = mixedDen / gcf;
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    //  convertMixed  //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //  Converts fraction into mixed fraction
    public void convertMixed()
    {
        //  Reduce the numerator by the value of the denominator.
        //  Each loop adds a whole value to the fraction.
        mixedWhole = 0;
        
        while (mixedNum >= mixedDen)
        {
            mixedNum = mixedNum - mixedDen;
            mixedWhole++;
        }
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    //  convertWord  //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public String convertWord()
    {
        
        String[] numText = {"zero", "one", "two", "three", "four", "five", 
                            "six", "seven", "eight", "nine", "ten", "eleven",
                            "twelve", "thirteen", "fourteen", "fifteen", 
                            "sixteen", "seventeen", "eighteen", "nineteen",
                            "twenty"};
        String[] denText = {"zero", "one", "half", "third", "fourth",
                            "fifth", "sixth", "seventh", "eighth", 
                            "ninth", "tenth", "eleventh", "twelth", 
                            "thirteenth", "fourteenth", "fifteenth", 
                            "sixteenth", "seventeenth", "eighteenth", 
                            "nineteenth", "twentith"};
        return (String.format("%s-%s", numText[numerator], denText[denominator]));
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    //  getNumerator  //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //  Return the fraction numerator
    public int getNumerator()
    {
        return numerator;
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    //  getDenominator  //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //  Return the fraction denominator
    public int getDenominator()
    {
        return denominator;
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    //  getMixedNumerator  //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //  Return the numerator of the Mixed Fraction
    public int getMixedNumerator()
    {
        return mixedNum;
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    //  getMixedDenominator  //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //  Return the denominator of the Mixed Fraction
    public int getMixedDenominator()
    {
        return mixedDen;
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    //  getMixedWhole  //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //  Return the whole portion of the Mixed Fraction
    public int getMixedWhole()
    {
        return mixedWhole;
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    //  toString  //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //  String version of the base fraction
    @Override
    public String toString()
    {
        return String.format("%d / %d", numerator, denominator);
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    //  toStringMixed  //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //  String version of the mixed fraction
    public String toStringMixed()
    {
        return String.format("%d %d / %d", mixedWhole, mixedNum, mixedDen);
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}
//  End class Fraction
//------------------------------------------------------------------------------
