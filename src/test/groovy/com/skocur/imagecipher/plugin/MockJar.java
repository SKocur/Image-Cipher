package com.skocur.imagecipher.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class MockJar {

  static void createJarFileWithEmptyPluginYaml(File file) {
    createJarFileWithPluginYaml(file, "");
  }

  static void createJarFileWithPluginYaml(File file, String pluginContent) {
    createJarFile(file, new PluginFile(new ZipEntry("plugin.yml"), pluginContent));
  }

  static void createJarFile(File file, PluginFile... pluginFiles) {
    try {
      FileOutputStream fout = new FileOutputStream(file);
      JarOutputStream jarOut = new JarOutputStream(fout);

      for (PluginFile pluginFile : pluginFiles) {
        jarOut.putNextEntry(pluginFile.zipEntry);
        jarOut.write(pluginFile.content.getBytes());
        jarOut.closeEntry();
      }

      jarOut.close();
      fout.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static class PluginFile {

    ZipEntry zipEntry;
    String content;

    public PluginFile(ZipEntry zipEntry, String content) {
      this.zipEntry = zipEntry;
      this.content = content;
    }
  }
}
