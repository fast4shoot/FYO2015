/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fyoprojekt;

/**
 *
 * @author fast4shoot
 */
public interface LineHit {
    public boolean wasHit();
    public Point hitPoint();
    public Vector surfaceNormal();
    public double lightMul();
}
