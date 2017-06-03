package org.project.data;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author Boutsman
 * @version 0.5
 */
public class Reflection {
    
    public Reflection(){

    }

    //Methodes uit te voeren op klassen
    
    /**
     * Get the class name from a Class
     * @param mClass the class whose name you want
     * @return name the Class name
     */
    public String getClassName(Class mClass) {
        //Vraag de naam van een klasse op
        String name = mClass.getName();
        //Geef de naam terug als string
        return name;
    }
    
    /**
     * Get the class name from a Class
     * @param mClass the class whose simple name you want
     * @return name the simple Class name
     */
    public String getClassSimpleName(Class mClass) {
        //Vraag de korte naam van een klasse op
        String name = mClass.getSimpleName();
        //Geef de naam terug als string
        return name;
    }
    
    /**
     * Get all the constructors from a certain Class
     * @param xClass The Class whose constructors you want to get
     * @return str the constructors of the Class
     */
    public Constructor[] getAlleConstructors(Class xClass){
        Constructor[] c = xClass.getDeclaredConstructors();
        return c;
    }
    
    /**
     * Create object from Class instance
     * 
     * Niet gebruikt, gebruik createInstance in de plaats
     * 
     * @param klasse
     * @return 
     * @deprecated 
     */
    public Object makeObject(Class klasse) {
        //create instance of "Class"
        Class<?> c = null;
        try {
            //c = Class.forName(className);
            c = klasse;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //create instance of "Mens"
        Object obj = null;

        try {
            obj = c.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    /**
     * Get the constructors from a Class and create the Class
     * @param klasse the Class that you want to create
     * @param constrNr
     * @return m1 the Object made from the Class
     */
    public Object createInstance(Class klasse, int constrNr) {
        /*
        //create instance of "Class"
        Class<?> c = null;
        try {
            c = Class.forName(klasse.getName());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Geen klasse aanwezig met naam: " + klasse.getName());
        }*/

        //create instance of "Mens"
        Object m1 = null;

        //get all constructors
        Constructor<?> cons[] = klasse.getConstructors();

        try {
            m1 = (Object) cons[constrNr].newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return m1;
    }
    
    /**
     * Deze methode converteerd een Stringwaarde naar het gevraagde type
     * 
     * @param targetType Het type waarnaar de String geconverteerd wordt.
     * @param text De String die naar een type moet geparsed worden.
     * @return De Stringdata als gevraagd datatype
     */
    private static Object convert(Class<?> targetType, String textWaarde) {
        PropertyEditor editor = PropertyEditorManager.findEditor(targetType);
        editor.setAsText(textWaarde);
        return editor.getValue();
    }
    
    /**
     * Hier probeer ik de vorige methode te hervormen zodat:
     * We de constructor kunnen kiezen
     * De parameters kunnen toevoegen indien nodig
     * @param klasse
     * @param param1
     * @param param2
     * @param param3
     * @return 
     */    
    public Object createInstance2(Class klasse, int constrNr, Object... param) {
        //create instance of "Class"
        Class<?> c = null;
        try {
            c = Class.forName(klasse.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //create instance of an object
        Object obj = null;

        //get all constructors
        Constructor<?> cons[] = c.getConstructors();
        
        try {
            obj = (Object) cons[constrNr].newInstance(param);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }
    
    /**
     *
     * @param klasse de klasse waarvan we de dataMembers willen weten.
     * @return de dataMembers van deze klasse
     */
    public Field[] getClassDeclaredFields(Class klasse){
        //gebruik van getDeclaredFields ipv getMethods zodat ook de private dataMembers getoond worden
        Field[] dataMembers = klasse.getDeclaredFields();
        return dataMembers;
    }
    
    /**
     *
     * @param klasse de klasse waarvan we de dataMembers willen weten.
     * @return de dataMembers van deze klasse
     */
    public ArrayList<String> getClassDeclaredFieldsName(Class klasse){
        //gebruik van getDeclaredFields ipv getMethods zodat ook de private dataMembers getoond worden
        Field[] dataMembers = klasse.getDeclaredFields();
        ArrayList<String> dataMembersNames = new ArrayList<String>();
        for(Field member:dataMembers){
            dataMembersNames.add(member.getType().getName());
            System.out.println(member.getType());
        }
        System.out.println(dataMembersNames.toString());
        return dataMembersNames;
    }
    
    /**
     *
     * @param klasse de klasse waarvan we de dataMembers willen weten.
     * @return de dataMembers van deze klasse
     */
    public ArrayList<String> getClassDeclaredFieldsSimpleName(Class klasse){
        //gebruik van getDeclaredFields ipv getMethods zodat ook de private dataMembers getoond worden
        Field[] dataMembers = klasse.getDeclaredFields();
        ArrayList<String> dataMembersNames = new ArrayList<String>();
        for(Field member:dataMembers){
            dataMembersNames.add(member.getType().getSimpleName());
        }
        //System.out.println(dataMembersNames.toString());
        return dataMembersNames;
    }
    
    //Methodes uit te voeren op objecten
    
    /**
     *
     * @param obj
     * @return
     */        
    public Class getObjClass(Object obj) {
        //Klasse opvragen van een object.
        Class mClass = obj.getClass();
        return mClass;
    }
    
    /**
     * Get the  class name from an object
     * and print the result
     * @param obj an object whose Class name you want to know
     * @return name a string with the Class name of the object
     */
    public String getObjClassName(Object obj) {
        //Klasse opvragen van een object.
        //Class mClass = obj.getClass();
        //Naam opvragen van een object
        String name = obj.getClass().getName();
        //Eenvoudige naam vragen van een object
        //String simpleName = obj.getClass().getSimpleName();
        //Geef de naam terug als string
        return name;
    }
    
    /**
     * Get the  class name from an object
     * and print the result
     * @param obj an object whose Class name you want to know
     * @return name a string with the simple Class name of the object
     */
    public String getObjClassSimpleName(Object obj) {
        //Klasse opvragen van een object.
        //Class mClass = obj.getClass();
        //Naam opvragen van een object
        String name = obj.getClass().getSimpleName();
        //Eenvoudige naam vragen van een object
        //String simpleName = obj.getClass().getSimpleName();
        //Geef de naam terug als string
        return name;
    }                            
    
    /**
     * Get all the methods from a certain Class
     * @param obj the Object whose methods you want to get
     * @return a string with the methods of the Class
     */
    public Method[] getAlleMethodes(Object obj){
        Method[] methods = obj.getClass().getDeclaredMethods();
        return methods;
    }
    /*
    public String getAlleMethodes(Object obj){
        StringBuilder sb = new StringBuilder();
        List result = null;
        Method[] methods = obj.getClass().getMethods();
        int index = 1;
        for (Method method : methods) {
            sb.append(method.toString()).append("\n");
            index++;
            // System.out.println(method.toString());
        }
        String str = sb.toString();
        //System.out.println(result);
        return str;
    }*/
   
    /**
     * Invoke method on unknown object
     * @param m the object that executes the method
     * @param methodName the name of the method that must be executed
     */
    public void invokeMethod(Object m, String methodName) {
        Method method;
        try {
            method = m.getClass().getDeclaredMethod(methodName, new Class<?>[0]);
            method.invoke(m);            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     *
     * @param obj het object waarvan we de dataMembers willen weten.
     * @returncde dataMembers van deze klasse
     */
    public Field[] getObjFields(Object obj){
        //gebruik van getDeclaedMethods ipv getMethods zodat ook de private dataMembers getoond worden
        Field[] dataMembers = obj.getClass().getDeclaredFields();
        return dataMembers;
    }
}