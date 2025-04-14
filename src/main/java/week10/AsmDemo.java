package week10;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.io.FileOutputStream;

import static org.objectweb.asm.Opcodes.*;

public class AsmDemo {
    private static final String className = "Kala";

    public static void main(String[] args) throws Exception {
        try (FileOutputStream out = new FileOutputStream(className + ".class")) {
            out.write(generateClassBytes());
        }
    }

    private static byte[] generateClassBytes() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        // Klassi attribuudid
        cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object", null);
        cw.visitSource(null, null);

        // main meetod
        MethodVisitor mv = cw.visitMethod(
                ACC_PUBLIC + ACC_STATIC,                     // modifikaatorid
                "main",                                        // meetodi nimi
                "([Ljava/lang/String;)V",                    // meetodi kirjeldaja
                null,                                         // geneerikute info
                new String[] { "java/io/IOException" });
        mv.visitCode();

        generateMethodBody(mv);

        mv.visitMaxs(0, 0);
        mv.visitEnd();


        // klassi lõpetamine
        cw.visitEnd();

        // klassi baidijada genereerimine
        return cw.toByteArray();

    }

    private static void generateMethodBody(MethodVisitor mv) {
        mv.visitFieldInsn(GETSTATIC,
                "java/lang/System",
                "out",
                "Ljava/io/PrintStream;");
        mv.visitLdcInsn("hello");
        mv.visitMethodInsn(INVOKEVIRTUAL,
                "java/io/PrintStream",
                "println",
                "(Ljava/lang/String;)V", false);
        mv.visitInsn(RETURN);
    }
}
