package com.example.mario_project.myapplication.framework;

/**
 * Created by woolo_so5omoy on 2017-08-08.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileIO {
   public InputStream readAsset(String fileName) throws IOException;
   public InputStream readFile(String fileName) throws IOException;
   public OutputStream writeFile(String fileName) throws IOException;
}
