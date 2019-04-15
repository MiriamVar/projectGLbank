package client;

public class Card {
    private int id;
    private int ida;
    private String pin;
    private boolean active;
    private int expireM;
    private int expireY;

    public Card(int id, int ida, String pin, boolean active, int expireM, int expireY) {
        this.id = id;
        this.ida = ida;
        this.pin = pin;
        this.active = active;
        this.expireM = expireM;
        this.expireY = expireY;
    }

    public int getId() {
        return id;
    }

    public int getIda() {
        return ida;
    }

    public String getPin() {
        return pin;
    }

    public boolean isActive() {
        return active;
    }

    public int getExpireM() {
        return expireM;
    }

    public int getExpireY() {
        return expireY;
    }
}
