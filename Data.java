package bubble;

import java.util.*;
import java.io.*;

public class Data {
   Boolean b = null; 
   Integer i = null;
   Double d = null;
   Character c = null;
   String s = null;
   String type = "";    
   
   public String getType()
   {
      return type;
   }
   public String toString() {
      if (type.equals("boolean")) {
         if (b)
            return "true";
         return "false";
      }
      else if (type.equals("int"))
         return i + "";
      else if (type.equals("double"))
         return d + "";
      else if (type.equals("char"))
         return c + "";
      else 
         return s;
   }
   public boolean retBool()
   {
      return b;
   }
   public int retInt()
   {
      return i;
   }
   public double retDouble()
   {
      return d;
   }
   public char retChar()
   {
      return c;
   }
   public String retStr()
   {
      return s;
   }
   public Data (boolean bool)
   {
      type = "boolean";
      b = bool;
   }
   public Data (int inte)
   {
      type = "int";
      i = inte;
   }
   public Data (double doub)
   {
      type = "double";
      d = doub;
   }
   public Data (char ch)
   {
      type = "char";
      c = ch;
   }
   public Data(String str)
   {
      type = "string";
      s = str;
   }
   /*public Data (Data[] dat)
   {
   }
   public Data (ArrayList<Data> dat)
   {
   }*/
}
