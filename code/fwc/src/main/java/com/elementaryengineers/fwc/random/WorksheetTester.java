//  Package Declaration
package com.elementaryengineers.fwc.random;

//  Imports  //
//------------------------------------------------------------------------------
import java.util.Scanner;

//  Class   :  WorksheetTester
//  Author  :  Eric Holm
//  Version :  1.0.0

//  Tester Class for the FractionGenerator
//------------------------------------------------------------------------------
public class WorksheetTester
{
    //  Main  //
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public static void main(String[] args)
    {
        //  Main Constants  //
        //======================================================================
        //  Flags for fraction generation
        //  These signal if a pair of fractions should have matching denominators
        final int GEN_DENOM_UNMATCHED = 0;
        final int GEN_DENOM_MATCHED = 1;
    
        //  These signal if fractions are allowed to evaluate to 1 or greater
        final int GEN_WHOLENUM_YES = 0;
        final int GEN_WHOLENUM_NO = 1;
        
        //  Main Variables  //
        //======================================================================
        Scanner input = new Scanner(System.in);
               
        //  Main Code  //
        //======================================================================
        //  Request Seed Value from User
        System.out.printf("Enter Seed Value: ");
        int seedValue = input.nextInt();
        
        //  FractionGenerator Test
        FractionGenerator fRNG1 = 
                new FractionGenerator(seedValue, 1, 24, 2, 24, 
                                      GEN_DENOM_MATCHED,
                                      GEN_WHOLENUM_NO);
        System.out.printf("fRNG1: %d\n", fRNG1.getSeedValue());
        fRNG1.printFractions();
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}
//------------------------------------------------------------------------------
//  End class WorksheetTester