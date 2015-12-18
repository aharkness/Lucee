package lucee.transformer.cfml.evaluator.func.impl;

import lucee.runtime.exp.TemplateException;
import lucee.transformer.TransformerException;
import lucee.transformer.bytecode.expression.var.Argument;
import lucee.transformer.bytecode.expression.var.Assign;
import lucee.transformer.bytecode.expression.var.BIF;
import lucee.transformer.bytecode.expression.var.NamedArgument;
import lucee.transformer.bytecode.expression.var.VariableString;
import lucee.transformer.cfml.evaluator.EvaluatorException;
import lucee.transformer.cfml.evaluator.FunctionEvaluator;
import lucee.transformer.expression.var.Variable;
import lucee.transformer.library.function.FunctionLibFunction;

public class QueryExecute implements FunctionEvaluator{

	@Override
	public void execute(BIF bif, FunctionLibFunction flf) throws TemplateException {
		
	}

	@Override
	public void evaluate(BIF bif, FunctionLibFunction flf) throws EvaluatorException {
		Variable var = bif.getParent();
		if(var!=null) {
			Assign ass = var.assign();
			if(ass!=null) {
				try {
					String str = VariableString.variableToString(ass.getVariable(), false);
					addArgument(bif,str);
				}
				catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
	}

	private void addArgument(BIF bif, String str) {
		Argument[] args = bif.getArguments();
		
		// named arguments
		if(args[0] instanceof NamedArgument) {
			bif.addArgument(
					new NamedArgument(
							bif.getFactory().createLitString("name"), 
							bif.getFactory().createLitString(str), 
							"string", 
							false));
		}
		// regular arguments
		else {
			// add params
			if(args.length==1) {
				bif.addArgument(new Argument(bif.getFactory().createNull(), "any"));
			}
			// add options
			if(args.length<=2) {
				bif.addArgument(new Argument(bif.getFactory().createStruct(), "struct"));
			}
			// add the name
			bif.addArgument(new Argument(bif.getFactory().createLitString(str), "string"));
		}
	}
}