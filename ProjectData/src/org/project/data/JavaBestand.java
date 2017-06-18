package org.project.data;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import org.openide.util.Exceptions;

/**
 *
 * @author Boutsman
 */
public class JavaBestand {

    // Instance variables
    private String index;
    private String naam;
    private String erftVan;
    private String type;
    private String url;
    private Relatie[] relaties;
    private int x = 0;
    private int y = 0;

    //Constructors
    
    /**
     * Default constructor
     */
    public JavaBestand() {

    }

    /**
     * Maakt een nieuw JavaBestand met bepaalde parameters
     *
     * @param index
     * @param naam
     * @param x
     * @param y
     * @param type
     */
    public JavaBestand(String index, String naam, int x, int y, String type) {
        this.index = index;
        this.naam = naam;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     * Maakt een nieuw JavaBestand met bepaalde parameters
     *
     * @param index
     * @param naam
     * @param x
     * @param y
     * @param type
     * @param url
     */
    public JavaBestand(String index, String naam, int x, int y, String type, String url) {
        this.index = index;
        this.naam = naam;
        this.x = x;
        this.y = y;
        this.type = type;
        this.url = url;
    }

    // Getters & setters
    /**
     *
     * @param index
     */
    public void setIndex(String index) {
        this.index = index;
    }

    /**
     *
     * @return
     */
    public String getIndex() {
        return index;
    }

    /**
     *
     * @return
     */
    public String getErftVan() {
        return erftVan;
    }

    /**
     *
     * @param erftVan
     */
    public void setErftVan(String erftVan) {
        this.erftVan = erftVan;
    }

    /**
     *
     * @return
     */
    public String getNaam() {
        return naam;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @param naam
     */
    public void setNaam(String naam) {
        this.naam = naam;
    }

    /**
     *
     * @return
     */
    public Relatie[] getRelaties() {
        return relaties;
    }

    /**
     *
     * @param relaties
     */
    public void setRelaties(Relatie[] relaties) {
        this.relaties = relaties;
    }

    /**
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Stel de coordinaten in van de klasseNode in het klassediagram
     *
     * @param x
     * @param y
     */
    public void setCoordinaten(int x, int y) {
        setX(x);
        setY(y);
    }

    /**
     * Bepaal het type van het javaBestand
     */
    public void bepaalType() {
        int modifiers = laadKlasse().getModifiers();
        if (Modifier.isAbstract(modifiers)) {
            type = "AbstractTarget";
        } else if (Modifier.isInterface(modifiers)) {
            type = "InterfaceTarget";
        } else {
            type = "ClassTarget";
        }
    }

    @Override
    public String toString() {
        return "Index:" + index + "Klassenaam: " + naam + " - x coördinaat: " + x + " - y coördinaat: " + y + " - type " + type + " - fileDir: " + url;
    }

    /**
     * Laadt de klasse van het Java bestand
     *
     * @return
     */
    public Class laadKlasse() {
        //System.out.println("***LaadKlasse");
        Class myClass = null;
        try {            
            String lookupPath = url.replace(naam + ".java", "");
            String pad = naam;
            //System.out.println("***lookupPath: " + lookupPath);
            //System.out.println("***naam: " + naam);

            URL urlFile = new File(lookupPath).toURL();
            //System.out.println(urlFile.toString());
            URL[] urls = new URL[]{urlFile};
            //System.out.println(Arrays.toString(urls));
            ClassLoader myLoader = new URLClassLoader(urls);
            //System.out.println(myLoader);
            myClass = myLoader.loadClass(pad);
            //System.out.println("***Klasse geladen " + myClass.getName());
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
            System.err.println("URL is verkeerd");
            return null;
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
            System.err.println("***Klasse niet geladen");
            return null;
        }
        return myClass;
    }    
}
