/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fyoprojekt;

import java.awt.Graphics;

/**
 *
 * @author fast4shoot
 */
public interface Element {
    void paint(Graphics g, int w, int h);
    Ray testHit(Ray ray);
}
