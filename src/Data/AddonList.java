/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

/**
 *
 * @author BJ
 */
public class AddonList extends java.util.ArrayList{
    public Addon get(int i) {
        return (Addon) super.get(i);
    }
    public boolean add(Addon addon) {
        return super.add(addon);
    }
    public void add(int i,Addon addon) {
        super.add(i,addon);
    }
}
