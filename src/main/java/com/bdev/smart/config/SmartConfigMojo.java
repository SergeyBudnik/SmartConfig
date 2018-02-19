package com.bdev.smart.config;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.generator.SmartConfigGlobalGenerator;
import com.bdev.smart.config.parser.SmartConfigParser;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

@Mojo(name = "jenesis4java", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class SmartConfigMojo extends AbstractMojo {
    @Parameter(property = "project", defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;
    @Parameter(property = "buildDirectory", defaultValue = "${project.build.directory}", readonly = true, required = true)
    protected File buildDirectory;
    @Parameter(property = "smart.config.relativeDirectory", defaultValue = "/generated-sources/smart-config", required = true)
    protected String relativeDirectory;
    @Parameter(property = "smart.config.rootPackage", defaultValue = "com.bdev.smart.config", required = true)
    protected String rootPackage;
    @Parameter(property = "smart.config.csvProperties", required = true)
    protected String csvProperties;
    @Parameter(property = "smart.config.dimensions", required = true)
    protected String dimensions;

    @Override
    public void execute() throws MojoExecutionException {
        if (project != null) {
            project.addCompileSourceRoot(getOutputDirectory().getAbsolutePath());
        }

        if (!getOutputDirectory().mkdirs()) {
            throw new RuntimeException("Unable to create new source directory");
        }

        generateJavaCode();
    }

    private void generateJavaCode() throws MojoExecutionException {
        try {
            ConfigInfo configInfo =
                    SmartConfigParser.parse(dimensions, csvProperties);

            SmartConfigGlobalGenerator
                    .generate(
                            getOutputDirectory().getAbsolutePath(),
                            rootPackage,
                            configInfo
                    );
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage());
        }
    }

    private File getOutputDirectory() {
        return new File(buildDirectory.getAbsolutePath() + relativeDirectory);
    }
}