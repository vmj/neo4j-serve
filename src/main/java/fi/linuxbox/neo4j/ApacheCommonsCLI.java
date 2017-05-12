package fi.linuxbox.neo4j;

import org.apache.commons.cli.*;

import java.io.File;
import java.nio.file.Paths;

import static org.apache.commons.cli.Option.builder;

/**
 * Implements the CLI using Apache Commons CLI library.
 */
class ApacheCommonsCLI implements CLI {
    /**
     * Default host.
     */
    private static final String DEFAULT_HOST = "localhost";

    /**
     * Default port.
     */
    private static final int DEFAULT_PORT = 7687;

    /**
     * Default path.
     */
    private static final String DEFAULT_PATH = ".";

    /**
     * Options.  Kept only to be able to show help.
     */
    private final Options options;

    /**
     * Whether help was requested on the command line (the -h option).
     */
    private final boolean help;

    /**
     * The -H option argument or {@link #DEFAULT_PATH default host}.
     */
    private final String host;

    /**
     * The -p option argument or {@link #DEFAULT_PORT default port}.
     */
    private final int port;

    /**
     * Whether write access was requested on the command line (the -w option).
     */
    private final boolean rw;

    /**
     * The PATH argument or {@link #DEFAULT_PATH default path}.
     */
    private final String path;

    /**
     * Initializes the CLI instance.
     *
     * @param args Command line arguments and options.
     * @throws ParseException if there are any problems encountered
     * while parsing the command line tokens.
     */
    ApacheCommonsCLI(String... args) throws ParseException {
        options = new Options()
                .addOption(builder("H")
                        .longOpt("host")
                        .numberOfArgs(1)
                        .argName("HOST")
                        .type(String.class)
                        .desc("Address of the interface on which to listen (default: " + DEFAULT_HOST +")")
                        .build())
                .addOption(builder("p")
                        .longOpt("port")
                        .numberOfArgs(1)
                        .argName("PORT")
                        .type(int.class)
                        .desc("TCP port on which to listen for connections (default: " + DEFAULT_PORT + ")")
                        .build())
                .addOption(builder("w")
                        .longOpt("write")
                        .numberOfArgs(0)
                        .type(boolean.class)
                        .desc("Enable write access for clients (default: read-only)")
                        .build())
                .addOption(builder("h")
                        .longOpt("help")
                        .numberOfArgs(0)
                        .type(boolean.class)
                        .desc("Show this help message and exit")
                        .build());

        final CommandLine cli = new DefaultParser().parse(options, args);

        this.help = cli.hasOption("h");
        this.host = cli.hasOption("H") ? cli.getOptionValue("H") : DEFAULT_HOST;
        this.port = cli.hasOption("p") ? Integer.valueOf(cli.getOptionValue("p")) : DEFAULT_PORT;
        this.rw = cli.hasOption("w");

        switch (cli.getArgs().length) {
            case 0:
                this.path = ".";
                break;
            case 1:
                this.path = cli.getArgs()[0];
                break;
            default:
                throw new IllegalArgumentException("too many arguments");
        }
    }

    /**
     * The address and port on which to listen for connections.
     *
     * E.g. "locahost:9000" or "127.0.0.1:3000".
     *
     * @return The listening address.
     */
    @Override
    public String getAddress() {
        return host + ":" + port;
    }

    /**
     * "true" if clients should have read-only access to the database. "false" otherwise.
     *
     * @return "true" or "false".
     */
    @Override
    public String getReadOnly() {
        return rw ? "true" : "false";
    }

    /**
     * The directory in which the database is.
     *
     * @return File instance representing the database directory.
     */
    @Override
    public File getPath() {
        return Paths.get(path).toFile();
    }

    /**
     * Show help if requested.
     *
     * @return true if help was shown, false otherwise.
     */
    @Override
    public boolean helpShown() {
        if (help) {
            new HelpFormatter().printHelp(
                    "[OPTIONS] [PATH]",
                    "Default PATH is " + DEFAULT_PATH,
                    options,
                    "Bug reports and other feedback to mikko@varri.fi"
            );
        }
        return help;
    }
}
