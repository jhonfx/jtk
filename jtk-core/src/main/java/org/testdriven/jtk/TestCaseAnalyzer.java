package org.testdriven.jtk;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class TestCaseAnalyzer {

    private final class DirectoryLister extends DirectoryWalker {

        public DirectoryLister() {
            super(null, new WildcardFileFilter("*.java"), -1);
        }

        public Collection<File> listSourceFiles(File startDirectory)
                throws IOException {
            Collection<File> files = new ArrayList<File>();

            walk(startDirectory, files);

            return files;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void handleFile(File file, int depth, Collection results)
                throws IOException {
            results.add(file);
        }
    }
    private final List<String> sourcesDirs;
    private final List<String> classesDirs;
    private final AnalisysEngine engine;

    public TestCaseAnalyzer(AnalisysEngine engine, String[] sourceDirs,
            String[] classesDirs) {

        if (sourceDirs != null) {
            this.sourcesDirs = asList(sourceDirs);
        } else {
            this.sourcesDirs = asList(new String[]{});
        }

        if (classesDirs != null) {
            this.classesDirs = asList(classesDirs);
        } else {
            this.classesDirs = asList(new String[]{});
        }
        this.engine = engine;
    }

    public AnalyzerResults analyzeTestCases() throws IOException {
        Collection<CompilationUnit> units = findTestCasesSources();

        //match test cases sources with class
        for (CompilationUnit unit : units) {
            for(String baseDir:classesDirs){
                File classFile = unit.getClassFile(baseDir);
                if(classFile.exists()){
                    unit.setClassFile(classFile);
                }
            }
        }
        
        return new AnalyzerResults();
    }

    private Collection<CompilationUnit> findTestCasesSources() throws IOException {
        // lists all test cases source files
        DirectoryLister dirWalker = new DirectoryLister();
        Collection<CompilationUnit> units = new ArrayList<CompilationUnit>();
        for (String baseSrcDir : sourcesDirs) {
            final File sourcesDir = new File(baseSrcDir);
            final Collection<File> sourceFiles = dirWalker.listSourceFiles(sourcesDir);
            for (File file : sourceFiles) {
                CompilationUnit unit = new CompilationUnit(sourcesDir, file);
                units.add(unit);
            }
        }
        return units;
    }
}
