package network.vena.cooperation.common.poster.kernal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.vena.cooperation.common.poster.contracts.JsonableInterface;

public abstract class JsonAble implements JsonableInterface {

    @Override
    public String toString() {
        String str = this.toJson();
        return str != null ? str : super.toString();
    }

    @Override
    public String toJson() {
        try {
            return (new ObjectMapper()).writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
