package com.tsystems.javaschool.tasks.calculator;

public class Calculator {
    private boolean checkBrackets(String str) {
        int num1 = 0;
        int num2 = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                num1++;
            } else if (str.charAt(i) == ')') {
                num2++;
            }
        }
        return num1 == num2;
    }
    private boolean checkString(String str) {
        if (str.equals("") || str.matches("(.*)[a-zA-Z]+(.*)")
                || str.matches("(.*)[*+-/]{2}(.*)")
        )
            return false;
        return checkBrackets(str);
    }
    private int findBeginOfNumber(String currBrackets, int mathSymbol) {
        int begin = mathSymbol - 1;

        if (begin < 0) {
            return -1;
        }
        while (begin >= 0 && (Character.isDigit(currBrackets.charAt(begin)) || currBrackets.charAt(begin) == '.' || currBrackets.charAt(begin) == '-')) {
            if (currBrackets.charAt(begin) == '-') {
                begin--;
                break;
            }
            begin--;
        }
        begin++;
       // System.out.println("BEGIN OF NUMBER " + begin);
        return begin;
    }
    private int findEndOfNumber(String currBrackets, int mathSymbol) {
        int end = mathSymbol + 1;

        if (end >= currBrackets.length()) {
            return -1;
        }
        if (currBrackets.charAt(end) == '-')
            end++;
        while (end < currBrackets.length() && (Character.isDigit(currBrackets.charAt(end)) || currBrackets.charAt(end) == '.')) {
            end++;
        }
        return end;
    }
    private Float findNumberAfter(String currBrackets, int mathSymbol) {
        float res = 0;
        int end = findEndOfNumber(currBrackets, mathSymbol);
        try {
            res = Float.parseFloat(currBrackets.substring(mathSymbol + 1, end));
        } catch (Exception e) {
            return null;
        }
        return res;
    }
    private Float findNumberBefore(String currBrackets, int mathSymbol) {
        float res = 0;

        int begin = findBeginOfNumber(currBrackets, mathSymbol);

        try {
            res = Float.parseFloat(currBrackets.substring(begin, mathSymbol));
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
        return res;
    }
    private String replaceSubstring(String str, String substr, int begin, int end) {
        //System.out.println("str " + str);
        String remove = str.substring(begin, end);
        //System.out.println("sub " + substr);
        //System.out.println("char at begin " + str.charAt(begin ));
        if (begin != 0 && Character.isDigit(str.charAt(begin -1)) && Character.isDigit(substr.charAt(0))) {
            str = str.replace(remove, "+" + substr);
            return str;
        }
        str = str.replace(remove, substr);
        return str;
    }
    private char chooseMathSymbol(String currBrackets, char ch1, char ch2) {
        int res1 = currBrackets.indexOf(ch1);
        int res2 = currBrackets.indexOf(ch2);
        if (res1 == -1)
            return ch2;
        if (res2 == -1)
            return ch1;
        if (res1 < res2)
            return ch1;
        return ch2;
    }

    private String multiplyOrDivide (String currBrackets) {
        Float num1;
        Float num2;
        Float res;
        int mathSymbol;

        mathSymbol = currBrackets.indexOf(chooseMathSymbol(currBrackets, '*', '/'));
//		if (currBrackets.indexOf("*") > currBrackets.indexOf("/")) {
//			mathSymbol = currBrackets.indexOf('*');
//		} else {
//			mathSymbol = currBrackets.indexOf("/");
//		}
        num1 = findNumberBefore(currBrackets, mathSymbol);
        num2 = findNumberAfter(currBrackets, mathSymbol);
       // System.out.println("num1 " + num1 + " num2 = " + num2);
        if (num1 == null || num2 == null)
            return null;
        if (currBrackets.charAt(mathSymbol) == '*') {
            res = num1 * num2;
        } else {
            if (num2 == 0){
                return null;
            }
            res = num1 / num2;
        }
        currBrackets = replaceSubstring(currBrackets, res.toString(), findBeginOfNumber(currBrackets, mathSymbol), findEndOfNumber(currBrackets, mathSymbol));
        return currBrackets;
    }

    private String calculate(String currBrackets) {
        int num1;
        int num2;
        int index;

        while(currBrackets.contains("*") || currBrackets.contains("/")) {
            currBrackets = multiplyOrDivide(currBrackets);
          //  System.out.println("calculation result DIV" + currBrackets);
            if (currBrackets == null) {
                return null;
            }
        }
        while(!isResult(currBrackets)) {
            currBrackets = addOrSubstract(currBrackets);
          //  System.out.println("calculation result PLUS" + currBrackets);
            if (currBrackets == null) {
                return null;
            }
        }

        return currBrackets;
    }

    private String addOrSubstract(String currBrackets) {
        Float num1;
        Float num2;
        Float res;
        int mathSymbol;

        if (currBrackets.indexOf("-", 1) < currBrackets.indexOf("+")) {
            mathSymbol = currBrackets.indexOf("+");
        } else if ((currBrackets.indexOf("-", 1) - currBrackets.indexOf("+")) == 1) {
            mathSymbol = currBrackets.indexOf("+");
        }else {
            mathSymbol = currBrackets.indexOf("-", 1);
        }
//		if (currBrackets.indexOf("-", 1) > currBrackets.indexOf("+")) {
//			System.out.println("I choose substr!");
//			mathSymbol = currBrackets.indexOf("-", 1);
//		} else {
//			System.out.println("I choose add!");
//			mathSymbol = currBrackets.indexOf("+");
//		}
        num1 = findNumberBefore(currBrackets, mathSymbol);
        num2 = findNumberAfter(currBrackets, mathSymbol);
       // System.out.println("num1 " + num1 + " num2 = " + num2);
        if (num1 == null || num2 == null)
            return null;
        if (currBrackets.charAt(mathSymbol) == '+') {
            res = num1 + num2;
        } else {
            res = num1 - num2;
        }
        currBrackets = replaceSubstring(currBrackets, res.toString(), findBeginOfNumber(currBrackets, mathSymbol), findEndOfNumber(currBrackets, mathSymbol));
     //   System.out.println("curr br " + currBrackets);
        return currBrackets;
    }

    private boolean isResult(String str) {
      //  System.out.println("Is result " + str + " " + str.matches("-?[\\d]+\\.?[\\d]*"));
        return str.matches("-?[\\d]+\\.?[\\d]*");
    }
    private String findBrackets(String currBrackets){ //1 - нашли скобки и все записали 0 нет скобок -1 ошибка со скобками
      //  System.out.println("i am in findBrackets " + currBrackets);
        if (!currBrackets.contains("(")) {
            if (!currBrackets.contains(")")) {
              //  System.out.println("calculate " + currBrackets);
                currBrackets = calculate(currBrackets);
              //  System.out.println("after calculate " + currBrackets);
                return currBrackets;
            }
            return null;
        }
        //если есть пара скобок
        String sub;
        if (currBrackets.contains(")")) { //maybe лишняя проверка? ведь мы проверяли это на входе
            int index1 = currBrackets.lastIndexOf("(");
            int index2 = currBrackets.indexOf(")");
            //System.out.println("index1 = " + index1 + " ind2 = " + index2);
            if ((index2 - index1) <= 1) {
                //System.out.println("here 3");
                return null;
            }
            //System.out.println("before " + currBrackets);
            sub = currBrackets.substring(index1 + 1, index2);
            //System.out.println("after sub " + sub);
            String res = "str";
            while(!isResult(currBrackets)) {
                res = findBrackets(sub);
                if (res == null) {
                    return null;
                }
                if (isResult(res)) {
                    currBrackets = currBrackets.replace("(" + sub + ")", res);
                    break;
                }
            }
            return currBrackets;
        }
        //System.out.println("before " + currBrackets);
        return null;

    }
    private String removeZero(String statement) {
        if (statement.contains(".0")) {
            statement = statement.replace(".0", "");
        }
//		while(statement.matches(".*\\..*0")) {
//			statement = statement.substring(0, statement.length() - 1);
//		} todo do it!!!!!!!
        return statement;
    }
    private static float round(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }
    public String evaluate(String statement) {
        if (statement == null)
            return null;
        statement = removeSpaces(statement);
        if (!checkString(statement)) {
            //System.out.println("Incorrect str, please, delete me!");
            return null;
        }
        while (!isResult(statement)) {
            statement = findBrackets(statement);
            if (statement == null)
                return  null;
        }

        float res = Float.parseFloat(statement); //todo перенести переменную в начало
        res = round(res, 4);
       // System.out.println(res);
        statement = Float.toString(res);
        statement = removeZero(statement);
        //System.out.println("final " + statement);
        return statement;
    }

    private String removeSpaces(String str) {
        str = str.replaceAll("\\s+", "");
        return str;
    }

}
