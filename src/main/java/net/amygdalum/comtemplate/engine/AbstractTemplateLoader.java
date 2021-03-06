package net.amygdalum.comtemplate.engine;

import static net.amygdalum.comtemplate.engine.TemplateGroup.NONE_ID;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTemplateLoader implements TemplateLoader {

	private TemplateCompiler compiler;
	private Map<String, TemplateGroup> resolvedGroups;

	public AbstractTemplateLoader(TemplateCompiler compiler) {
		this.compiler = compiler;
		this.resolvedGroups = new HashMap<String, TemplateGroup>();
	}

	public AbstractTemplateLoader() {
		this(new DefaultTemplateCompiler());
	}

	public TemplateGroup compile(String name, String resource, InputStream stream) throws IOException {
		return compiler.compileLibrary(name, resource, stream, this);
	}

	public TemplateDefinition compileMain(String name, String resource, InputStream stream) throws IOException {
		return compiler.compileMain(name, resource, stream, this);
	}

	public TemplateDefinition compileText(String text) throws IOException {
		return compileMain(NONE_ID, NONE_ID, new ByteArrayInputStream(text.getBytes()));
	}

	@Override
	public TemplateGroup loadGroup(String name) {
		TemplateGroup resolved = resolvedGroups.get(name);
		if (resolved == null) {
			try {
				InputStream stream = loadSource(name);
				String resource = resolveResource(name);
				resolved = compile(name, resource, stream);
				resolvedGroups.put(name, resolved);
			} catch (IOException e) {
				throw new TemplateGroupNotFoundException(name);
			}
		}
		return resolved;
	}

	public abstract InputStream loadSource(String name) throws IOException;

	public abstract String resolveResource(String name);

	@Override
	public TemplateDefinition loadDefinition(String name) {
		int separator = name.lastIndexOf('.');
		if (separator < 0) {
			throw new TemplateDefinitionNotFoundException("unknown", name);
		}
		String file = name.substring(0, separator);
		String definition = name.substring(separator + 1);
		TemplateGroup group = loadGroup(file);
		return group.getDefinition(definition);
	}

	protected String pathOf(String name) {
		return name.replace('.', '/')  + ".ctp";
	}

}
