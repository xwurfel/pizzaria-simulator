package interfaces;
public interface IPizzaStatus {
    public void next();
    public void stop();
    public void setContext(IPizza p);
    public String getStatusName();
}
