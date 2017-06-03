package org.classdiagram.view;

import java.awt.BorderLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.api.visual.widget.Widget;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.filesystems.FileObject;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.project.data.*;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//org.classdiagram.view//classdiagram//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "classdiagramTopComponent",
        iconBase = "org/classdiagram/img/icon.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.classdiagram.view.classdiagramTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_classdiagramAction",
        preferredID = "classdiagramTopComponent"
)
@Messages({
    "CTL_classdiagramAction=classdiagram",
    "CTL_classdiagramTopComponent=classdiagram Window",
    "HINT_classdiagramTopComponent=This is a classdiagram window"
})
public final class classdiagramTopComponent extends TopComponent {

    public classdiagramTopComponent() {
        initComponents();
        setName(Bundle.CTL_classdiagramTopComponent());
        setToolTipText(Bundle.HINT_classdiagramTopComponent());
        content = new InstanceContent();
        associateLookup(new AbstractLookup(content));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        classDiagramjScrollPane = new javax.swing.JScrollPane();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(classDiagramjScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(classDiagramjScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane classDiagramjScrollPane;
    // End of variables declaration//GEN-END:variables

    // Eigen variabelen declareren
    private ProjectData blueJData;
    private ClassDiagramGraphScene scene;
    private InstanceContent content;

    @Override
    public void componentOpened() {
        maakKlasseDiagram();
    }

    public void maakKlasseDiagram() {

        //Opvragen van de projectbestanden
        Project project = OpenProjects.getDefault().getOpenProjects()[0];
        blueJData = new ProjectData();

        if (project != null) {
            /*Opvragen van de projectlocatie*/
            FileObject projectDir = project.getProjectDirectory();
            blueJData.setProjectDir(projectDir.getPath());
            /*Opvragen van alle bestanden en mappen*/
//            FileObject[] alleProjectOnderdelen = projectDir.getChildren();

            /*Doe iets indien het om een BlueJ project gaat*/
            if (projectDir.getFileObject("package.bluej") != null) {
                System.out.println("***Project type: BlueJ project");
                /*Laad alle java-bestanden in*/
                /*Vraag nodig info op uit package.bluej*/
                blueJData.verwerkBlueJPackageFile(projectDir.getFileObject("package.bluej").getPath());
                blueJData.setIsARelaties();
                blueJData.setPackageBlueJ();
            } /*Doe iets indien het om een standaard Java project gaat*/ else if (projectDir.getFileObject("src") != null) {
                System.out.println("***Project type: Standaard java project");
                verwerkNetBeansJavaProject(projectDir);
                //blueJData.setIsARelaties();
                //blueJData.setPackageBlueJ();
            }

            scene = tekenKlasseDiagram();
            classDiagramjScrollPane.setViewportView(scene.createView());
        }
    }

    /**
     * Verwerk een standaard NetBeans Java Project
     *
     * @param projectDir
     */
    public void verwerkNetBeansJavaProject(FileObject projectDir) {
        blueJData = new ProjectData();

        ArrayList<String> bestanden = new ArrayList<String>();
        ArrayList<String> fileDir = new ArrayList<String>();
        FileObject src = projectDir.getFileObject("src");
        FileObject map = src.getFileObject(projectDir.getName());
        FileObject[] mapInhoud = map.getChildren();
        for (FileObject inhoud : mapInhoud) {
            /*Laad alle java-bestanden in*/
            if (inhoud.hasExt("java")) {                
                String[] temp = projectDir.getPath().split("/");
                bestanden.add(temp[temp.length - 1].toLowerCase() + "." + inhoud.getName());
                fileDir.add(projectDir.getPath() + "/build/classes/");
                //fileDir.add(projectDir.getPath() + "/src/" + temp[temp.length-1] + "/" + inhoud.getName() + ".java");
                //System.out.println("*********" + projectDir.getPath() + "/build/classes/" + temp[temp.length-1] + "/" + inhoud.getName() + ".java");
            }
        }

        JavaBestand[] javaBestanden = new JavaBestand[bestanden.size()];
        for (int i = 0; i < bestanden.size(); i++) {
            javaBestanden[i] = new JavaBestand(Integer.toString(i), bestanden.get(i).replace(".java", ""), i * 50, 0, null, fileDir.get(i));
        }
        blueJData.setJavaBestanden(javaBestanden);
    }

    public Collection<JavaBestand> getObjectsFromScene() {
        Collection<JavaBestand> nodes = scene.getNodes();
        JavaBestand[] temp = blueJData.getJavaBestanden();
        ArrayList<JavaBestand> bestanden = new ArrayList<JavaBestand>();

        for (JavaBestand n : nodes) {
            int index = bestanden.indexOf(n);
//            bestanden.get(index).setCoordinaten(n., index);
        }
        return nodes;
//        System.out.println("Alle objecten in de scene");
//        for (JavaBestand node : nodes) {
//            System.out.println(node.getNaam());
//        }
    }

    @Override
    public void componentClosed() {
        //int count = classDiagramjScrollPane.getViewport().getView().getAccessibleContext().getAccessibleChildrenCount();
        //System.out.println("Test of dit werkt: " + count);
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p
    ) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    /**
     * Teken het klassediagram
     *
     * @return de ClassDiagramGraphScene
     */
    public ClassDiagramGraphScene tekenKlasseDiagram() {

        ClassDiagramGraphScene scene = new ClassDiagramGraphScene();

        Widget klasseDiagramWidget = null;
        JavaBestand[] javaBestanden = blueJData.getJavaBestanden();
        /*Klassen tekenen*/
        if (javaBestanden != null) {
            for (JavaBestand bestand : javaBestanden) {
                klasseDiagramWidget = scene.addNode(bestand);
                klasseDiagramWidget.setPreferredLocation(new Point(bestand.getX(), bestand.getY()));
            }
        }
        /*Pijlen trekken tussen de klassen*/
        Relatie[] relaties = blueJData.getRelaties();
        if (relaties != null) {
            try {
                for (Relatie relatie : relaties) {
                    //de methode eddEdge heeft een String nodig
                    scene.addEdge(relatie);
                    //setEdgeSource heeft een javaBestand nodig met de oorsprong van de pijl
                    scene.setEdgeSource(relatie, relatie.getOorsprong());
                    //setEdgeTarget heeft een javaBestand nodig met de bestemming van de pijl
                    scene.setEdgeTarget(relatie, relatie.getBestemming());
                }

            } catch (NullPointerException ex) {
                System.out.println(ex);
            }
        }
        return scene;
    }

    public Collection<Relatie> getRelationsFromScene() {
        Collection<Relatie> nodes = scene.getEdges();
        return nodes;
    }
}

//        System.out.println("Alle relaties in de scene");
//        for (Relatie node : nodes) {
//            System.out.println("Oorsprong: " + node.getOorsprong().getNaam() + " - Bestemming: " + node.getBestemming().getNaam());
//        }
//        for (Relatie deEdge : edges) {
//            System.out.println("Oorsprong: " + scene.getEdgeSource(deEdge) + " - Bestemming: " + scene.getEdgeTarget(deEdge));
//        }
