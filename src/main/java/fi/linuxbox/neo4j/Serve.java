package fi.linuxbox.neo4j;

import org.apache.commons.cli.ParseException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.graphdb.factory.GraphDatabaseSettings.BoltConnector;

import static java.lang.Runtime.getRuntime;
import static org.neo4j.graphdb.factory.GraphDatabaseSettings.read_only;

/**
 * Main class.
 */
public class Serve {
    /**
     * The main entry point.
     *
     * @param args Command line options and arguments.
     * @throws InterruptedException if the CTRL-C is pressed.
     * @throws ParseException if there are any problems encountered
     * while parsing the command line tokens.
     */
    public static void main(String... args) throws InterruptedException, ParseException {
        final CLI cli = new ApacheCommonsCLI(args);
        if (cli.helpShown())
            return;

        final BoltConnector bolt = GraphDatabaseSettings.boltConnector( "0" );

        final GraphDatabaseService graph = new GraphDatabaseFactory()
                // Where is it?
                .newEmbeddedDatabaseBuilder(cli.getPath())
                // Allow connections via BOLT
                .setConfig(bolt.type, "BOLT")
                .setConfig(bolt.enabled, "true")
                .setConfig(bolt.address, cli.getAddress())
                // Read-only
                .setConfig(read_only, cli.getReadOnly())
                .newGraphDatabase();

        getRuntime().addShutdownHook(new Thread(graph::shutdown));

        System.out.println("Listening for connections at " + cli.getAddress() + "; Use CTRL-C to exit...");
        synchronized (lock) { lock.wait(); }
    }

    /**
     * Poor man's deamon thread.
     */
    private static final Object lock = new Object();
}
