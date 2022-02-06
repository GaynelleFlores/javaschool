package com.tsystems.javaschool.tasks.calculator;

public class Calculator {
    public String evaluate(String statement) {
        float res;
        if (statement == null)
            return null;
        statement = removeSpaces(statement);
        if (!checkString(statement)) {
            return null;
        }
        while (!isResult(statement)) {
            statement = findBrackets(statement);
            if (statement == null)
                return  null;
        }
        res = Float.parseFloat(statement);
        res = round(res, 4);
        statement = Float.toString(res);
        statement = removeZero(statement);
        return statement;
    }
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
        float res;
        int end = findEndOfNumber(currBrackets, mathSymbol);
        try {
            res = Float.parseFloat(currBrackets.substring(mathSymbol + 1, end));
        } catch (Exception e) {
            return null;
        }
        return res;
    }
    private Float findNumberBefore(String currBrackets, int mathSymbol) {
        float res;

        int begin = findBeginOfNumber(currBrackets, mathSymbol);
        try {
            res = Float.parseFloat(currBrackets.substring(begin, mathSymbol));
        } catch (Exception e) {
            return null;
        }
        return res;
    }
    private String replaceSubstring(String str, String substr, int begin, int end) {
        String remove = str.substring(begin, end);
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
        float res;
        int mathSymbol;

        mathSymbol = currBrackets.indexOf(chooseMathSymbol(currBrackets, '*', '/'));
        num1 = findNumberBefore(currBrackets, mathSymbol);
        num2 = findNumberAfter(currBrackets, mathSymbol);
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
        currBrackets = replaceSubstring(currBrackets, Float.toString(res), findBeginOfNumber(currBrackets, mathSymbol), findEndOfNumber(currBrackets, mathSymbol));
        return currBrackets;
    }
    private String calculate(String currBrackets) {
        while(currBrackets.contains("*") || currBrackets.contains("/")) {
            currBrackets = multiplyOrDivide(currBrackets);
            if (currBrackets == null) {
                return null;
            }
        }
        while(!isResult(currBrackets)) {
            currBrackets = addOrSubtract(currBrackets);
            if (currBrackets == null) {
                return null;
            }
        }
        return currBrackets;
    }
    private String addOrSubtract(String currBrackets) {
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
        num1 = findNumberBefore(currBrackets, mathSymbol);
        num2 = findNumberAfter(currBrackets, mathSymbol);
        if (num1 == null || num2 == null)
            return null;
        if (currBrackets.charAt(mathSymbol) == '+') {
            res = num1 + num2;
        } else {
            res = num1 - num2;
        }
        currBrackets = replaceSubstring(currBrackets, res.toString(), findBeginOfNumber(currBrackets, mathSymbol), findEndOfNumber(currBrackets, mathSymbol));
        return currBrackets;
    }

    private boolean isResult(String str) {
        return str.matches("-?[\\d]+\\.?[\\d]*");
    }
    private String findBrackets(String currBrackets){
        if (!currBrackets.contains("(")) {
            if (!currBrackets.contains(")")) {
                currBrackets = calculate(currBrackets);
                return currBrackets;
            }
            return null;
        }
        String sub;
        if (currBrackets.contains(")")) {
            int index1 = currBrackets.lastIndexOf("(");
            int index2 = currBrackets.indexOf(")");
            if ((index2 - index1) <= 1)
                return null;
            sub = currBrackets.substring(index1 + 1, index2);
            String res;
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
        return null;
    }
    private String removeZero(String statement) {
        if (statement.contains(".0")) {
            statement = statement.replace(".0", "");
        }
        return statement;
    }
    private static float round(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }
    private String removeSpaces(String str) {
        str = str.replaceAll("\\s+", "");
        return str;
    }
}
