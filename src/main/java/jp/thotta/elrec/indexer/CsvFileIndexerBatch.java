package jp.thotta.elrec.indexer;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


public class CsvFileIndexerBatch {
  private File file;
  public CsvFileIndexerBatch(File file) {
    this.file = file;
  }

  public void doIndex() throws IOException {
    PreferenceIndexer indexer = new PreferenceIndexer();
    FileReader fr = new FileReader(this.file);
    BufferedReader br = new BufferedReader(fr);
    String str;
    while((str = br.readLine()) != null){
      String[] arr = str.split(",");
      long userId = Long.parseLong(arr[0]);
      long itemId = Long.parseLong(arr[1]);
      indexer.addPreference(userId, itemId);
    }
  }
}
