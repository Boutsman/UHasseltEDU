package org.objectenbench.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.InvocationTargetException;
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
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Exceptions;

/**
 * @author Stijn Boutsen
 */
public class ObjectenBenchGraphScene extends GraphScene<Object, Object> {

    private static final Border BORDER_4 = BorderFactory.createLineBorder(2);

    private LayerWidget mainLayer;
    private LayerWidget connectionLayer;

    private WidgetAction moveAction = ActionFactory.createMoveAction();

    public ObjectenBenchGraphScene() {
        mainLayer = new LayerWidget(this);
        addChild(mainLayer);

        connectionLayer = new LayerWidget(this);
        addChild(connectionLayer);

        getActions().addAction(ActionFactory.createZoomAction());
        getActions().addAction(createSelectAction());
    }

    @Override
    protected Widget attachNodeWidget(final Object node) {
        LabelWidget label = new LabelWidget(this, node.getClass().getSimpleName());
        label.setBorder(BORDER_4);
        label.getActions().addAction(moveAction);
        label.getActions().addAction(ActionFactory.createOrthogonalMoveControlPointAction());
        label.getActions().addAction(createSelectAction());
        label.getActions().addAction(ActionFactory.createRectangularSelectAction(this, connectionLayer));
        label.getActions().addAction(ActionFactory.createPopupMenuAction(new PopupMenuProvider() {

            @Override
            public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
                // Bronvermelding: http://www.java2s.com/Code/Java/Swing-JFC/AsimpleexampleofJPopupMenu.htm                
                JPopupMenu popup = new JPopupMenu();
                ActionListener menuListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        String menuText = event.getActionCommand();
                        String[] parts = menuText.split(" ");
                        System.out.println("Popup menu item [" + event.getActionCommand() + "] was pressed." + event.getSource());

                        Method[] methodes = node.getClass().getDeclaredMethods();
                        //tekenJFrame(methodes[Integer.parseInt(parts[0])]);

                        Method m = methodes[Integer.parseInt(parts[0])];

                        //Ga na of het een getter of setter is
                        if (methodes[Integer.parseInt(parts[0])].getReturnType().getSimpleName().equals("void")) {
                            System.out.println("Methode zonder returnwaarde");
                            // System.out.println(methodes[Integer.parseInt(parts[0])].getReturnType().getSimpleName());
                            if (m.getParameters().length != 0) {

                                System.out.println("Methode met parameters");
                                tekenJFrame(node, m);
                                //m.invoke(node /*,parameters*/);
                                //m = reflectie.createInstance(node.laadKlasse(), Integer.parseInt(parts[0]));

                            } else {
                                System.out.println("Methode zonder parameters");
                                try {
                                    //tekenJFrame(m);
                                    m.invoke(node);
                                } catch (IllegalAccessException ex) {
                                    Exceptions.printStackTrace(ex);
                                } catch (IllegalArgumentException ex) {
                                    Exceptions.printStackTrace(ex);
                                } catch (InvocationTargetException ex) {
                                    Exceptions.printStackTrace(ex);
                                }
                            }
                        } else {
                            System.out.println("Methode met returnwaarde");
                            if (m.getParameters().length == 0) {
                                try {
                                    System.out.println("Methode zonder parameters");
                                    Object returnwaarde = m.invoke(node);
                                    //System.out.println(returnwaarde.toString());
                                    toonReturnWaarde(m, returnwaarde.toString());
                                    //m = reflectie.createInstance(node.laadKlasse(), Integer.parseInt(parts[0]));
                                } catch (IllegalAccessException ex) {
                                    Exceptions.printStackTrace(ex);
                                } catch (IllegalArgumentException ex) {
                                    Exceptions.printStackTrace(ex);
                                } catch (InvocationTargetException ex) {
                                    Exceptions.printStackTrace(ex);
                                }
                            } else {

                                System.out.println("Methode met parameters");
                                tekenJFrame(node, m);

                            }
                        }
                    }
                };
                JMenuItem item;
                Method[] methodes = node.getClass().getDeclaredMethods();
                int i = 0;
                for (Method m : methodes) {
                    popup.add(item = new JMenuItem(i + " - " + m.getName() + Arrays.toString(m.getParameters()).replace("[", "(").replace("]", ")")));
                    item.addActionListener(menuListener);
                    i++;
                }
                return popup;
            }
        }));
        mainLayer.addChild(label);
        return label;
    }

    @Override
    protected Widget attachEdgeWidget(Object edge) {
        ConnectionWidget connection = new ConnectionWidget(this);
        connection.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        float[] dashes = {10, 10};
        connection.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, dashes, 2));   //BasicStroke(float width, int cap, int join, float miterlimit, float[] dash, float dash_phase)
        connectionLayer.addChild(connection);
        return connection;
    }

    @Override
    protected void attachEdgeSourceAnchor(Object edge, Object oldSourceNode, Object sourceNode) {
        Widget w = sourceNode != null ? findWidget(sourceNode) : null;
        ((ConnectionWidget) findWidget(edge)).setSourceAnchor(AnchorFactory.createRectangularAnchor(w));
    }

    @Override
    protected void attachEdgeTargetAnchor(Object edge, Object oldTargetNode, Object targetNode) {
        Widget w = targetNode != null ? findWidget(targetNode) : null;
        ((ConnectionWidget) findWidget(edge)).setTargetAnchor(AnchorFactory.createRectangularAnchor(w));
    }

    private static Object convert(Class<?> targetType, String textWaarde) {
        PropertyEditor editor = PropertyEditorManager.findEditor(targetType);
        editor.setAsText(textWaarde);
        return editor.getValue();
    }

    private void tekenJFrame(final Object node, final Method methode) {
        Parameter[] params = methode.getParameters();
        final Class[] paramTypes = methode.getParameterTypes();
        //System.out.println("Methode wordt opgeroepen " + methode.getName() + " " + params.length);

        if (params.length > 0) {
            final JFrame methodesFrame = new JFrame();
            methodesFrame.setTitle("Add method parameters");

            methodesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            methodesFrame.setLocationRelativeTo(null);

            Container contentPane = methodesFrame.getContentPane();
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
            }

            panel.add(new JLabel("Voer methode uit"));
            JButton okBtn = new JButton("OK");
            okBtn.setActionCommand("disable");
            okBtn.addActionListener(new ActionListener() {
                //@Override
                public void actionPerformed(ActionEvent e) {
                    //System.out.println("Btn clicked");
                    //System.out.println("Voer methode uit met volgende parameters:");
                    ArrayList<Object> params = new ArrayList<Object>();
                    int i = 0;
                    for (JTextField textFieldParam : textFieldParam) {
                        //System.out.println(paramTypes[i] + " - " + textFieldParam.getText());
                        //System.out.println(convert(paramTypes[i], textFieldParam.getText()));
                        params.add(convert(paramTypes[i], textFieldParam.getText()));
                    }
                    try {
                        if (methode.getReturnType().getSimpleName().equals("void")) {
                            methode.invoke(node, params.toArray(new Object[params.size()]));
                        } else {
                            Object returnwaarde = methode.invoke(node, params.toArray(new Object[params.size()]));
                            //System.out.println(returnwaarde.toString());
                            toonReturnWaarde(methode, returnwaarde.toString());
                        }

                    } catch (IllegalAccessException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IllegalArgumentException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (InvocationTargetException ex) {
                        Exceptions.printStackTrace(ex);
                    }

                    //close the JFrame
                    methodesFrame.dispose();
                }
            });
            panel.add(okBtn);

            SpringUtilities.makeGrid(panel,
                    params.length+1, 2, //rows, cols
                    5, 5, //initialX, initialY
                    5, 5);//xPad, yPad

            layout.putConstraint(SpringLayout.EAST, contentPane, 5, SpringLayout.EAST, panel);
            layout.putConstraint(SpringLayout.SOUTH, contentPane, 5, SpringLayout.SOUTH, panel);

            methodesFrame.add(panel);
            methodesFrame.pack();
            methodesFrame.setVisible(true);
        }
    }

    public void toonReturnWaarde(Method m, String returnwaarde) {
        JFrame returnFrame = new JFrame();
        returnFrame.setTitle("Return values");
        returnFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        returnFrame.setLocationRelativeTo(null);
        Container contentPane = returnFrame.getContentPane();
        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);
        JLabel infoLabel1 = new JLabel("Returntype");
        panel.add(infoLabel1);
        JLabel infoLabel2 = new JLabel("Returnvalue");
        panel.add(infoLabel2);
        JLabel typeLabel = new JLabel(m.getReturnType().getSimpleName());
        panel.add(typeLabel);
        JLabel returnWaardeLabel = new JLabel(returnwaarde, JLabel.TRAILING);
        panel.add(returnWaardeLabel);

        SpringUtilities.makeGrid(panel,
                2, 2, //rows, cols
                5, 5, //initialX, initialY
                5, 5);//xPad, yPad

        layout.putConstraint(SpringLayout.EAST, contentPane, 5, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.SOUTH, contentPane, 5, SpringLayout.SOUTH, panel);

        returnFrame.add(panel);
        returnFrame.pack();
        returnFrame.setVisible(true);
    }
}
