package bubble;

import java.util.*;
import java.io.*;
import javax.swing.JOptionPane;

public class API {
   HashMap<String, Data> val;
   
   public API() {
      val = new HashMap<String, Data>();
      val.put("curr", null);
        
   }
   public void ASK(String[] array, String[] des, String lineNum)
   {
      //ASK (iWORD (USER, GRAPHIC, NOTHING)) dType (INT, ETC.) (STRING ("INTEGER: "))
      String d1 = "";
      if (Arrays.asList(des).contains("value")) {
         int i = Arrays.asList(des).indexOf("value");
         d1 = array[i].replaceAll("\"","");
      }
      String askWho = "scanner";
      for (int i=0; i<array.length; i++) {
         if (array[i].equalsIgnoreCase("graphic") || array[i].equalsIgnoreCase("graphics"))
            askWho = "joption";
      }
      int dTypeInd = Arrays.asList(des).indexOf("dType");
      if (dTypeInd == -1) {
         JOptionPane.showMessageDialog(null, "Error On Line " + (Integer.parseInt(lineNum.charAt(0)+"")+1) + ": Please Give the DataType Required for Input.");
         System.exit(0);
      }
      String dType = array[dTypeInd].toUpperCase();

      if (askWho.equals("joption")) {
         String input = JOptionPane.showInputDialog(d1);
         if (dType.equals("NUMBER") || dType.equals("INT") || dType.equals("INTEGER")) {
            Data data = new Data(Integer.parseInt(input));
            val.put("curr", data);
         }
         else if (dType.equals("DECIMAL") || dType.equals("DOUBLE")) {
            Data data = new Data(Double.parseDouble(input));
            val.put("curr", data);
         }
         else if (dType.equals("LETTER") || dType.equals("CHAR") || dType.equals("CHARACTER")) {
            Data data = new Data(input.charAt(0));
            val.put("curr", data);
         }
         else if (dType.equals("STRING") || dType.startsWith("WORD") || dType.equals("TEXT")){
            Data data = new Data(input);
            val.put("curr", data);
         }
         else {
            JOptionPane.showMessageDialog(null, "Error On Line " + (Integer.parseInt(lineNum.charAt(0)+"")+1) + ": Please Indicate Proper DataType for Input.");
            System.exit(0);
         }
      }
      else {
         Scanner sc = new Scanner(System.in);
         System.out.println(d1);
         if (dType.equals("NUMBER") || dType.equals("INT") || dType.equals("INTEGER")) {
            Data data = new Data(sc.nextInt());
            val.put("curr", data);
         }
         else if (dType.equals("DECIMAL") || dType.equals("DOUBLE")) {
            Data data = new Data(sc.nextDouble());
            val.put("curr", data);
         }
         else if (dType.equals("LETTER") || dType.equals("CHAR") || dType.equals("CHARACTER")) {
            Data data = new Data(sc.next().charAt(0));
            val.put("curr", data);
         }
         else if (dType.equals("STRING") || dType.startsWith("WORD") || dType.equals("TEXT")){
            Data data = new Data(sc.next());
            val.put("curr", data);
         }
         else {
            JOptionPane.showMessageDialog(null, "Error On Line " + (Integer.parseInt(lineNum.charAt(0)+"")+1) + ": Please Indicate Proper DataType for Input.");
            System.exit(0);
         }
      }

   }
   public void STORE(String[] array, String[] des, String lineNum)
   {
      //STORE (VALUE) VALUE_NAME
      ArrayList<Integer> valInd = new ArrayList<Integer>();
      for (int i=1; i<des.length; i++) {
         if (des[i].equalsIgnoreCase("value"))
            valInd.add(i);
      }
      if (valInd.size() == 0) {
         JOptionPane.showMessageDialog(null, "Error On Line " + (Integer.parseInt(lineNum.charAt(0)+"")+1) + ": Please Indicate Proper DataType for Stored Value.");
         System.exit(0);         
      }
      else if (valInd.size() == 1) {
         int i = Arrays.asList(des).indexOf("value");
         val.put(array[i].replaceAll("\"",""), val.get("curr"));
      }
      else if (valInd.size() == 2) {
         String a = array[valInd.get(0)].replaceAll("\"","");
         String b = array[valInd.get(1)].replaceAll("\"","");
         val.put(b, val.get(a));
      }
      else {
         JOptionPane.showMessageDialog(null, "Error On Line " + (Integer.parseInt(lineNum.charAt(0)+"")+1) + ": Current Bubble Version Does Not Support Multi-Value Setting.");
         System.exit(0);    
      }
   }
   public void ADD(String[] array, String[] des, String lineNum)
   {
      //ADD Value (,...) AND VALUE

      String sumType = "int";
      Boolean tStr = false;
      ArrayList<Integer> valInd = new ArrayList<Integer>();
      for (int i=1; i<des.length; i++) {
         if (des[i].equalsIgnoreCase("value")) {
            valInd.add(i);
            if (((val.get(array[i].replaceAll("\"", ""))).getType()).equals("string")) 
               tStr = true;
            else if (((val.get(array[i].replaceAll("\"", ""))).getType()).equals("double")) 
               sumType = "double";
         }
      }
      double sumD = 0;
      String sumStr = "";

      for (int i=0; i<valInd.size(); i++) {
         Data t = val.get(array[valInd.get(i)].replaceAll("\"", ""));
         String type = t.getType();
         if (!type.equals("string") && !type.equals("char")) {
            if (type.equals("double"))
               sumD += t.retDouble();
            else if (type.equals("int"))
               sumD += t.retInt();
            else {
               JOptionPane.showMessageDialog(null, "Error On Line " + (Integer.parseInt(lineNum.charAt(0)+"")+1) + ": Current Bubble Version Can Only Add Strings, Integers, Decimals, and Characters.");
               System.exit(0);              
            }
         }
         else {
            String numAdd = "";
            if (sumD != 0) 
               numAdd = sumD + "";
            if (type.equals("string"))
               sumStr += numAdd + t.retStr();
            else
               sumStr += numAdd + t.retChar() + "";
         }
      }

      if (tStr) {
         Data dRet = new Data(sumStr);
         val.put("curr", dRet);
      }
      else {
         if (sumType.equals("int")) {
            Data dInt = new Data((int) sumD);
            val.put("curr", dInt);
         }
         else {
            Data dInt = new Data(sumD);
            val.put("curr", dInt);
         }
      }

   }
   public void IF(String[] array, String[] des, String lineNum)
   {
      //IF Boolean_METHOD THEN METHOD
      int breakInd = 0;
      for (int i=1; i<array.length; i++) {
         if (array[i].equalsIgnoreCase("then")) {
            breakInd = i;
            break;
         }
      }
      String[] bool = Arrays.copyOfRange(array, 1, breakInd);
      String[] boolDes = Arrays.copyOfRange(des, 1, breakInd);
      String[] then = Arrays.copyOfRange(array, breakInd+1, array.length);
      String[] thenDes = Arrays.copyOfRange(des, breakInd+1, array.length);

      if (searchBool(bool, boolDes, lineNum))
         search(then, thenDes, lineNum);
   }
   public void DIVISION(String[] array, String[] des, String lineNum)
   {
      //Value Divide Value
      ArrayList<Integer> valInd = new ArrayList<Integer>();
      for (int i=0; i<des.length; i++) {
         if (des[i].equalsIgnoreCase("value"))
            valInd.add(i);
      }
      double n1;
      if (val.containsKey(array[valInd.get(0)].replaceAll("\"", "")))
         n1 = Double.parseDouble(val.get(array[valInd.get(0)].replaceAll("\"", "")).toString());
      else
         n1 = Double.parseDouble(array[valInd.get(0)].replaceAll("\"", ""));
      double n2;
      if (val.containsKey(array[valInd.get(1)].replaceAll("\"", "")))
         n2 = Double.parseDouble(val.get(array[valInd.get(1)].replaceAll("\"", "")).toString());
      else
         n2 = Double.parseDouble(array[valInd.get(1)].replaceAll("\"", ""));      
      Data d = new Data(n1/n2);
      val.put("curr", d);
   }
   public boolean DIVISIBLE(String[] array, String[] des, String lineNum)
   {
      //Value DIVISIBLE Value
      ArrayList<Integer> valInd = new ArrayList<Integer>();
      for (int i=0; i<des.length; i++) {
         if (des[i].equalsIgnoreCase("value"))
            valInd.add(i);
      }
      double n1;
      if (val.containsKey(array[valInd.get(0)].replaceAll("\"", "")))
         n1 = Double.parseDouble(val.get(array[valInd.get(0)].replaceAll("\"", "")).toString());
      else
         n1 = Double.parseDouble(array[valInd.get(0)].replaceAll("\"", ""));
      double n2;
      if (val.containsKey(array[valInd.get(1)].replaceAll("\"", "")))
         n2 = Double.parseDouble(val.get(array[valInd.get(1)].replaceAll("\"", "")).toString());
      else
         n2 = Double.parseDouble(array[valInd.get(1)].replaceAll("\"", "")); 
      if (n1 % n2 == 0)
         return true;
      return false;
   }
   public void PRINT(String[] array, String[] des, String lineNum)
   {
      //PRINT Value (Graphic)
      int i = Arrays.asList(des).indexOf("value");
      Data d = val.get(array[i].replaceAll("\"", ""));
      if (d == null) {
         JOptionPane.showMessageDialog(null, "Error On Line " + (Integer.parseInt(lineNum.charAt(0)+"")+1) + ": The Value You Are Trying to Print has not been created.");
         System.exit(0);
      }
      else
         System.out.println(d.toString());
   }
   public void EQUAL(String[] array, String[] des, String lineNum)
   {
      //Value EQUAL Value 
   }
   public boolean EQUALS(String[] array, String[] des, String lineNum)
   {
      //Value Equals? Value
      ArrayList<Integer> valInd = new ArrayList<Integer>();
      for (int i=0; i<des.length; i++) {
         if (des[i].equalsIgnoreCase("value"))
            valInd.add(i);
      }
      String n1;
      if (val.containsKey(array[valInd.get(0)].replaceAll("\"", "")))
         n1 = val.get(array[valInd.get(0)].replaceAll("\"", "")).toString();
      else
         n1 = array[valInd.get(0)].replaceAll("\"", "");
      String n2;
      if (val.containsKey(array[valInd.get(1)].replaceAll("\"", "")))
         n2 = val.get(array[valInd.get(1)].replaceAll("\"", "")).toString();
      else
         n2 = array[valInd.get(1)].replaceAll("\"", ""); 
      if (n1.equals(n2))
         return true;
      return false; 
   }
   public void MULTIPLY(String[] array, String[] des, String lineNum) 
   {
      //Value MULTIPLY Value
      ArrayList<Integer> valInd = new ArrayList<Integer>();
      for (int i=0; i<des.length; i++) {
         if (des[i].equalsIgnoreCase("value"))
            valInd.add(i);
      }
      double n1;
      if (val.containsKey(array[valInd.get(0)].replaceAll("\"", "")))
         n1 = Double.parseDouble(val.get(array[valInd.get(0)].replaceAll("\"", "")).toString());
      else
         n1 = Double.parseDouble(array[valInd.get(0)].replaceAll("\"", ""));
      double n2;
      if (val.containsKey(array[valInd.get(1)].replaceAll("\"", "")))
         n2 = Double.parseDouble(val.get(array[valInd.get(1)].replaceAll("\"", "")).toString());
      else
         n2 = Double.parseDouble(array[valInd.get(1)].replaceAll("\"", "")); 
      Data d = new Data(n1*n2);
      val.put("curr", d);  
   }
   public void search(String[] array, String[] des, String lineNum) 
   {
      for (int i=0; i<array.length; i++) {
         if (array[i].toUpperCase().equals("ASK")) {
            ASK(array, des, lineNum);
            break;
         }
         if (array[i].toUpperCase().equals("STORE")) {
            STORE(array, des, lineNum);
            break;
         }
         if (array[i].toUpperCase().equals("ADD") || array[i].equals("+")) {
            ADD(array, des, lineNum);
            break;
         }
         if (array[i].toUpperCase().equals("IF")) {
            IF(array, des, lineNum);
            break;
         }
         if (array[i].toUpperCase().equals("DIVIDE") || array[i].equals("/")) {
            DIVISION(array, des, lineNum);
            break;
         }
         if (array[i].equalsIgnoreCase("*") || array[i].equalsIgnoreCase("MULTIPLY") || array[i].equalsIgnoreCase("TIMES")) {
            MULTIPLY(array, des, lineNum);
            break;
         }
         if (array[i].toUpperCase().equals("PRINT")) {
            PRINT(array, des, lineNum);
            break;
         }
         if (array[i].toUpperCase().equals("EQUAL")) {
            EQUAL(array, des, lineNum);  
            break; 
         }
      }
   }
   public boolean searchBool(String[] array, String[] des, String lineNum) {
      for (int i=0; i<array.length; i++) {
         if (array[i].toUpperCase().equals("REMAINDER") || array[i].toUpperCase().equals("DIVISIBLE")) 
            return DIVISIBLE(array, des, lineNum);
         if (array[i].toUpperCase().equals("EQUAL") || array[i].toUpperCase().equals("EQUALS")) 
            return EQUALS(array, des, lineNum);   
      }   
      return false;   
   }
}