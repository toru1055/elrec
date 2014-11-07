`$ mvn clean`  
`$ mvn assembly:assembly -DdescriptorId=jar-with-dependencies`  
`$ java -cp target/elrec-1.0-SNAPSHOT-jar-with-dependencies.jar jp.thotta.elrec.searcher.SearchServer`  

Start Search Server  
`$ mvn exec:java -Dexec.mainClass=jp.thotta.elrec.searcher.SearchServer -Dexec.args="-t 1"`  

Start Index Server  
`$ mvn exec:java -Dexec.mainClass=jp.thotta.elrec.indexer.IndexServer -Dexec.args="-t 1"`


Run Search Client  
`$ mvn exec:java -Dexec.mainClass=jp.thotta.elrec.searcher.SearchClient`  

Run Index Client
`$ mvn exec:java -Dexec.mainClass=jp.thotta.elrec.indexer.IndexClient`  
