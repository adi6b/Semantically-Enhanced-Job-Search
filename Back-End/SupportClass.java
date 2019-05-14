/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uga.ei_final;

import java.io.IOException;
import java.io.Reader;

/**
 *
 * @author agashe
 */
public class SupportClass {
    
            public static String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
              sb.append((char) cp);
            }
            return sb.toString();
          }
    
}
