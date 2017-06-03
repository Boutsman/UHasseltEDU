package org.project.data;

/**
 * @author Boutsman
 * 
 * Deze klasse slaat de informatie van een relatie op.
 */
public class Relatie {
    //Instance variables
    private String index;
    private String type;
    private JavaBestand oorsprong;
    private JavaBestand bestemming;

    // Constructors
    /**
     *  default constructor
     */
    public Relatie(){}
    
    /**
     * Constructor die een object maakt met bepaalde parameters
     * 
     * @param index de index van de relatie
     * @param oorsprong de oorsprong van de relatie
     * @param bestemming de bestemming van de relatie
     * @param type het type van de relatie
     */
    public Relatie(String index, JavaBestand oorsprong, JavaBestand bestemming, String type) {
        this.index = index;
        this.oorsprong = oorsprong;
        this.bestemming = bestemming;
        this.type = type;
    }

    /**
     * Vraag de index op.
     * 
     * @return de index van de relatie
     */
    public String getIndex() {
        return index;
    }

    /**
     * Stel de index in.
     * 
     * @param index de index van de relatie
     */
    public void setIndex(String index) {
        this.index = index;
    }

    /**
     * Vraag de oorsprong op.
     * 
     * @return de oorsprong van de relatie
     */
    public JavaBestand getOorsprong() {
        return oorsprong;
    }

    /**
     * Stel de oorsprong in.
     * @param oorsprong de oorsprong van de relatie
     */
    public void setOorsprong(JavaBestand oorsprong) {
        this.oorsprong = oorsprong;
    }

    /**
     * vraag de bestemming op.
     * 
     * @return de bestemming van de relatie
     */
    public JavaBestand getBestemming() {
        return bestemming;
    }

    /**
     * Stel de bestemming in.
     * 
     * @param bestemming de naam van de bestemming
     */
    public void setBestemming(JavaBestand bestemming) {
        this.bestemming = bestemming;
    }

    /**
     *Vraag het type van de relatie op.
     * De mogelijke types zijn:
     * Een hasA relatie of een isA relatie.
     * 
     * @return het type van de relatie
     */
    public String getType() {
        return type;
    }

    /**
     * Stel het type van de relatie in.
     * 
     * @param type het type van de relatie
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Index: " + index + " - Oorsprong: " + oorsprong.getNaam() + " - Bestemming: " + bestemming.getNaam() + " - Type: " + type; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }

}
