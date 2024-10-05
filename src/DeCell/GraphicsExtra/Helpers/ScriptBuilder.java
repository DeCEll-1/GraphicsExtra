package DeCell.GraphicsExtra.Helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScriptBuilder {

    private List<String> lines = new ArrayList<>();

    public ScriptBuilder() {
    }

    public void addLine(String line) {
        lines.add(line);
    }

    public void addLines(String[] lines) {
        this.lines.addAll(Arrays.asList(lines));
    }


    public String returnScript() {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append("\r\n");
        }

        return sb.toString();

    }

}
