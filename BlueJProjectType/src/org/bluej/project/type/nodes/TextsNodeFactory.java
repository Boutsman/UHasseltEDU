package org.bluej.project.type.nodes;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.bluej.project.type.BlueJProject;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

@NodeFactory.Registration(projectType = "org-bluej-project", position = 10)
/**
 * Deze klasse bepaalt de bestanden die in het project window als nodes worden voorgesteld.
 * Deze klasse is gebaseerd op de NetBeans Project Type Module Tutorial (https://platform.netbeans.org/tutorials/nbm-projecttype.html)
 * 
 * @author Stijn Boutsen
 */
public class TextsNodeFactory implements NodeFactory {
    
    @Override
    public NodeList<?> createNodes(Project project) {
        //Vraag het BlueJ project op uit de lookup
        BlueJProject p = project.getLookup().lookup(BlueJProject.class);
        //Ga na of er wel een BlueJ project in de lookup zit
        assert p != null;
        //Geef een nieuwe textNode lijst terug
        return new TextsNodeList(p);
    }

    private class TextsNodeList implements NodeList<Node> {

        BlueJProject project;

        public TextsNodeList(BlueJProject project) {
            this.project = project;
        }

        @Override
        public List<Node> keys() {
            //Vraag alle bestanden op van de project directory
            FileObject bluejFolder = project.getProjectDirectory();
            //Maak een array waarin we alle nodige bestanden zullen opslaan.
            List<Node> result = new ArrayList<Node>();                       
            //Doe iets als de folder niet leeg is
            if (bluejFolder != null) {
                //Doe iets voor elk bestand
                for (FileObject bluejFolderFile : bluejFolder.getChildren()) {
                    //Kijk na of we het bestand nodig hebben voor ons BlueJ-project
                    if (bluejFolderFile.hasExt("java") || bluejFolderFile.hasExt("txt") || bluejFolderFile.hasExt("TXT")) {
                        //Sla het bestand op in de voorgemaakte array
                        try {                            
                            result.add(DataObject.find(bluejFolderFile).getNodeDelegate());
                        } catch (DataObjectNotFoundException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                }
            }
            //geef de array met de bluej bestanden terug
            return result;            
        }

        @Override
        public Node node(Node node) {
            return new FilterNode(node);
        }

        @Override
        public void addNotify() {
        }

        @Override
        public void removeNotify() {
        }

        @Override
        public void addChangeListener(ChangeListener cl) {
        }

        @Override
        public void removeChangeListener(ChangeListener cl) {
        }

    }

}
