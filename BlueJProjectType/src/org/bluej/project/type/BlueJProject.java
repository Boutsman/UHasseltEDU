package org.bluej.project.type;

import java.awt.Image;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * Deze klasse houdt de informatie bij van een BlueJ project
 * Deze klasse is gebaseerd op de NetBeans Project Type Module Tutorial (https://platform.netbeans.org/tutorials/nbm-projecttype.html)
 * 
 * @author Stijn Boutsen
 */
public class BlueJProject implements Project {

    //Directory van het project wordt bijgehouden
    private final FileObject projectDir;
    //Toestand van het project wordt bijgehouden
    private final ProjectState state;
    //Lookup wordt bijgehouden
    private Lookup lkp;

    /**
     * Nieuw BlueJ project
     *
     * @param dir de projectdirectory
     * @param state de toestand van het project
     */
    BlueJProject(FileObject dir, ProjectState state) {
        this.projectDir = dir;
        this.state = state;
    }

    /**
     * Geef de directory van het project terug
     *
     * @return een FileObject met de directory van het project
     */
    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    /**
     * Vraag de lookup op
     *
     * @return de lookup
     */
    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(new Object[]{
                // register your features here
                this,
                new Info(),
                new BlueJProjectLogicalView(this),
            });
        }
        return lkp;
    }

    private final class Info implements ProjectInformation {

        @StaticResource()
        public static final String BLUEJ_ICON = "org/bluej/project/type/bluej-icon.png";

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(BLUEJ_ICON));
        }

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public Project getProject() {
            return BlueJProject.this;
        }

    }

    class BlueJProjectLogicalView implements LogicalViewProvider {

        @StaticResource()
        public static final String BLUEJ_ICON = "org/bluej/project/type/bluej-icon.png";

        private final BlueJProject project;

        public BlueJProjectLogicalView(BlueJProject project) {
            this.project = project;
        }

        @Override
        public Node createLogicalView() {
            try {
                //Obtain the project directory's node:
                FileObject projectDirectory = project.getProjectDirectory();
                DataFolder projectFolder = DataFolder.findFolder(projectDirectory);
                Node nodeOfProjectFolder = projectFolder.getNodeDelegate();
                //Decorate the project directory's node:
                return new ProjectNode(nodeOfProjectFolder, project);
            } catch (DataObjectNotFoundException donfe) {
                Exceptions.printStackTrace(donfe);
                //Fallback-the directory couldn't be created -
                //read-only filesystem or something evil happened
                return new AbstractNode(Children.LEAF);
            }
        }

        private final class ProjectNode extends FilterNode {

            final BlueJProject project;

            public ProjectNode(Node node, BlueJProject project)
                    throws DataObjectNotFoundException {
                super(node,
                        NodeFactorySupport.createCompositeChildren(
                                project,
                                "Projects/org-bluej-project/Nodes"),
                        //new FilterNode.Children(node),
                        new ProxyLookup(
                                new Lookup[]{
                                    Lookups.singleton(project),
                                    node.getLookup()
                                }));
                this.project = project;
            }

            @Override
            public Action[] getActions(boolean arg0) {
                return new Action[]{
                    //Voeg acties toe aan het menu verkrijgbaar door met de rechter muisknop op de projectnode te klikken
                    CommonProjectActions.newFileAction(),
                    CommonProjectActions.renameProjectAction(),
                    CommonProjectActions.copyProjectAction(),
                    CommonProjectActions.deleteProjectAction(),
                    CommonProjectActions.closeProjectAction()
                };
            }

            @Override
            public Image getIcon(int type) {
                return ImageUtilities.loadImage(BLUEJ_ICON);
            }

            @Override
            public Image getOpenedIcon(int type) {
                return getIcon(type);
            }

            @Override
            public String getDisplayName() {
                return project.getProjectDirectory().getName();
            }

        }

        @Override
        public Node findPath(Node root, Object target) {
            //leave unimplemented for now
            return null;
        }

    }

}
