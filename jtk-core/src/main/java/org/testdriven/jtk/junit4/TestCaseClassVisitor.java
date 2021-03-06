package org.testdriven.jtk.junit4;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

public class TestCaseClassVisitor extends EmptyVisitor {

    private final JUnit4AnalisysContext analisys;
    private final MethodVisitor methodVisitor;

    public TestCaseClassVisitor(JUnit4AnalisysContext byteCodeAnalyzer,
            MethodVisitor methodVisitor) {
        this.analisys = byteCodeAnalyzer;
        this.methodVisitor = methodVisitor;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
            String signature, String[] exceptions) {

        analisys.setCurrentMethodName(name);
        return methodVisitor;
    }
}
