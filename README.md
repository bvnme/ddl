# Oracle DDL Generator

If you are using free Oracle SQL Developer you might have found its object export very powerful.
It would be handy to have a similar instrument to run from shell, rather than from GUI. I could
not find one, so I chose to reuse Oracle java libraries from SQL Developer app and to write a
tiny command line exporter.

Usage:

./export.sh -c "jdbc:oracle:thin:@192.168.120.141:1521:devel" -u system -p password -s SCHEMA -t TABLE -n MYTABLE
