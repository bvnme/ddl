package com.mesilat.ddl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import oracle.javatools.db.DBException;
import oracle.javatools.db.DBObject;
import oracle.javatools.db.Database;
import oracle.javatools.db.ddl.DDL;
import oracle.javatools.db.ddl.DDLGenerator;
import oracle.javatools.db.ddl.DDLOptions;
import oracle.javatools.db.ora.OracleDatabaseFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Generator {
    private static final String DRIVER_NAME = "oracle.jdbc.OracleDriver";

    public static void main(String[] args) throws ParseException, ClassNotFoundException, SQLException, DBException {
        Options opts = new Options();
        opts.addOption(Option.builder("c").longOpt("connection").desc("Oraclee connection URL").hasArg(true).argName("url").required().build());
        opts.addOption(Option.builder("u").longOpt("username").desc("Oracle user to connect to").hasArg(true).argName("user").required().build());
        opts.addOption(Option.builder("p").longOpt("password").desc("Oracle user password").hasArg(true).argName("password").required().build());
        opts.addOption(Option.builder("s").longOpt("schema").desc("Oracle schema").hasArg(true).argName("name").required().build());
        opts.addOption(Option.builder("t").longOpt("type").desc("Oracle object type").hasArg(true).argName("type").required().build());
        opts.addOption(Option.builder("n").longOpt("name").desc("Oracle object name").hasArg(true).argName("name").required().build());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        
        try {
            cmd = parser.parse(opts, args);
        } catch(MissingOptionException ex) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("export.sh", opts);
            System.exit(1);
        }

        String url = cmd.getOptionValue("c"),
                username = cmd.getOptionValue("u"),
                password = cmd.getOptionValue("p"),
                schema = cmd.getOptionValue("s"),
                type = cmd.getOptionValue("t"),
                name = cmd.getOptionValue("n");

        Class.forName(DRIVER_NAME);
        
        try (Connection conn = DriverManager.getConnection(url, username, password)){
            OracleDatabaseFactory factory = new OracleDatabaseFactory();
            String connStore = "store";
            String connName = "conn";
            Database db = factory.createDatabaseImpl(connStore, connName, conn);

            DDLGenerator gen = db.getDDLGenerator();
            DBObject obj = db.getObject(type, db.getSchema(schema), name);

            DDLOptions dbo = new DDLOptions();
            dbo.setCascade(true);
            dbo.setIncludePrompts(false);
            dbo.setPrefixSchemaName(true);
            dbo.setReplace(false);
            dbo.setSpoolFile(null);
            dbo.setSqlBlankLines(false);

            DDL ddl = gen.getCreateDDL(dbo, new DBObject[]{obj});
            System.out.println(ddl.toString(true));
        }
    }   
}