package fi.linuxbox.neo4j;

import java.io.File;

/**
 * Command Line Interface.
 */
public interface CLI {
    /**
     * The address and port on which to listen for connections.
     *
     * E.g. "locahost:9000" or "127.0.0.1:3000".
     *
     * @return The listening address.
     */
    String getAddress();

    /**
     * "true" if clients should have read-only access to the database. "false" otherwise.
     *
     * @return "true" or "false".
     */
    String getReadOnly();

    /**
     * The directory in which the database is.
     *
     * @return File instance representing the database directory.
     */
    File getPath();

    /**
     * Show help if requested.
     *
     * @return true if help was shown, false otherwise.
     */
    boolean helpShown();
}
