/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweepertest1;

/**
 *
 *  * @author Grisham Balloo 20099072
 */
public class Timer {
    private long startTime;
    private long endTime;

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public void stop() {
        this.endTime = System.currentTimeMillis();
    }

    public long getDuration() {
        if (endTime == 0) { // If the timer hasn't stopped
        return (System.currentTimeMillis() - startTime) / 1000;
    } else {
        return (endTime - startTime) / 1000;
    }
    }
}
