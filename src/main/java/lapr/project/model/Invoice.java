/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author 1100241
 */
public class Invoice {
    
    final String tag;
    final float totalCost;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    
    public Invoice(float totalCost){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        tag = "FR-" + sdf.format(timestamp) + "";
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return tag;
    }

    public String getTag() {
        return tag;
    }

    public float getTotalCost() {
        return totalCost;
    }
    
    
    
    
}
