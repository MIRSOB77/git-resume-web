package com.exozet.gitresumeweb;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;

public final class TestUtils {

    public static String getResource(String path) throws IOException {
        return Resources.toString(Resources.getResource(path), Charsets.UTF_8);
        
    }

}
