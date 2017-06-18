/*
 * Copyright: NetBeans Platform 4 beginners
 * Code adapted by Stijn Boutsen
 */
package org.bluej.project.type;

import java.io.IOException;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service=ProjectFactory.class)
/**
 * Deze klasse identificeert en laadt BlueJ projecten
 * Deze klasse is gebaseerd op de NetBeans Project Type Module Tutorial (https://platform.netbeans.org/tutorials/nbm-projecttype.html)
 * 
 * @author Stijn Boutsen
 */
public class BlueJProjectFactory implements ProjectFactory {

    //We geven het bestand op dat aangeeft dat het om een BlueJ project gaat.
    public static final String PROJECT_FILE = "package.bluej";

    //Bepaalt wanneer een project een BlueJproject is
    //doe het volgende indien "package.bluej" zich in de map bevind
    @Override
    public boolean isProject(FileObject projectDirectory) {
        //Geeft True terug indien de PROJECT_FILE aanwezig is
        return projectDirectory.getFileObject(PROJECT_FILE) != null;
    }

    //Specifies when the project will be opened, i.e., if the project exists:
    @Override
    public Project loadProject(FileObject dir, ProjectState state) throws IOException {
        return isProject(dir) ? new BlueJProject(dir, state) : null;
    }

    @Override
    public void saveProject(final Project project) throws IOException, ClassCastException {
        // leave unimplemented, not needed
    }

}