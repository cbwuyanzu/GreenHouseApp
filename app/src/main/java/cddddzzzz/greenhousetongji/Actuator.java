package cddddzzzz.greenhousetongji;

/**
 * Created by cdz on 2016/8/17.
 */
public class Actuator {
    public String nameJson;

    public String getLabel() {
        return label;
    }

    public String label;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String command;

    public Actuator(String n, String label) {
        this.nameJson = n;
        this.label = label;
    }

    public String toJString() {
        StringBuilder stringBuilder = new StringBuilder("{\"");
        stringBuilder.append(nameJson);
        stringBuilder.append("\":\"");
        stringBuilder.append(command);
        stringBuilder.append("\"}");
        return stringBuilder.toString();
    }
}
