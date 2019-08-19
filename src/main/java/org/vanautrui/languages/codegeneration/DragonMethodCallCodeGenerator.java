package org.vanautrui.languages.codegeneration;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.vanautrui.languages.codegeneration.symboltables.nameconversions.TypeNameToJVMInternalTypeNameConverter;
import org.vanautrui.languages.codegeneration.symboltables.tables.DragonMethodScopeVariableSymbolTable;
import org.vanautrui.languages.codegeneration.symboltables.tables.DragonSubroutineSymbolTable;
import org.vanautrui.languages.parsing.astnodes.nonterminal.DragonExpressionNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.statements.DragonMethodCallNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.upperscopes.DragonClassNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.upperscopes.DragonMethodNode;
import org.vanautrui.languages.typeresolution.DragonTypeResolver;

import java.util.stream.Collectors;

import static org.objectweb.asm.Opcodes.*;

public class DragonMethodCallCodeGenerator {

    //https://tomassetti.me/generating-bytecode/

    private static void compile_printing_statement(
            ClassWriter cw, MethodVisitor mv, DragonClassNode classNode, DragonMethodNode methodNode,
            DragonMethodCallNode methodCallNode, DragonMethodScopeVariableSymbolTable methodScopeSymbolTable,
            DragonSubroutineSymbolTable subroutineSymbolTable,boolean debug)throws Exception{

        //TODO: actually compile the stuff, not just fake

        mv.visitFieldInsn(GETSTATIC,
                "java/lang/System",
                "out",
                "Ljava/io/PrintStream;");

        //get the jvm internal type for the descriptor of the method
        String methodDescriptor ="(Ljava/lang/String;)V";

        if(methodCallNode.argumentList.size()>0) {
            //mv.visitLdcInsn(methodCallNode.argumentList.get(0).str);
            for(DragonExpressionNode expressionNode : methodCallNode.argumentList){

                //TODO: make getTypeJVMInternal() to make this easier? or just make a translator class for it
                String expressionType=DragonTypeResolver.getTypeExpressionNode(expressionNode,methodNode,subroutineSymbolTable);
                if(expressionType.equals("Int") || expressionType.equals("ERROR")){
                    //set the  descriptor to the signature which accepts int
                    methodDescriptor="(I)V";
                }
                DragonExpressionCodeGenerator.visitExpression(cw,mv,classNode,methodNode,expressionNode,methodScopeSymbolTable,subroutineSymbolTable,debug);
            }
            //DragonStringConstantCodeGenerator.visitStringConstant(cw,mv,classNode,methodNode,methodCallNode.argumentList.get(0),methodScopeSymbolTable);
        }else{
            mv.visitLdcInsn("");
        }

        switch (methodCallNode.identifierMethodName.name.getContents()) {
            case "println":
                mv.visitMethodInsn(INVOKEVIRTUAL,
                        "java/io/PrintStream",
                        "println",
                        methodDescriptor);
                break;
            case "print":
                mv.visitMethodInsn(INVOKEVIRTUAL,
                        "java/io/PrintStream",
                        "print",
                        methodDescriptor);
                break;
            default:
                throw new Exception("unrecognized method: '" + methodCallNode.identifierMethodName.name.getContents() + "'");

        }
    }

    public static void visitMethodCallNode(ClassWriter cw, MethodVisitor mv, DragonClassNode classNode, DragonMethodNode methodNode, DragonMethodCallNode methodCallNode, DragonMethodScopeVariableSymbolTable methodScopeSymbolTable,DragonSubroutineSymbolTable subroutineSymbolTable,boolean debug) throws Exception {
        //TODO: actually compile the stuff, not just fake

        if(subroutineSymbolTable.containsVariable(methodCallNode.identifierMethodName.name.getContents())){
            String subrType = DragonTypeResolver.getTypeMethodCallNode(methodCallNode,subroutineSymbolTable);

            String methodName = methodCallNode.identifierMethodName.name.getContents();
            String owner=classNode.name.typeName.getContents();//TODO: figure out if this is ok
            String descriptor= TypeNameToJVMInternalTypeNameConverter.convertSubroutineName(
                    subrType,methodCallNode.argumentList.stream().map(expressionNode -> {
                        try {
                            return DragonTypeResolver.getTypeExpressionNode(expressionNode,methodNode,subroutineSymbolTable);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new RuntimeException(" FATAL error in DragonMethodCallCodeGenerator");
                        }
                    }).collect(Collectors.toList()),
                    debug
            );

            //push the arguments on the stack
            for(DragonExpressionNode expr : methodCallNode.argumentList){
                DragonExpressionCodeGenerator.visitExpression(cw,mv,classNode,methodNode,expr,methodScopeSymbolTable,subroutineSymbolTable,debug);
            }

            mv.visitMethodInsn(INVOKESTATIC,owner,methodName,descriptor);

            //DEBUG
            System.out.println("found method in symbol table");
        }else {

            switch (methodCallNode.identifierMethodName.name.getContents()) {

                case "readln":
                    //create an instance of Scanner
                    //mv.visitInsn(NEW);

                    //the new instruction must get an index into
                    //the runtime constant pool of the current class
                    //int myconst = cw.newConst("java/util/Scanner");

                    //mv.visitVarInsn(NEW,myconst);
                    mv.visitTypeInsn(NEW, "java/util/Scanner");
                    //mv.visitInsn(NEW,"java/util/Scanner");

                    mv.visitInsn(DUP);

                    //DEBUG: try to comment out stuff individually and see
                    //what happens

                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
                    mv.visitMethodInsn(INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V");

                    //call the Scanner.nextLine();
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Scanner", "nextLine", "()Ljava/lang/String;", false);

                    //throw new Exception("readln() not implemented (DragonMethodCallGenerator)");
                    break;
                default:
                    compile_printing_statement(cw, mv, classNode, methodNode, methodCallNode, methodScopeSymbolTable, subroutineSymbolTable,debug);
            }
        }
    }
}
