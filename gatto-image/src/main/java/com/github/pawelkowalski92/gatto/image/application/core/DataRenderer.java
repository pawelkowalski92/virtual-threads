package com.github.pawelkowalski92.gatto.image.application.core;

import java.io.IOException;
import java.io.OutputStream;

public interface DataRenderer {

    void renderTo(OutputStream outputStream) throws IOException;

}
