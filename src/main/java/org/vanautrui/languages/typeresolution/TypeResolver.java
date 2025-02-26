package org.vanautrui.languages.typeresolution;

import org.vanautrui.languages.parsing.astnodes.nonterminal.ArrayConstantNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.ExpressionNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.TermNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.statements.MethodCallNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.upperscopes.MethodNode;
import org.vanautrui.languages.parsing.astnodes.terminal.*;
import org.vanautrui.languages.symboltables.tables.LocalVarSymbolTable;
import org.vanautrui.languages.symboltables.tables.SubroutineSymbolTable;

import java.util.Arrays;
import java.util.List;

public class TypeResolver {

    //todo: make some class or global subroutine
    //that can then convert the type description directly
    //to a jvm internal representation

    public static String getTypeIntegerConstantNode(IntegerConstantNode integerConstantNode){
        return "Int";
    }
    public static String getTypeFloatConstantNode(FloatConstantNode node){
    	return "Float";
    }

    public static String getTypeStringConstantNode(StringConstantNode stringConstantNode){
        return "String";
    }

    public static String getTypeVariableNode(VariableNode variableNode, MethodNode methodNode, SubroutineSymbolTable subroutineSymbolTable, LocalVarSymbolTable varTable)throws Exception{
        //TODO: implement by looking at the definitions in the AST and such

        if( varTable.containsVariable(variableNode.name) ){
            //the symbol table should contain the array type, if it is an array
            //but this method should return the type also if it has an index
            if(variableNode.indexOptional.isPresent()){
                String type = varTable.getTypeOfVariable(variableNode.name);
                return type.substring(1,type.length()-1); //'[Int]' -> 'Int'
            }

			return varTable.getTypeOfVariable(variableNode.name);
        }else{
            throw new Exception("could not determine type of "+variableNode.name);
        }
    }

    public static String getTypeTermNode(TermNode termNode, MethodNode methodNode,
                                         SubroutineSymbolTable subroutineSymbolTable,
                                         LocalVarSymbolTable varTable
    )throws Exception{
        if(termNode.termNode instanceof ExpressionNode){
            return getTypeExpressionNode((ExpressionNode)termNode.termNode,methodNode,subroutineSymbolTable,varTable);
        }else if (termNode.termNode instanceof MethodCallNode){
            return getTypeMethodCallNode((MethodCallNode)termNode.termNode,subroutineSymbolTable);
	}else if(termNode.termNode instanceof FloatConstantNode){
		return getTypeFloatConstantNode((FloatConstantNode)termNode.termNode);
        }else if(termNode.termNode instanceof IntegerConstantNode){
            return getTypeIntegerConstantNode((IntegerConstantNode)termNode.termNode);
        }else if(termNode.termNode instanceof StringConstantNode){
            return getTypeStringConstantNode((StringConstantNode)termNode.termNode);
        }else if(termNode.termNode instanceof VariableNode){
            return getTypeVariableNode((VariableNode) termNode.termNode,methodNode,subroutineSymbolTable,varTable);
		}else if(termNode.termNode instanceof BoolConstantNode) {
            return "Bool";
        }else if(termNode.termNode instanceof ArrayConstantNode) {
            return getTypeArrayConstNode((ArrayConstantNode) termNode.termNode, methodNode, subroutineSymbolTable, varTable);
        }else if(termNode.termNode instanceof CharConstantNode){
            return "Char";
        }else{
            throw new Exception("unforeseen case in getTypeTermNode(...) in DragonTypeResolver");
        }

    }

    private static String getTypeArrayConstNode(ArrayConstantNode arrayConstantNode,MethodNode methodNode, SubroutineSymbolTable subroutineSymbolTable, LocalVarSymbolTable varTable) throws Exception {
        //since the array types should be all the same,
        //that should be checked in the package responsible for typechecking
        //here we assume it will be checked there

        if(arrayConstantNode.elements.size()==0){
            throw new Exception("array size should be atleast 1, for the type to be inferred without type annotations");
        }

        //for the array to have a type, it has to either be annotated,
        // or contain atleast 1 element of which the type can be known
        return "["+getTypeExpressionNode(arrayConstantNode.elements.get(0),methodNode,subroutineSymbolTable,varTable)+"]";
    }

    public static String getTypeExpressionNode(ExpressionNode expressionNode, MethodNode methodNode, SubroutineSymbolTable subTable, LocalVarSymbolTable varTable) throws Exception{
        List<String> boolean_operators= Arrays.asList("<",">","<=",">=","==","!=");

        if(
                getTypeTermNode(expressionNode.term,methodNode,subTable,varTable).equals("Int") &&
                        expressionNode.termNodes.size()==1 &&
                        getTypeTermNode(expressionNode.termNodes.get(0),methodNode,subTable,varTable).equals("Int") &&
                        expressionNode.operatorNodes.size()==1 &&
                        (boolean_operators.contains(expressionNode.operatorNodes.get(0).operator))
        ){
            return "Bool";
        }

		if(
                getTypeTermNode(expressionNode.term,methodNode,subTable,varTable).equals("Float") &&
                        expressionNode.termNodes.size()==1 &&
                        getTypeTermNode(expressionNode.termNodes.get(0),methodNode,subTable,varTable).equals("Float") &&
                        expressionNode.operatorNodes.size()==1 &&
                        (boolean_operators.contains(expressionNode.operatorNodes.get(0).operator))
        ){
            return "Bool";
        }


        String type = getTypeTermNode(expressionNode.term,methodNode,subTable,varTable);

        /*
        for(DragonOperatorNode op : this.operatorNodes){
            if(!op.operator.equals("+")){
                throw new Exception("only '+' is supported for now");
            }
        }

         */

        for (TermNode t : expressionNode.termNodes){
            String termType = getTypeTermNode(t,methodNode,subTable,varTable);

            if(!(termType.equals(type))){
                throw new Exception(
					"the types are not the same, "+type+" collides with "+termType
					+" in '"+expressionNode.toSourceCode()+"'"
				);
            }
        }

        return getTypeTermNode(expressionNode.term,methodNode,subTable,varTable);
    }

    public static String getTypeMethodCallNode(MethodCallNode methodCallNode, SubroutineSymbolTable subroutineSymbolTable) throws Exception{

        String subrName = methodCallNode.getMethodName();

        if(subrName.equals("readln")){
            return "String";
        }

        //these calls are Void
        //but since there is no void in this language, they are int
        //we have to think about that during code generation later on
        List<String> builtin_stuff=Arrays.asList("print","println");
        if(builtin_stuff.contains(subrName)){
            return "Int";
        }
        //TODO: handle the other builtin methods

        if(subroutineSymbolTable.containsVariable(subrName)){
            return subroutineSymbolTable.getTypeOfVariable(subrName);
        }

        //TODO: throw exception if not found in symbol table
        throw new Exception("colud not get type of "+subrName+" ( in TypeResolver.java)");
    }
}
