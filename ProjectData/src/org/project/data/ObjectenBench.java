/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.project.data;

import java.util.ArrayList;

/**
 *
 * @author Boutsman
 */
public class ObjectenBench {

    private ArrayList<Object> objBench = new ArrayList<Object>();
    private static ObjectenBench instance = null;

    public ObjectenBench() {

    }

    public ArrayList<Object> getObjBench() {
        return objBench;
    }

    public void voegObjectToe(Object obj) {
        objBench.add(obj);
    }

    public static ObjectenBench getInstance() {
        if (instance == null) {
            instance = new ObjectenBench();
        }
        return instance;
    }

}
