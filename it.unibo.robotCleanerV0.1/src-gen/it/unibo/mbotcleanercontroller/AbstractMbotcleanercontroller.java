/* Generated by AN DISI Unibo */ 
package it.unibo.mbotcleanercontroller;
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
public abstract class AbstractMbotcleanercontroller extends QActor { 
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
		public AbstractMbotcleanercontroller(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/mbotcleanercontroller/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/mbotcleanercontroller/plans.txt";
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
	    	stateTab.put("waitStart",waitStart);
	    	stateTab.put("doWork",doWork);
	    	stateTab.put("handleConditionStop",handleConditionStop);
	    	stateTab.put("handletempSensor",handletempSensor);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "mbotcleanercontroller tout : stops");  
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
	    	temporaryStr = "\"Robot Ready\"";
	    	println( temporaryStr );  
	    	//switchTo waitStart
	        switchToPlanAsNextState(pr, myselfName, "mbotcleanercontroller_"+myselfName, 
	              "waitStart",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun waitStart = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_waitStart",0);
	     pr.incNumIter(); 	
	    	String myselfName = "waitStart";  
	    	//bbb
	     msgTransition( pr,myselfName,"mbotcleanercontroller_"+myselfName,false,
	          new StateFun[]{stateTab.get("doWork") }, 
	          new String[]{"true","M","usercmdMsg" },
	          36000000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_waitStart){  
	    	 println( getName() + " plan=waitStart WARNING:" + e_waitStart.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//waitStart
	    
	    StateFun doWork = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("doWork",-1);
	    	String myselfName = "doWork";  
	    	printCurrentEvent(false);
	    	parg = "getTime"; 
	    	actorOpExecute(parg, false);	//OCT17		 
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " ??actorOpDone(OP,R)" )) != null ){
	    	//not here genMove StateMoveNormal}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(start,TEMP)");
	    	if( currentMessage != null && currentMessage.msgId().equals("usercmdMsg") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(CMD,TEMP)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg  ="curTemperatureValue(X)";
	    		String parg1 ="curTemperatureValue(TEMP)";
	    		/* ReplaceRule */
	    		parg = updateVars(Term.createTerm("usercmd(CMD,TEMP)"),  Term.createTerm("usercmd(start,TEMP)"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg);
	    		parg1 = updateVars(Term.createTerm("usercmd(CMD,TEMP)"),  Term.createTerm("usercmd(start,TEMP)"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg1);
	    		if( parg != null && parg1 != null  ) replaceRule(parg, parg1);	    		  					
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(start,TEMP)");
	    	if( currentMessage != null && currentMessage.msgId().equals("usercmdMsg") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(CMD,TEMP)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		{//actionseq
	    		if( (guardVars = QActorUtils.evalTheGuard(this, " !?evalCondition(start)" )) != null ){
	    		{//actionseq
	    		temporaryStr = "\"START\"";
	    		println( temporaryStr );  
	    		temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmdToLed(CMD)","cmdToLed(on)", guardVars ).toString();
	    		sendMsg("cmdToLed","ledonmbot", QActorContext.dispatch, temporaryStr ); 
	    		};//actionseq
	    		}
	    		else{ temporaryStr = "\"CONDITION OF TIME OR TEMP NOT SATISFIED\"";
	    		println( temporaryStr );  
	    		}};//actionseq
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(halt,TEMP)");
	    	if( currentMessage != null && currentMessage.msgId().equals("usercmdMsg") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(CMD,TEMP)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		{//actionseq
	    		temporaryStr = "\"STOP\"";
	    		println( temporaryStr );  
	    		temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmdToLed(CMD)","cmdToLed(off)", guardVars ).toString();
	    		sendMsg("cmdToLed","ledonmbot", QActorContext.dispatch, temporaryStr ); 
	    		};//actionseq
	    	}
	    	//switchTo handleConditionStop
	        switchToPlanAsNextState(pr, myselfName, "mbotcleanercontroller_"+myselfName, 
	              "handleConditionStop",false, true, null); 
	    }catch(Exception e_doWork){  
	    	 println( getName() + " plan=doWork WARNING:" + e_doWork.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//doWork
	    
	    StateFun handleConditionStop = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_handleConditionStop",0);
	     pr.incNumIter(); 	
	    	String myselfName = "handleConditionStop";  
	    	parg = "getTime"; 
	    	actorOpExecute(parg, false);	//OCT17		 
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " ??actorOpDone(OP,TIME)" )) != null ){
	    	//not here genMove StateMoveNormal}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?evalCondition(halt)" )) != null ){
	    	{//actionseq
	    	temporaryStr = "\"STOP\"";
	    	println( temporaryStr );  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmdToLed(CMD)","cmdToLed(off)", guardVars ).toString();
	    	sendMsg("cmdToLed","ledonmbot", QActorContext.dispatch, temporaryStr ); 
	    	};//actionseq
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"mbotcleanercontroller_"+myselfName,false,
	          new StateFun[]{stateTab.get("handletempSensor"), stateTab.get("doWork") }, 
	          new String[]{"true","M","tempSensorMsg", "true","M","usercmdMsg" },
	          10000, "handleConditionStop" );//msgTransition
	    }catch(Exception e_handleConditionStop){  
	    	 println( getName() + " plan=handleConditionStop WARNING:" + e_handleConditionStop.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleConditionStop
	    
	    StateFun handletempSensor = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handletempSensor",-1);
	    	String myselfName = "handletempSensor";  
	    	printCurrentEvent(false);
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("tempSensor(TEMP)");
	    	if( currentMessage != null && currentMessage.msgId().equals("tempSensorMsg") && 
	    		pengine.unify(curT, Term.createTerm("tempSensor(TEMP)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg  ="curTemperatureValue(X)";
	    		String parg1 ="curTemperatureValue(TEMP)";
	    		/* ReplaceRule */
	    		parg = updateVars(Term.createTerm("tempSensor(TEMP)"),  Term.createTerm("tempSensor(TEMP)"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg);
	    		parg1 = updateVars(Term.createTerm("tempSensor(TEMP)"),  Term.createTerm("tempSensor(TEMP)"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg1);
	    		if( parg != null && parg1 != null  ) replaceRule(parg, parg1);	    		  					
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"mbotcleanercontroller_"+myselfName,false,true);
	    }catch(Exception e_handletempSensor){  
	    	 println( getName() + " plan=handletempSensor WARNING:" + e_handletempSensor.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handletempSensor
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}