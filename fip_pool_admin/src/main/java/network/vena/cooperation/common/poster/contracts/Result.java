package network.vena.cooperation.common.poster.contracts;


public interface Result extends JsonableInterface {

    static final String SUCCESSFUL = "success";

    public boolean isSuccessful();
}
