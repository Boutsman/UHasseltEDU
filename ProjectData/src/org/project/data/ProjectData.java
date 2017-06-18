package org.project.data;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Deze klasse houdt de informatie bij die nodig is om een java project te
 * visualiseren.
 *
 * @author Stijn Boutsen
 */
public class ProjectData {

    // Instance variables
    private String projectDir;
    private JavaBestand[] javaBestanden;
    private Relatie[] relaties;

    // Constructors
    /**
     * Default constructor
     */
    public ProjectData() {

    }

    //Getters en setters
    public String getProjectDir() {
        return projectDir;
    }

    public void setProjectDir(String projectDir) {
        this.projectDir = projectDir;
    }

    /**
     * Vraag de JavaBestanden op.
     *
     * @return
     */
    public JavaBestand[] getJavaBestanden() {
        return javaBestanden;
    }

    /**
     * Vraag een bepaald JavaBestand op.
     *
     * @param hetBestand het bestand dat we zoeken
     * @return het javaBestand
     */
    public JavaBestand getJavaBestand(String hetBestand) {
        JavaBestand temp = null;
        for (JavaBestand j : javaBestanden) {
            if (j.getNaam().equals(hetBestand)) {
                temp = j;
            }
        }
        return temp;
    }

    /**
     * Stel de JavaBestanden in.
     *
     * @param javaBestanden
     */
    public void setJavaBestanden(JavaBestand[] javaBestanden) {
        this.javaBestanden = javaBestanden;
    }

    /**
     * Vraag de relaties op.
     *
     * @return
     */
    public Relatie[] getRelaties() {
        return relaties;
    }

    /**
     * Stel de relaties in.
     *
     * @param relaties
     */
    public void setRelaties(Relatie[] relaties) {
        this.relaties = relaties;
    }

    // Methodes
    /**
     * Deze methode voegt een JavaBestand toe aan de opgeslagen Array
     *
     * @param bestand
     */
    public void voegJavaBestandToe(JavaBestand bestand) {
        if (javaBestanden != null) {
            javaBestanden[javaBestanden.length] = bestand;
        } else {
            javaBestanden[0] = bestand;
        }
    }

    public int getAantalBestanden() {
        return javaBestanden.length;
    }

