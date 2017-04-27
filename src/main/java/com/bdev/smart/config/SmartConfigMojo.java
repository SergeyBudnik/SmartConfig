package com.bdev.smart.config;

import com.bdev.smart.config.generator.SmartConfigGenerator;
import net.sourceforge.jenesis4java.VirtualMachine;
import net.sourceforge.jenesis4java.jaloppy.JenesisJalopyEncoder;
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
    @Parameter(property = "smart.config.dimensions", required = true)
    protected String smartConfigDimensions;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (this.project != null) {
            this.project.addCompileSourceRoot(this.outputJavaDirectory.getAbsolutePath());
        }

        if (!this.outputJavaDirectory.mkdirs()) {
            getLog().error("Could not create source directory!");
        } else {
            try {
                generateJavaCode();
            } catch (IOException e) {
                throw new MojoExecutionException("Could not generate Java source code!", e);
            }
        }
    }

    private void generateJavaCode() throws IOException {
        try {
            SmartConfigGenerator
                    .generate(
                            outputJavaDirectory.getAbsolutePath(),
                            smartConfigProperties,
                            smartConfigDimensions
                    );
        } catch (Exception e) {
            System.out.println("fuck you");
        }
    }
}