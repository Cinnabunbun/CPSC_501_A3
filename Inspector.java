import javax.lang.model.util.ElementScanner6;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

/**
 * CPSC 501
 * Inspector starter class
 *
 * @author Jonathan Hudson
 */
public class Inspector {

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {

      //string formatting for proper indentation
      String indent = "";
      for(int i = 0; i < depth; i++)
      {
        indent = indent + "        ";
      }
      String format = indent + "%s%n";

      //printing class name
      System.out.printf(format, "CLASS");
      System.out.printf(format, "Class: " + c.getName());

      if(c.isArray())
      {
        printBaseArrayInformation(c, obj, recursive, depth, format);
      }
      else
      {
        //printing superclass information
        Class superclass = c.getSuperclass();
        if(superclass == null)
        {
          System.out.printf(format, "Superclass: NONE");
        }
        else
        {
          System.out.printf(format, " SUPERCLASS -> Recursively Inspect");
          System.out.printf(format, " Superclass: " + superclass.getName());
          inspectClass(superclass, obj, recursive, depth + 1);
        }

        //printing interface information
        System.out.printf(format, "INTERFACES(" + c.getName() + ")");
        Class[] interfaces = c.getInterfaces();
        if(interfaces.length == 0)
        {
          System.out.printf(format, "Interfaces-> NONE");
        }
        else
        {
          System.out.printf(format, "Interfaces->");
          for(Class i : interfaces)
          {
            System.out.printf(format, " INTERFACE -> Recursively Inspect");
            System.out.printf(format, " Interface: " + i.getName());
            inspectClass(i, null, recursive, depth + 1);
          }
        }

        //printing constructor information
        System.out.printf(format, "CONSTRUCTORS(" + c.getName() + ")");
        Constructor[] constructors = c.getDeclaredConstructors();
        if(constructors.length == 0)
        {
          System.out.printf(format, "Constructors-> NONE");
        }
        else
        {
          System.out.printf(format, "Constructors->");
          for(Constructor con : constructors)
          {
            System.out.printf(format, " CONSTRUCTOR");
            System.out.printf(format, "  Name: " + con.getName());

            Class[] exceptions = con.getExceptionTypes();
            if(exceptions.length == 0)
            {
              System.out.printf(format, "  Exceptions-> NONE");
            }
            else
            {
              System.out.printf(format, "  Exceptions->");
              for(Class e : exceptions)
              {
                System.out.printf(format, "  " + e.toString());
              }
            }

            Class [] parameterTypes = con.getParameterTypes();
            if(parameterTypes.length == 0)
            {
              System.out.printf(format, "  Parameter types-> NONE");
            }
            else
            {
              System.out.printf(format, "  Parameter types->");
              for(Class p : parameterTypes)
              {
                if(p.isPrimitive())
                {
                  System.out.printf(format, "  " + p.getName());
                }
                else
                {
                  System.out.printf(format, "  class " + p.getName());
                }
              }
            }

            System.out.printf(format, "  Modifiers: " + Modifier.toString(con.getModifiers()));
          }
        }

        //printing method information
        System.out.printf(format, "METHODS(" + c.getName() + ")");
        Method[] methods = c.getDeclaredMethods();
        if(methods.length == 0)
        {
          System.out.printf(format, "Methods-> NONE");
        }
        else
        {
          System.out.printf(format, "Methods->");
          for(Method m : methods)
          {
            System.out.printf(format, " METHOD");
            System.out.printf(format, "  Name: " + m.getName());

            Class[] exceptions = m.getExceptionTypes();
            if(exceptions.length == 0)
            {
              System.out.printf(format, "  Exceptions-> NONE");
            }
            else
            {
              System.out.printf(format, "  Exceptions->");
              for(Class e : exceptions)
              {
                System.out.printf(format, "  " + e.toString());
              }
            }

            Class [] parameterTypes = m.getParameterTypes();
            if(parameterTypes.length == 0)
            {
              System.out.printf(format, "  Parameter types-> NONE");
            }
            else
            {
              System.out.printf(format, "  Parameter types->");
              for(Class p : parameterTypes)
              {
                if(p.isPrimitive())
                {
                  System.out.printf(format, "  " + p.getName());
                }
                else
                {
                  System.out.printf(format, "  class " + p.getName());
                }
              }
            }

            Class rtype = m.getReturnType();

            if(rtype.isPrimitive())
            {
              System.out.printf(format, "  Return type: " + rtype.getName());
            }
            else
            {
              System.out.printf(format, "  Return type: class " + rtype.getName());
            }

            System.out.printf(format, "  Modifiers: " + Modifier.toString(m.getModifiers()));
          }
        }

        //printing field information
        System.out.printf(format, "FIELDS(" + c.getName() + ")");
        Field[] fields = c.getDeclaredFields();
        if(fields.length == 0)
        {
          System.out.printf(format, "Fields-> NONE");
        }
        else
        {
          System.out.printf(format, "Fields->");
          for(Field f : fields)
          {
            System.out.printf(format, " FIELD");
            System.out.printf(format, "  Name: " + f.getName());

            Class ftype = f.getType();

            if(ftype.isPrimitive())
            {
              System.out.printf(format, "  Type: " + ftype.getName());
            }
            else
            {
              System.out.printf(format, "  Type: class " + ftype.getName());
            }

            System.out.printf(format, "  Modifiers: " + Modifier.toString(f.getModifiers()));

            f.setAccessible(true);

            Object o = null;

            try
            {
              o = f.get(obj);
            } catch(Exception e)
            {
              e.printStackTrace();
            }

            if(o == null)
            {
              System.out.printf(format, "  Value: null");
            }
            else if(isPrimitive(o.getClass()))
            {
              System.out.printf(format, "  Value: " + o);
            }
            else if(o.getClass().isArray())
            {
              //printing array information
              Class arrayType = o.getClass().getComponentType();
              System.out.printf(format, "  Component Type: " + arrayType);
              System.out.printf(format, "  Length: " + Array.getLength(o));
              System.out.printf(format, "  Entries->");

              for(int i = 0; i < Array.getLength(o); i++)
              {
                Object element = Array.get(o, i);

                if(element == null)
                {
                  System.out.printf(format, "   Value: null");
                }
                else if(isPrimitive(element.getClass()))
                {
                  System.out.printf(format, "   Value: " + element);
                }
                else
                {
                  System.out.printf(format, "   Value (ref): " + element + "@" + element.hashCode());

                  if(recursive)
                  {
                    System.out.printf(format, "    ->Recursively Inspect ");
                    inspectClass(element.getClass(), element, recursive, depth + 1);
                  }
                }
              }
            }
            else
            {
              System.out.printf(format, "  Value (ref): " + o + "@" + o.hashCode());
              if(recursive)
              {
              System.out.printf(format, "   ->Recursively Inspect ");
              inspectClass(o.getClass(), o, recursive, depth + 1);
              }
            }
          }
        }
      }
    }

