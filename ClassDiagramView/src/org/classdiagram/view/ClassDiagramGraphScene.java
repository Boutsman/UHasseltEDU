package org.classdiagram.view;

/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.project.data.JavaBestand;
import org.project.data.ObjectenBench;
import org.project.data.Reflection;
import org.project.data.Relatie;

/**
 
 * @author Stijn Boutsen
 */
public class ClassDiagramGraphScene extends GraphScene<JavaBestand, Relatie>/*.StringGraph*/ {

    private static final Border BORDER_4 = BorderFactory.createLineBorder(2);

    //private Object[] objectenBench = null;
    private LayerWidget mainLayer;
    private LayerWidget connectionLayer;

    public ClassDiagramGraphScene() {
        mainLayer = new LayerWidget(this);
        addChild(mainLayer);

        connectionLayer = new LayerWidget(this);
        addChild(connectionLayer);

        getActions().addAction(ActionFactory.createZoomAction());
        getActions().addAction(createSelectAction());
    }

    @Override
    protected Widget attachNodeWidget(final JavaBestand node) {
        LabelWidget widget = new LabelWidget(this, node.getNaam());
        widget.setBorder(BORDER_4);
        widget.getActions().addAction(createSelectAction());
        widget.getActions().addAction(ActionFactory.createMoveAction());
        widget.getActions().addAction(ActionFactory.createRectangularSelectAction(this, connectionLayer));

        widget.getActions().addAction(ActionFactory.createPopupMenuAction(new PopupMenuProvider() {
            @Override
            public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
                // Bronvermelding: http://www.java2s.com/Code/Java/Swing-JFC/AsimpleexampleofJPopupMenu.htm                
                JPopupMenu popup = new JPopupMenu();
                ActionListener menuListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        String menuText = event.getActionCommand();
                        System.out.println("Popup menu item [" + event.getActionCommand() + "] was pressed." + event.getSource());
                        String[] parts = menuText.split(" ");
                        Reflection reflectie = new Reflection();

                        Constructor[] constr = reflectie.getAlleConstructors(node.laadKlasse());                        

                        System.out.println("Aanmaken van object: " + node.getNaam());

                        Object obj = null;

                        if (constr[Integer.parseInt(parts[0])].getParameters() == null) {
                            System.out.println("Object zonder parameters aanmaken");
                            obj = reflectie.createInstance(node.laadKlasse(), Integer.parseInt(parts[0]));
                        } else {
                            System.out.println("Object met parameters aanmaken");
                            tekenJFrame(node, Integer.parseInt(parts[0]));                              
                        }

                        if (obj != null) {
                            Method[] methodes = reflectie.getAlleMethodes(obj);
                            System.out.println("Methodes van het object: " + Arrays.toString(methodes));
                            addObject(obj);
                            ObjectenBench bench = ObjectenBench.getInstance();
                            bench.voegObjectToe(obj);
                            System.out.println("Object geladen");
                        } else {
                            System.out.println("Geen object geladen");
                        }
                        //ArrayList<Object> alleObj = bench.getObjBench();
                        //System.out.println(alleObj);
                        //System.out.println("Popup menu item [" + event.getActionCommand() + "] was pressed." + event.getID());
                        //System.out.println(parts[0]);
                    }
                };
                JMenuItem item;
                Constructor[] constructors = node.laadKlasse().getConstructors();
                int i = 0;
                if (constructors.length > 0) {
                    for (Constructor c : constructors) {
                        Parameter[] p = c.getParameters();
                        popup.add(item = new JMenuItem(i + " - new " + c.getName() + Arrays.toString(c.getParameters()).replace("[", "(").replace("]", ")")));
                        item.addActionListener(menuListener);
                        i++;
                    }
                }
                return popup;
            }
        }));
        mainLayer.addChild(widget);
        return widget;
    }

    @Override
    protected Widget attachEdgeWidget(Relatie edge) {
        ConnectionWidget connection = new ConnectionWidget(this);
        connection.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);

        if (edge.getType().equals("hasA")) {
            float[] dashes = {20, 20};
            connection.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, dashes, 2));   //BasicStroke(float width, int cap, int join, float miterlimit, float[] dash, float dash_phase)

        } else {
            float[] dashes = {5, 5};
            connection.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, dashes, 0));   //BasicStroke(float width, int cap, int join, float miterlimit, float[] dash, float dash_phase)

        }
        //connection.setRouter(RouterFactory.createOrthogonalSearchRouter(mainLayer/*,connectionLayer*/));
        connectionLayer.addChild(connection);
        return connection;
    }

    @Override
    protected void attachEdgeSourceAnchor(Relatie edge, JavaBestand oldSourceNode, JavaBestand sourceNode) {
        Widget w = sourceNode != null ? findWidget(sourceNode) : null;
        ((ConnectionWidget) findWidget(edge)).setSourceAnchor(AnchorFactory.createRectangularAnchor(w));
    }

    @Override
    protected void attachEdgeTargetAnchor(Relatie edge, JavaBestand oldTargetNode, JavaBestand targetNode) {
        Widget w = targetNode != null ? findWidget(targetNode) : null;
        ((ConnectionWidget) findWidget(edge)).setTargetAnchor(AnchorFactory.createRectangularAnchor(w));
    }

    private void tekenJFrame(final JavaBestand node,final int constrNr) {
        final Reflection reflectie = new Reflection();
        Constructor[] constr = reflectie.getAlleConstructors(node.laadKlasse());
        Parameter[] params = constr[constrNr].getParameters();
        final Class[] paramTypes = constr[constrNr].getParameterTypes();

        if (params.length > 0) {
            final JFrame constructorFrame = new JFrame();
            constructorFrame.setTitle("Add constructor parameters");

            constructorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            constructorFrame.setLocationRelativeTo(null);

            Container contentPane = constructorFrame.getContentPane();
            // https://docs.oracle.com/javase/tutorial/uiswing/layout/spring.html
            SpringLayout layout = new SpringLayout();
            JPanel panel = new JPanel(layout);

            final JTextField[] textFieldParam = new JTextField[params.length];

            for (int i = 0; i < params.length; i++) {
                JLabel paramLabel = new JLabel(params[i].getName(), JLabel.TRAILING);
                panel.add(paramLabel);

                textFieldParam[i] = new JTextField(paramTypes[i].getName());
                textFieldParam[i].setBackground(Color.white);
                panel.add(textFieldParam[i]);

                System.out.println(params[i].getAnnotatedType().getType().getTypeName());
            }
            panel.add(new JLabel("Maak object"));
            JButton okBtn = new JButton("OK");
            okBtn.setActionCommand("disable");
            okBtn.addActionListener(new ActionListener() {
                //@Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Btn clicked");
                    System.out.println("Maak object met volgende parameters:");
                    ArrayList<Object> params = new ArrayList<Object>();
                    int i = 0;
                    for (JTextField textFieldParam : textFieldParam) {
                        params.add(convert(paramTypes[i],textFieldParam.getText()));                        
                    }
                    
                    Object obj = reflectie.createInstance2(node.laadKlasse(), constrNr,params);
                    ObjectenBench bench = ObjectenBench.getInstance();
                    bench.voegObjectToe(obj);
                    //close the JFrame
                    //constructorFrame.dispose();
                }
            });
            panel.add(okBtn);

            SpringUtilities.makeGrid(panel,
                    params.length + 1, 2, //rows, cols
                    5, 5, //initialX, initialY
                    5, 5);//xPad, yPad

            layout.putConstraint(SpringLayout.EAST, contentPane, 5, SpringLayout.EAST, panel);
            layout.putConstraint(SpringLayout.SOUTH, contentPane, 5, SpringLayout.SOUTH, panel);

            constructorFrame.add(panel);
            constructorFrame.pack();
            constructorFrame.setVisible(true);

        }

    }
    
    private static Object convert(Class<?> targetType, String textWaarde) {
        PropertyEditor editor = PropertyEditorManager.findEditor(targetType);
        editor.setAsText(textWaarde);
        return editor.getValue();
    }

}
