package com.skocur.imagecipher.plugin.lib;

import java.util.LinkedList;
import java.util.List;

public class PluginInstance {
    private List<IcEncrypter> encrypters = new LinkedList();

    public List<IcEncrypter> getEncrypters() {
        return this.encrypters;
    }

    public void setEncrypters(List<IcEncrypter> encrypters) {
        this.encrypters = encrypters;
    }
}
