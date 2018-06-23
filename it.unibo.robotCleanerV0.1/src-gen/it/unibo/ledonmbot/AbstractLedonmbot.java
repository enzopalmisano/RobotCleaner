/* Generated by AN DISI Unibo */ 
package it.unibo.ledonmbot;
import it.unibo.qactors.PlanRepeat;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.StateExecMessage;
import it.unibo.qactors.QActorUtils;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.action.AsynchActionResult;
import it.unibo.qactors.action.IActorAction;
import it.unibo.qactors.action.IActorAction.ActionExecMode;
import it.unibo.qactors.action.IMsgQueue;
import it.unibo.qactors.akka.QActor;
import it.unibo.qactors.StateFun;
import java.util.Stack;
import java.util.Hashtable;
import java.util.concurrent.Callable;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.qactors.action.ActorTimedAction;
public abstract class AbstractLedonmbot extends QActor { 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	 
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			return outEnvView;
		}
		public AbstractLedonmbot(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/ledonmbot/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/ledonmbot/plans.txt";
	  	}
		@Override
		protected void doJob() throws Exception {
			String name  = getName().replace("_ctrl", "");
			mysupport = (IMsgQueue) QActorUtils.getQActor( name ); 
			initStateTable(); 
	 		initSensorSystem();
	 		history.push(stateTab.get( "init" ));
	  	 	autoSendStateExecMsg();
	  		//QActorContext.terminateQActorSystem(this);//todo
		} 	
		/* 
		* ------------------------------------------------------------
		* PLANS
		* ------------------------------------------------------------
		*/    
	    //genAkkaMshHandleStructure
	    protected void initStateTable(){  	
	    	stateTab.put("handleToutBuiltIn",handleToutBuiltIn);
	    	stateTab.put("init",init);
	    	stateTab.put("waitCommand",waitCommand);
	    	stateTab.put("doWork",doWork);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "ledonmbot tout : stops");  
	    		repeatPlanNoTransition(pr,myselfName,"application_"+myselfName,false,false);
	    	}catch(Exception e_handleToutBuiltIn){  
	    		println( getName() + " plan=handleToutBuiltIn WARNING:" + e_handleToutBuiltIn.getMessage() );
	    		QActorContext.terminateQActorSystem(this); 
	    	}
	    };//handleToutBuiltIn
	    
	    StateFun init = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("init",-1);
	    	String myselfName = "init";  
	    	temporaryStr = "\"Inizializza oggetto led\"";
	    	println( temporaryStr );  
	    	//switchTo waitCommand
	        switchToPlanAsNextState(pr, myselfName, "ledonmbot_"+myselfName, 
	              "waitCommand",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun waitCommand = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_waitCommand",0);
	     pr.incNumIter(); 	
	    	String myselfName = "waitCommand";  
	    	//bbb
	     msgTransition( pr,myselfName,"ledonmbot_"+myselfName,false,
	          new StateFun[]{stateTab.get("doWork") }, 
	          new String[]{"true","M","cmdToLed" },
	          3600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_waitCommand){  
	    	 println( getName() + " plan=waitCommand WARNING:" + e_waitCommand.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//waitCommand
	    
	    StateFun doWork = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("doWork",-1);
	    	String myselfName = "doWork";  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cmdToLed(on)");
	    	if( currentMessage != null && currentMessage.msgId().equals("cmdToLed") && 
	    		pengine.unify(curT, Term.createTerm("cmdToLed(CMD)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "\"LED ON\"";
	    		/* Print */
	    		parg =  updateVars( Term.createTerm("cmdToLed(CMD)"), 
	    		                    Term.createTerm("cmdToLed(on)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) println( parg );
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cmdToLed(off)");
	    	if( currentMessage != null && currentMessage.msgId().equals("cmdToLed") && 
	    		pengine.unify(curT, Term.createTerm("cmdToLed(CMD)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "\"LED OFF\"";
	    		/* Print */
	    		parg =  updateVars( Term.createTerm("cmdToLed(CMD)"), 
	    		                    Term.createTerm("cmdToLed(off)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) println( parg );
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"ledonmbot_"+myselfName,false,true);
	    }catch(Exception e_doWork){  
	    	 println( getName() + " plan=doWork WARNING:" + e_doWork.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//doWork
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
