/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.praqma.prqa.products;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.praqma.prqa.PRQA;
import net.praqma.prqa.PRQACommandLineUtility;
import net.praqma.prqa.logging.Config;
import net.praqma.util.execute.CmdResult;

/**
 *
 * @author jes, man
 */
public class QAC extends PRQA {

    private static final Logger logger;

    static {
        logger = Logger.getLogger(Config.GLOBAL_LOGGER_NAME);
    }
    
    public QAC() {}
    
    public QAC(String command) {
    	logger.finest(String.format("Constructor called for class QAC(String command)"));
    	logger.finest(String.format("Input parameter command type: %s; value: %s", command.getClass(), command));
    	
        this.command = command;
        
        logger.finest(String.format("Ending execution of constructor - QAC"));
    }

    public QAC(String commandBase, String command) {
    	logger.finest(String.format("Constructor called for class QAC(String commandBase, String command)"));
    	logger.finest(String.format("Input parameter commandBase type: %s; value: %s", commandBase.getClass(), commandBase));
    	logger.finest(String.format("Input parameter command type: %s; value: %s", command.getClass(), command));
    	
        this.command = command;
        this.commandBase = commandBase;
        
        logger.finest(String.format("Ending execution of constructor - QAC"));
    }

    public CmdResult execute(String command, File dir) {
        logger.finest(String.format("Starting execution of method - execute(String command, File dir)"));
        logger.finest(String.format("Input parameter command type: %s; value: %s", command.getClass(), command));
        logger.finest(String.format("Input parameter dir type: %s; value: %s", dir.getClass(), dir));

        CmdResult output = PRQACommandLineUtility.run(command, dir);
        
        logger.finest(String.format("Returning value: %s", output));
        
        return output;
    }

    public CmdResult execute(String command) {
        logger.finest(String.format("Starting execution of method - execute(String command)"));
        logger.finest(String.format("Input parameter command type: %s; value: %s", command.getClass(), command));

        CmdResult output = PRQACommandLineUtility.run(command, new File(commandBase));
        
        logger.finest(String.format("Returning value: %s", output));
        
        return output;
    }

    public CmdResult generateReportFiles() {
        logger.finest(String.format("Starting execution of method - generateReportFiles()"));

        CmdResult output = PRQACommandLineUtility.run(command, new File(commandBase));
        
        logger.finest(String.format("Returning value: %s", output));
        
        return output; 
    }
    
    @Override
    public String getProductVersion() {
        String productVersion = "Unknown version";
       
        //CmdResult res = PRQACommandLineUtility.run("qac -version", new File(commandBase));
       
        //productVersion = res.stdoutBuffer.toString();
        
        return productVersion;
    }

    @Override
    public String toString() {
        return "qac";
    }
    
}
