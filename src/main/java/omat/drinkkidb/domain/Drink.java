package omat.drinkkidb.domain;

import javax.swing.text.AbstractDocument;
import omat.drinkkidb.sql.Dao;

public class Drink extends AbstractObject{

    public Drink(int id, String name) {
        super(id, name);
    }
}
