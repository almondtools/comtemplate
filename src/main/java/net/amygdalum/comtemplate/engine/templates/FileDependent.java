package net.amygdalum.comtemplate.engine.templates;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpression;
import net.amygdalum.comtemplate.engine.TemplateInterpreter;

public interface FileDependent {

    default Path pathOf(String base, String source) {
        Path file = Paths.get(source);
        if (!Files.exists(file)) {
            file = Paths.get(base, source);
        }
        return file;
    }
    
    default File fileOf(String base, String source) {
        File file = new File(source);
        if (!file.exists()) {
            file = new File(base, source);
        }
        return file;
    }

    List<Path> files(TemplateInterpreter interpreter, Scope parent, TemplateExpression expression);
}