    // Method that determines whether a given class represents a primitive type.
    public boolean isPrimitive(Class<?> c)
    {
      return c.equals(Boolean.class) || 
        c.equals(Integer.class) ||
        c.equals(Character.class) ||
        c.equals(Byte.class) ||
        c.equals(Short.class) ||
        c.equals(Double.class) ||
        c.equals(Long.class) ||
        c.equals(Float.class);
    }

    public void printBaseArrayInformation(Class c, Object obj, boolean recursive, int depth, String format)
    {
      //printing array information
      Class arrayType = c.getComponentType();
      System.out.printf(format, " Type name: " + arrayType.getName() + "[]");
      System.out.printf(format, " Component Type: " + arrayType);
      System.out.printf(format, " Length: " + Array.getLength(obj));
      System.out.printf(format, " Entries->");

      for(int i = 0; i < Array.getLength(obj); i++)
      {
        Object element = Array.get(obj, i);

        if(element == null)
        {
          System.out.printf(format, "  Value: null");
        }
        else if(isPrimitive(element.getClass()))
        {
          System.out.printf(format, "  Value: " + element);
        }
        else
        {
          System.out.printf(format, "  Value (ref): " + element + "@" + element.hashCode());

          if(recursive)
          {
            System.out.printf(format, "   ->Recursively Inspect ");
            inspectClass(element.getClass(), element, recursive, depth + 1);
          }
        }
      }
    }

}