    /**
     * Deze methode verwerkt de data die is opgeslagen in het package.bluej
     * bestand en slaat de bestanden en relaties op
     *
     * @param dir de locatie van het package.bluej bestand
     */
    public void verwerkBlueJPackageFile(String dir) {
        ArrayList<String> bestandsNaam = new ArrayList<String>();
        ArrayList<String> xCoordnaat = new ArrayList<String>();
        ArrayList<String> yCoordinaat = new ArrayList<String>();
        ArrayList<String> type = new ArrayList<String>();
        ArrayList<String> oorsprong = new ArrayList<String>();
        ArrayList<String> bestemming = new ArrayList<String>();
        ArrayList<String> depIndex = new ArrayList<String>();
        ArrayList<String> tarIndex = new ArrayList<String>();
        // Geef de locatie van het bestand
        String file_name = dir;
        // Probeer het bestand te lezen
        try {
            // Lees het bestand in
            ReadFile file = new ReadFile(file_name);
            // Sla elke regel op in een array van strings
            String[] allLines = file.OpenFile();

            // Controle of de eerste regel van het bestand niet fout is
            if (!allLines[0].equals("#BlueJ package file")) {
                System.err.println("Bestand is geen correct package.bluej bestand");
            } else {
                // Verwerking van resterende regels
                for (int i = 1; i < allLines.length; i++) {
                    // Plaats een regel in een string
                    // Iedere regel is volgens het formaat what.doWhat.param=val
                    String str = allLines[i];
                    //System.out.println(str);
                    String[] temp = str.split("=");
                    //System.out.println(Arrays.toString(temp) + "    " + temp.length);
                    if (temp.length == 2) {
                        String parameterNaam = temp[0];
                        String waarde = temp[1];
                        // Splits de parameternaam in naam en type
                        String[] paramNaamBits = parameterNaam.split("\\.");

                        // Zoek de relaties op tussen de bestanden en sla oorsprong en bestemming op
                        if (paramNaamBits[0].contains("dependency")) {
                            if (paramNaamBits[1].contains("from")) {
                                depIndex.add(paramNaamBits[0].replaceFirst("dependency", ""));

                                oorsprong.add(waarde);
                            } else if (paramNaamBits[1].contains("to")) {
                                bestemming.add(waarde);
                            }
                        }

                        // Zoek de java klassen en sla de naam, de coördinaten en het type op
                        if (paramNaamBits[0].contains("target")) {
                            if (paramNaamBits[1].contains("name")) {
                                tarIndex.add(paramNaamBits[0].replaceFirst("target", ""));
                                bestandsNaam.add(waarde);
                            } else if (paramNaamBits[1].equals("x")) {
                                xCoordnaat.add(waarde);
                            } else if (paramNaamBits[1].equals("y")) {
                                yCoordinaat.add(waarde);
                            } else if (paramNaamBits[1].equals("type")) {
                                type.add(waarde);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        // Sla alle info voor de javaBestanden op
        javaBestanden = new JavaBestand[tarIndex.size()];
        for (int i = 0; i < tarIndex.size(); i++) {
            String fileDir = dir.replaceFirst("package.bluej", "") + bestandsNaam.get(i) + ".java";
            javaBestanden[i] = new JavaBestand(tarIndex.get(i), bestandsNaam.get(i), Integer.parseInt(xCoordnaat.get(i)), Integer.parseInt(yCoordinaat.get(i)), type.get(i), fileDir);
        }

        // Sla alle info voor de hasA relaties tussen de bestanden op
        // Het package.bluej bestand houdt geen info bij over de isA relatie
        relaties = new Relatie[depIndex.size()];
        for (int i = 0; i < depIndex.size(); i++) {
            JavaBestand start = null;
            JavaBestand stop = null;
            for (JavaBestand javaBestand : javaBestanden) {
                if (javaBestand.getNaam().equals(oorsprong.get(i))) {
                    start = javaBestand;
                    //System.out.println("Startpunt gevonden: " + javaBestand.toString());
                }
                if (javaBestand.getNaam().equals(bestemming.get(i))) {
                    stop = javaBestand;
                    //System.out.println("Stoppunt gevonden: " + javaBestand.toString());
                }
            }

            relaties[i] = new Relatie(depIndex.get(i), start, stop, "hasA");
        }
    }

    /**
     * Methode die een package.bluej bestand genereert
     */
    public void setPackageBlueJ() {
        // https://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-it-in-java
        try {
            PrintWriter writer = new PrintWriter(getProjectDir() + "/test.bluej", "UTF-8");
            writer.println("#BlueJ package file");
            //has a relaties toevoegen
            Relatie[] rel = getRelaties();
            for (Relatie r : rel) {
                if (r.getType().equals("hasA")) {
                    writer.println("dependency" + r.getIndex() + ".from=" + r.getOorsprong().getNaam());
                    writer.println("dependency" + r.getIndex() + ".to=" + r.getBestemming().getNaam());
                    writer.println("dependency" + r.getIndex() + ".type=UsesDependency");
                }
            }
            //Standaard Bluej info toevoegen
            writer.println("objectbench.height=76");
            writer.println("objectbench.width=898");
            writer.println("package.editor.height=758");
            writer.println("package.editor.width=800");
            writer.println("package.editor.x=451");
            writer.println("package.editor.y=3");
            //aantal relaties toevoegen
            writer.println("package.numDependencies=" + getAantalHasARelaties());
            //aantal klassen toevoegen
            writer.println("package.numTargets=" + javaBestanden.length);
            //Standaard Bluej info toevoegen
            writer.println("package.showExtends=true");
            writer.println("package.showUses=true");
            writer.println("project.charset=UTF-8");
            writer.println("readme.editor.height=1036");
            writer.println("readme.editor.width=1292");
            writer.println("readme.editor.x=-9");
            writer.println("readme.editor.y=-9");
            //klassen toevoegen            
            for (JavaBestand b : javaBestanden) {
                writer.println("target"+b.getIndex()+".editor.height=");
                writer.println("target"+b.getIndex()+".editor.width=");
                writer.println("target"+b.getIndex()+".editor.x=");
                writer.println("target"+b.getIndex()+".editor.y=");
                writer.println("target"+b.getIndex()+".height=");
                writer.println("target"+b.getIndex()+".name="+b.getNaam());
                writer.println("target"+b.getIndex()+".naviview.expanded=true");
                writer.println("target"+b.getIndex()+".showInterface=false");
                writer.println("target"+b.getIndex()+".type="); //extra info toevoegen
                writer.println("target"+b.getIndex()+".typeParameters=");
                writer.println("target"+b.getIndex()+".width=");
                writer.println("target"+b.getIndex()+".x=");
                writer.println("target"+b.getIndex()+".y=");
            }
            writer.close();
            System.out.println("***UHasseltEDU_INFO: Package.bluej aangemaakt");
        } catch (IOException e) {
            System.err.println(e);
            System.err.println("***UHasseltEDU_INFO: Package.bluej niet aangemaakt");
        }
    }

    public void setIsARelaties() {
        //System.out.println("Functie setIsARelaties() in ProjectData.java");
        ArrayList<Relatie> rel = new ArrayList<Relatie>();
        for (JavaBestand j : javaBestanden) {
            Class myClass = j.laadKlasse();
            if (myClass != null) {
                Class mySuperClass = myClass.getSuperclass();

                for (JavaBestand k : javaBestanden) {
                    if (getJavaBestand(mySuperClass.getSimpleName()) != null && k.getNaam().equals(mySuperClass.getName())) {
                        //System.out.println("*************** " + myClass.getName() + " erft van " + mySuperClass.getName());
                        //System.out.println(mySuperClass.getName());
                        //System.out.println(k.getNaam());
                        rel.add(new Relatie("1", j, k, "isA"));
                    }
                }
            } else {
                System.err.println("Error: Class not loaded");
            }
        }

        Relatie[] temp = getRelaties();
        for (Relatie t : temp) {
            rel.add(t);
        }
        Relatie[] tempRelaties = new Relatie[rel.size()];
        for (int i = 0; i < rel.size(); i++) {
            tempRelaties[i] = rel.get(i);
        }
        setRelaties(tempRelaties);
    }

    public int getAantalHasARelaties() {
        Relatie[] rel = getRelaties();
        int aantalDeps = 0;
        for (Relatie r : rel) {
            if (r.getType().equals("hasA")) {
                aantalDeps++;
            }
        }
        return aantalDeps;
    }

    /*public void setHasARelaties() {
     System.out.println("Functie setHasARelaties() in ProjectData.java");
     for (JavaBestand j : javaBestanden) {
     Class myClass = j.laadKlasse();
     System.out.println("*************** velden: " + Arrays.toString(myClass.getDeclaredFields()));
     }
     }*/
}