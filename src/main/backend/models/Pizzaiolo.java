package models;

import java.time.Duration;

public class Pizzaiolo {
    private int _id;
    private boolean _isAvailable;
    private Duration _delay;

    public Pizzaiolo(int id, boolean isAvailable, Duration delay) {
        this._id = id;
        this._isAvailable = isAvailable;
        this._delay = delay;
    }

    public int GetId()
    {
        return _id;
    }

    public boolean IsAvailable()
    {
        return _isAvailable;
    }

    public void setDelay() {
        _isAvailable = false;
        new Thread(() -> {
            try {
                Thread.sleep(_delay.toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            _isAvailable = true;
        }).start();
    }

}
