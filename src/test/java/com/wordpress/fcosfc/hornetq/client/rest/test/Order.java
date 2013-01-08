package com.wordpress.fcosfc.hornetq.client.rest.test;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Just another order class
 *
 * @author Paco Saucedo
 */
@XmlRootElement(name="order")
public class Order implements Serializable {

    private String item;
    private int units;
    private float unitPrice;

    /**
     * Default constructor
     */
    public Order() {
    }

    /**
     * Parameterized constructor
     * 
     * @param item
     * @param units
     * @param unitPrice 
     */
    public Order(String item, int units, float unitPrice) {
        this.item = item;
        this.units = units;
        this.unitPrice = unitPrice;
    }

    /**
     * Get the value of item
     *
     * @return the value of item
     */
    public String getItem() {
        return item;
    }

    /**
     * Set the value of item
     *
     * @param item new value of item
     */
    public void setItem(String item) {
        this.item = item;
    }

    /**
     * Get the value of units
     *
     * @return the value of units
     */
    public int getUnits() {
        return units;
    }

    /**
     * Set the value of units
     *
     * @param units new value of units
     */
    public void setUnits(int units) {
        this.units = units;
    }

    /**
     * Get the value of unitPrice
     *
     * @return the value of unitPrice
     */
    public float getUnitPrice() {
        return unitPrice;
    }

    /**
     * Set the value of unitPrice
     *
     * @param unitPrice new value of unitPrice
     */
    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.item != null ? this.item.hashCode() : 0);
        hash = 29 * hash + this.units;
        hash = 29 * hash + Float.floatToIntBits(this.unitPrice);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if ((this.item == null) ? (other.item != null) : !this.item.equals(other.item)) {
            return false;
        }
        if (this.units != other.units) {
            return false;
        }
        if (Float.floatToIntBits(this.unitPrice) != Float.floatToIntBits(other.unitPrice)) {
            return false;
        }
        return true;
    }
    
    
}
