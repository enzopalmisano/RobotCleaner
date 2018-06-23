/* Generated by AN DISI Unibo */ 
package it.unibo.sendtemperaturecontroller;
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
public abstract class AbstractSendtemperaturecontroller extends QActor { 
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
		public AbstractSendtemperaturecontroller(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/sendtemperaturecontroller/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/sendtemperaturecontroller/plans.txt";
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
	    	stateTab.put("catchTemperature",catchTemperature);
	    	stateTab.put("handleCondition",handleCondition);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "sendtemperaturecontroller tout : stops");  
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
	        switchToPlanAsNextState(pr, myselfName, "sendtemperaturecontroller_"+myselfName, 
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
	     msgTransition( pr,myselfName,"sendtemperaturecontroller_"+myselfName,false,
	          new StateFun[]{stateTab.get("catchTemperature") }, 
	          new String[]{"true","M","tempSensor" },
	          360000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_waitStart){  
	    	 println( getName() + " plan=waitStart WARNING:" + e_waitStart.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//waitStart
	    
	    StateFun catchTemperature = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("catchTemperature",-1);
	    	String myselfName = "catchTemperature";  
	    	parg = "getTemperature"; 
	    	actorOpExecute(parg, false);	//OCT17		 
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " ??actorOpDone(R)" )) != null ){
	    	//not here genMove StateMoveNormal}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("tempSensor(giveMeTemp)");
	    	if( currentMessage != null && currentMessage.msgId().equals("tempSensor") && 
	    		pengine.unify(curT, Term.createTerm("tempSensor(TEMP)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="tempSensor(TEMP)";
	    		/* SendDispatch */
	    		parg = updateVars(Term.createTerm("tempSensor(TEMP)"),  Term.createTerm("tempSensor(giveMeTemp)"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) sendExtMsg("tempSensor","console","ctxConsoleRobotGui",QActorContext.dispatch, parg ); 
	    	}
	    	//switchTo handleCondition
	        switchToPlanAsNextState(pr, myselfName, "sendtemperaturecontroller_"+myselfName, 
	              "handleCondition",false, true, null); 
	    }catch(Exception e_catchTemperature){  
	    	 println( getName() + " plan=catchTemperature WARNING:" + e_catchTemperature.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//catchTemperature
	    
	    StateFun handleCondition = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_handleCondition",0);
	     pr.incNumIter(); 	
	    	String myselfName = "handleCondition";  
	    	parg = "getTemperature"; 
	    	actorOpExecute(parg, false);	//OCT17		 
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " ??actorOpDone(R)" )) != null ){
	    	//not here genMove StateMoveNormal}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?evalCondition(halt)" )) != null ){
	    	{//actionseq
	    	temporaryStr = "\"STOP\"";
	    	println( temporaryStr );  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(halt,TEMP)");
	    	if( currentMessage != null && currentMessage.msgId().equals("usercmdMsg") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(CMD,TEMP)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="usercmd(halt,TEMP)";
	    		/* SendDispatch */
	    		parg = updateVars(Term.createTerm("usercmd(CMD,TEMP)"),  Term.createTerm("usercmd(halt,TEMP)"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) sendExtMsg("usercmdMsg","mbotcleanercontroller","ctxRobotCleaner",QActorContext.dispatch, parg ); 
	    	}
	    	};//actionseq
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"sendtemperaturecontroller_"+myselfName,false,
	          new StateFun[]{}, 
	          new String[]{},
	          10000, "handleCondition" );//msgTransition
	    }catch(Exception e_handleCondition){  
	    	 println( getName() + " plan=handleCondition WARNING:" + e_handleCondition.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleCondition
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
