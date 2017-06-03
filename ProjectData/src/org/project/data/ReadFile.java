package org.project.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Gebaseerd op
 * https://stackoverflow.com/questions/18700205/code-for-reading-from-a-text-file-doesnt-work
 */
public class ReadFile {

    private String path;

    /**
     *
     * @param file_path Het bestand waar we de inhoud van willen hebben.
     */
    public ReadFile(String file_path) {
        path = file_path;
    }

    /**
     *
     * @return een array van Strings waarbij elk element een tekstregel bevat.
     * @throws IOException
     */
    public String[] OpenFile() throws IOException {
        FileReader fr = new FileReader(path);
        BufferedReader textReader = new BufferedReader(fr);

        int numberOfLines = countLines();
        String[] textData = new String[numberOfLines];

        int i;
        for (i = 0; i < numberOfLines; i++) {
            textData[i] = textReader.readLine();
        }

        textReader.close();
        return textData;
    }

    /**
     * 
     * @return een getal met het aantal regels
     * @throws IOException 
     */
    int countLines() throws IOException {
        FileReader file_to_read = new FileReader(path);
        BufferedReader bf = new BufferedReader(file_to_read);

        String aLine;
        int numberOfLines = 0;

        while ((aLine = bf.readLine()) != null) {
            numberOfLines++;
        }
        bf.close();

        return numberOfLines;
    }
}
