package omat.drinkkidb.domain;

public abstract class AbstractObject {

    private int id;
    private String name;

    public AbstractObject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
