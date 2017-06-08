#!/bin/bash

CP=dist/ddl.jar
CP=$CP:lib/commons-cli-1.4.jar
CP=$CP:lib/dbapi-impl.jar
CP=$CP:lib/dbapi.jar
CP=$CP:lib/dbplsql.jar
CP=$CP:lib/javatools-nodeps.jar
CP=$CP:lib/ojdbc6.jar
CP=$CP:lib/xmlparserv2.jar
CP=$CP:lib/oracle.dbtools-common.jar

java -cp $CP com.mesilat.ddl.Generator $*
