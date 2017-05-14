package com.bdev.smart.config;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.generator.SmartConfigGenerator;
import com.bdev.smart.config.parser.SmartConfigParser;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;

@Mojo(name = "jenesis4java", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class SmartConfigMojo extends AbstractMojo {
    @Parameter(property = "project", defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;
    @Parameter(property = "outputJavaDirectory", defaultValue = "${project.build.directory}/generated-sources/smart-config", readonly = true, required = true)
    protected File outputJavaDirectory;
    @Parameter(property = "smart.config.properties", required = true)
    protected String smartConfigProperties;
    @Parameter(property = "smart.config.values", required = true)
    protected String smartConfigDimensions;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (project != null) {
            project.addCompileSourceRoot(outputJavaDirectory.getAbsolutePath());
        }

        if (!outputJavaDirectory.mkdirs()) {
            throw new RuntimeException("Unable to create new source directory");
        }

        try {
            generateJavaCode();
        } catch (IOException e) {
            throw new MojoExecutionException("Could not generate Java source code!", e);
        }
    }

    private void generateJavaCode() throws IOException {
        try {
            ConfigInfo configInfo =
                    SmartConfigParser.parse(smartConfigDimensions, smartConfigProperties);

            SmartConfigGenerator
                    .generate(
                            outputJavaDirectory.getAbsolutePath(),
                            configInfo
                    );
        } catch (Exception e) {
            e.printStackTrace();

            System.out.println("fuck you");
        }
    }
}