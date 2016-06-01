package test.net.jawr.web.resource.bundle.generator.css;

import com.google.common.collect.Sets;
import net.jawr.web.JawrConstant;
import net.jawr.web.config.JawrConfig;
import net.jawr.web.resource.BinaryResourcesHandler;
import net.jawr.web.resource.bundle.IOUtils;
import net.jawr.web.resource.bundle.JoinableResourceBundle;
import net.jawr.web.resource.bundle.generator.GeneratorContext;
import net.jawr.web.resource.bundle.generator.GeneratorRegistry;
import net.jawr.web.resource.bundle.generator.css.less.LessCssGenerator;
import net.jawr.web.resource.bundle.handler.ResourceBundlesHandler;
import net.jawr.web.resource.bundle.mappings.FilePathMapping;
import net.jawr.web.resource.handler.reader.ResourceReaderHandler;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import test.net.jawr.web.FileUtils;
import test.net.jawr.web.servlet.mock.MockServletContext;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DifferentBinaryMappingLessCssGeneratorTestCase {

	public static final String BUNDLE_PATH = "/some/long/path/to/less/resource/differentBinaryMappingBundle.less";
	private static String WORK_DIR = "workDirLess";

	private JawrConfig config;
	private GeneratorContext ctx;
	private LessCssGenerator generator;

	@Mock
	private ResourceReaderHandler rsReaderHandler;

	@Mock
	private ResourceReaderHandler binaryRsReaderHandler;

	@Mock
	private ResourceBundlesHandler cssBundleHandler;

	@Mock
	private GeneratorRegistry generatorRegistry;

	@Mock
	private JoinableResourceBundle bundle;

	private List<FilePathMapping> filePathMappings;

	@Before
	public void setUp() throws Exception {

		// Bundle path (full url would be: /servletMapping/prefix/css/bundle.css

		Properties props = new Properties();
		props.put(JawrConstant.JAWR_CSS_URL_REWRITER_CONTEXT_PATH, "/");
		config = new JawrConfig("css", props);
		ServletContext servletContext = new MockServletContext();

		// String[] paths = new String[]{"/temp.less", "jar:/style.less"};
		servletContext.setAttribute(JawrConstant.CSS_CONTEXT_ATTRIBUTE, cssBundleHandler);

		config.setContext(servletContext);
		config.setServletMapping("/jawr/css");
		config.setCharsetName("UTF-8");
		when(generatorRegistry.isGeneratedBinaryResource(Matchers.startsWith("jar:"))).thenReturn(true);
		when(generatorRegistry.isHandlingCssImage(Matchers.startsWith("jar:"))).thenReturn(true);

		config.setGeneratorRegistry(generatorRegistry);

		generator = new LessCssGenerator();
		FileUtils.clearDirectory(FileUtils.getClasspathRootDir() + File.separator + WORK_DIR);
		FileUtils.createDir(WORK_DIR);

		generator.setWorkingDirectory(FileUtils.getClasspathRootDir() + "/" + WORK_DIR);
		ctx = new GeneratorContext(bundle, config, BUNDLE_PATH);
		ctx.setResourceReaderHandler(rsReaderHandler);

		// Set up the Image servlet Jawr config
		ServletContext binaryServletContext = new MockServletContext();
		JawrConfig binaryServletJawrConfig = new JawrConfig(JawrConstant.BINARY_TYPE, new Properties());
		binaryServletJawrConfig.setServletMapping("/jawr/binary");
		binaryServletJawrConfig.setGeneratorRegistry(generatorRegistry);
		when(binaryRsReaderHandler.getResourceAsStream(anyString()))
				.thenReturn(new ByteArrayInputStream("fakeData".getBytes()));
		BinaryResourcesHandler binaryRsHandler = new BinaryResourcesHandler(binaryServletJawrConfig,
				binaryRsReaderHandler, null);
		servletContext.setAttribute(JawrConstant.BINARY_CONTEXT_ATTRIBUTE, binaryRsHandler);

		generator.setResourceReaderHandler(rsReaderHandler);
		generator.setConfig(config);
		generator.afterPropertiesSet();

		filePathMappings = new ArrayList<>();
		when(bundle.getFilePathMappings()).thenReturn(filePathMappings);
	}

	@After
	public void tearDown() throws Exception {
		FileUtils.deleteDirectory(FileUtils.getClasspathRootDir() + "/" + WORK_DIR);

		// Make sure that importa.less is restored
		Reader rd = new FileReader(FileUtils.getClassPathFile("generator/css/less/import1/import1a.less.backup"));
		Writer wr = new FileWriter(FileUtils.getClassPathFile("generator/css/less/import1/import1a.less"));
		IOUtils.copy(rd, wr, true);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testLessCssBundleGeneratorInDebugMode() throws Exception {

		String tempLessContent = FileUtils.readClassPathFile("generator/css/less/differentBinaryMapping.less");
		when(rsReaderHandler.getResource(Matchers.any(JoinableResourceBundle.class), Matchers.eq(BUNDLE_PATH),
				Matchers.anyBoolean(), (List<Class<?>>) Matchers.any())).thenReturn(new StringReader(tempLessContent));
		when(rsReaderHandler.getResourceAsStream(anyString()))
				.thenReturn(new ByteArrayInputStream("fakeData".getBytes()));

		ctx.setProcessingBundle(false);
		Reader rd = generator.createResource(ctx);
		StringWriter writer = new StringWriter();
		IOUtils.copy(rd, writer);
		Assert.assertEquals(FileUtils.readClassPathFile("generator/css/less/expected_differentBinaryMapping.css"),
				writer.getBuffer().toString());

		assertEquals(0, filePathMappings.size());
	}

}
