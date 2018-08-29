//Author: Siddhartha Edara
//Date: 11/2/16
//Project: Bubble v1
//Course: Computer Systems Research

import bubble.API;

import java.util.*;
import java.io.*;
import java.util.regex.*;
import javax.swing.JOptionPane;

public class Bubble
{
   private static final String[] operSymbol = {"(", ")"};
   private static final String[] symbol = {"[", "]"};
   private static final String[] iWords = {"AND", "IS", "SCREEN", "THEN", "TO", "USER", "GRAPHIC", "GRAPHICS"};
   private static final String[] dataTypes = {"INT", "INTEGER"};
   //Should "IS" be included in methods OR iWords???
   private static final String[] methods = {"ADD", "ASK", "DIVIDE", "DIVIDED", "DIVISIBLE", "EQUAL", "IF", "PRINT", "STORE", "/", "*", "MULTIPLICATION", "MULTIPLY", "TIMES"};
   
   public static boolean isValue(String str)
   {
      try { 
         Integer.parseInt(str); 
      } 
      catch(Exception e) { 
         try {
            Double.parseDouble(str);
         }
         catch (Exception e2) {
            if (str.toUpperCase().equals("TRUE") || str.toUpperCase().equals("FALSE"))
               return true;
            return false; 
         }
      }
      return true;
   }
   public static String[] getDescription(String[] array)
   {
      String[] out = new String[array.length];
      for (int i=0; i<array.length; i++) {
         if (Arrays.asList(methods).contains(array[i].toUpperCase()))
            out[i] = "method";
         else if (Arrays.asList(dataTypes).contains(array[i].toUpperCase()))
            out[i] = "dType";
         else if (Arrays.asList(iWords).contains(array[i].toUpperCase()))
            out[i] = "iWord";
         else if (Arrays.asList(symbol).contains(array[i].toUpperCase()))
            out[i] = "symbol";
         else if (array[i].indexOf("\"") != -1 || array[i].indexOf("\'") != -1 || isValue(array[i]))
            out[i] = "value";
         else if (Arrays.asList(operSymbol).contains(array[i].toUpperCase()))
            out[i] = "operation";
         else
            out[i] = "idk";   
      }
      return out;
   }
   public static void removePunc(String[] array)
   {
      for (int i=0; i<array.length; i++) {
         array[i] = array[i].replaceAll("\\.|,|!|;|:|_|`|~\\G", "");
      }
   }
   public static List<String[]> getMethods(String[] array) {
      ArrayList<String[]> list = new ArrayList<String[]>();
      
      String tmp = "";
      for (int i=0; i<array.length; i++) {
         if ((array[i].toUpperCase().equals("AND")) 
            || (array[i].toUpperCase().equals("THEN") && ((tmp.toUpperCase()).indexOf("IF") == -1 && (tmp.toUpperCase()).indexOf("THEN") == -1))
            || i == array.length-1) {
            if (array[i].toUpperCase().equals("AND") && (tmp.toUpperCase()).indexOf("ADD") != -1 && (tmp.toUpperCase()).indexOf("AND") == -1)
               tmp+=array[i]+",";
            else {
               if (i == array.length-1)
                  tmp+=array[i]+",";
               tmp = tmp.substring(0, tmp.length()-1);
               String[] parts = tmp.split(",");
               list.add(parts);  
               tmp = "";    
            }
         }
         else
            tmp+=array[i]+",";
      }
      return list;
   }
   public static void main(String[] args) throws Exception
   {
      HashMap<Integer, String[]> map = new HashMap<Integer, String[]>();
      
      String fileName = JOptionPane.showInputDialog("Enter the filename of your BubbleCode: \n(It should be stored in the folder named Text)");
      
      File inFile = new File ("Text/" + fileName);
   
      Scanner sc = new Scanner (inFile);
      
      int lineNum = 0;
      while (sc.hasNextLine())
      {
         String line = sc.nextLine();
         String[] split = line.split("\\s+");
         removePunc(split);
         map.put(lineNum, split);
         lineNum++;
      }
      sc.close();
      
      HashMap<Integer, List<String[]>> code = new HashMap<Integer, List<String[]>>();
      
      for (Map.Entry<Integer, String[]> entry : map.entrySet()) {
         int key = entry.getKey();
         String[] value = entry.getValue();
         List<String[]> methods = getMethods(value);
         code.put(key, methods);
      }
      
      API api = new API();
      
      for (Map.Entry<Integer, List<String[]>> entry : code.entrySet()) {
          int key = entry.getKey();
          List<String[]> value = entry.getValue();
          for (int i=0; i<value.size(); i++) {
            String[] tmp = value.get(i);
            String lineInd = key+","+i;
            api.search(tmp, getDescription(tmp), lineInd);
          }          
      } 
     /* 
      for (Map.Entry<Integer, List<String[]>> entry : code.entrySet()) {
         int key = entry.getKey();
         List<String[]> value = entry.getValue();
         System.out.println("Line " + key + ": ");
         for (int i=0; i<value.size(); i++) {
            System.out.print("\t"+i+": ");
            String[] tmp = value.get(i);
            for (int j=0; j<tmp.length; j++) {
               System.out.print(tmp[j] + " ");
            }
            System.out.println();
            String[] out = getDescription(tmp);
            System.out.print("\tDescription: ");
            for (int j=0; j<out.length; j++) {
               System.out.print(out[j] + " ");
            }
            System.out.println();
         }          
         System.out.println();
      }  */
   }
}
