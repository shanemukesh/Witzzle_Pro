package com.company.Wizzle;

import java.util.*;
import java.util.function.IntBinaryOperator;

interface Print {
    void say(String str);
}

/*
    Date: 08/29/2020
    Written by: Mukesh Bhat
    Problem: Tanay's math problem with Witzzle Pro
    Description: Given 3 * 3 matrix of non-zero single digit non-repetitive numbers/integers,
                 using basic arithmetic operations + - * / compute(output) with result set
                 from 1 to 30 as result of arithmetic operation through either same row
                 or same column or diagonal entries of input matrix(3 operands each time).
*/

public class Wizzle {
    static int MAXROW = 3;
    static int MAXCOLUMN = 3;
    static int MAPSIZE = 100;
    static int Counter = 0;
    static int LoopCount = 0;

    static int[][] input = new int[MAXROW][MAXCOLUMN];
    static Set<String> operators = new HashSet<>(Arrays.asList("+", "-", "*", "/"));

    static Map<Integer, String> output;

    static Print pri = System.out::print;

    public static void main(String[] args) {

       // pri.say("Hello World");

        //1. input 3 X 3 array of int values
        getInputs();

        //2. Display input matrix
        displayInputs();

        //3. initialize output map
        initializeMap();

        //4. fill output map
        fillOutputMap();

        //5. Display output map
        displayOutput();
        pri.say("\nCompleted, loop count:" + LoopCount + " , counter: " + Counter + "\n");
    }

    static void getInputs() {
        Scanner sc = new Scanner(System.in);
        pri.say(" Enter input integers: \n");

        for (int i=0; i<MAXROW; i++) {
            for (int j=0; j<MAXCOLUMN; j++) {
                input[i][j] = sc.nextInt();
            }
        }
    }

    private static void displayInputs() {
        pri.say("\nInput matrix: \n");
        for(int[] i: input) {
            for(int j: i) {
                pri.say(j + "  ");
            }
            pri.say("\n");
        }
    }

    private static void initializeMap() {
        output = new HashMap<>();
        for (int i = 0 ; i< MAPSIZE; i++) {
            output.put(i+1, "");
        }
    }

    private static void fillOutputMap() {
        //rows
        for(int i=0; i< MAXCOLUMN; i++)
        {
            int first = input[i][0];
            int second = input[i][1];
            int third = input[i][2];

            fillOutputMap(first, second, third);
        }

        //columns
        for(int i=0; i< MAXROW; i++)
        {
            int first = input[0][i];
            int second = input[1][i];
            int third = input[2][i];

            fillOutputMap(first, second, third);
        }

        //diagonal
        //TODO logic needed, hard coding now
        {
            int first = input[0][0];
            int second = input[1][1];
            int third = input[2][2];

            fillOutputMap(first, second, third);

            first = input[0][2];
            second = input[1][1];
            third = input[2][0];

            fillOutputMap(first, second, third);
        }
    }

    private static void fillOutputMap(int first, int second, int third) {
        //all possible combinations of these 3 numbers
        //example: input: 1 2 3
        //combinations: 1 2 3, 1 3 2, 2 3 1, 2 1 3, 3 1 2, 3 2 1
        int[] inputs = {first, second, third};

        for(int x = 0; x < 3; x++) {
            for (int y = 0; y <3; y++) {
                for (int z = 0; z <3; z++) {
                    if(x!=y && y!=z && z!=x) {
                        fillOutputMapEach(inputs[x], inputs[y], inputs[z]);
                    }
                }
            }
        }
    }

    private static void fillOutputMapEach(int first, int second, int third) {
        int result1, result2;
        String str;

        for(String oper: operators) {
            result1 = operate(oper, first, second);
            if (result1 >= 0) { //to take care of divisible %
                for (String op : operators) {
                    result2 = operate(op, result1, third);
                    str = "( " + first + " " + oper + " " + second + " ) " + op + " " + third;
                    if(updateOutputMap(result2, str)) {
                        //updated, can use some logging here
                    }
                }
            }
        }
    }

    private static int operate(String oper, int first, int second) {
        int result = 0;

        IntBinaryOperator operAdd = Integer::sum;
        IntBinaryOperator operSubstract = (a, b) -> a - b;
        IntBinaryOperator operMultiply = (a, b) -> a * b;
        IntBinaryOperator operDivide = (a, b) -> a / b;

        switch (oper) {
            case "+":
                result = operAdd.applyAsInt(first, second);
                break;
            case "-":
                result = operSubstract.applyAsInt(first, second);
                break;
            case "*":
                result = operMultiply.applyAsInt(first, second);
                break;
            case "/":
                if (first % second == 0) {
                    result = operDivide.applyAsInt(first, second);
                } else {
                    result = -1;
                }
                break;
        }

        return result;
    }

    private static boolean updateOutputMap(int result, String str) {
        for(Map.Entry<Integer, String> keys: output.entrySet()) {
            LoopCount++;
            Integer key = keys.getKey();
            String value = keys.getValue();
            if(key.equals(result) && value.isEmpty()) {
                Counter++;
                output.put(result, str);
                if (Counter == MAPSIZE) {
                    //complete
                    displayOutput();
                    pri.say("\nCompleted, loop count:" + LoopCount + " , counter: " + Counter + "\n");
                    System.exit(0);
                }
                return true;
            }
        }
        return false;
    }

    private static void displayOutput() {
        pri.say("\nOutput map: \n");
        for(Map.Entry<Integer, String> keys: output.entrySet()) {
            pri.say(keys.getKey() + " = " + keys.getValue() + "\n");
        }
    //    output.forEach((key,value) -> pri.say(key + " = " + value));
    }

}
