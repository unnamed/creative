package team.unnamed.uracle.generate;

import team.unnamed.uracle.TreeOutputStream;

import java.io.IOException;

public interface TreeWriteable {

    void write(TreeOutputStream output) throws IOException;

}
