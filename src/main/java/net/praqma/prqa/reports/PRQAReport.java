/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.praqma.prqa.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.logging.Logger;
import net.praqma.prga.excetions.PrqaCommandLineException;
import net.praqma.prga.excetions.PrqaException;
import net.praqma.prqa.PRQAContext;
import net.praqma.prqa.analyzer.PRQAanalyzer;
import net.praqma.prqa.logging.Config;
import net.praqma.prqa.parsers.ReportHtmlParser;
import net.praqma.prqa.products.QAR;
import net.praqma.prqa.status.PRQAStatus;
import net.praqma.util.execute.AbnormalProcessTerminationException;
import net.praqma.util.execute.CmdResult;
import net.praqma.util.execute.CommandLineException;

/**
 *Class defining a report. The report holds a commmand line too. (The QAR object) and also holds a result, 
 * 
 * @author Praqma
 */
public abstract class PRQAReport<T extends PRQAStatus> implements Serializable {
        
	protected static final Logger logger = Logger.getLogger(PRQAReport.class.getName());
    private EnumSet<PRQAContext.QARReportType> chosenReports;
    private HashMap<String, String> environment;
	
    protected ReportHtmlParser parser;
    protected QAR reportTool;
    private boolean useCrossModuleAnalysis;
    
    //RQ-1
    private boolean enableDependencyMode;
    private boolean enableDataFlowAnalysis;
    
    //Store the result of the executed command result.
    protected CmdResult cmdResult;
    
    public static String XHTML = "xhtml";
    public static String XML = "xml";
    public static String HTML = "html";
    
    public static String XHTML_REPORT_EXTENSION = "Report."+PRQAReport.XHTML;
    public static String XML_REPORT_EXTENSION = "Report."+PRQAReport.XML;
    public static String HTML_REPORT_EXTENSION = "Report."+PRQAReport.HTML;
    
    public PRQAReport() {
    }
    
    public static String getNamingTemplate(PRQAContext.QARReportType type, String extension) {
        return type.toString()+ " "+extension;
    }
    
    public PRQAReport(QAR reportTool) {
    	logger.finest(String.format("Constructor called for class PRQAReport(QAR reportTool)"));
    	logger.finest(String.format("Input parameter qar type: %s; value: %s", reportTool.getClass(), reportTool));
    	
        this.reportTool = reportTool;
        
        logger.finest(String.format("Ending execution of constructor - PRQAReport"));
	}

	public void setParser(ReportHtmlParser parser) {
		logger.finest(String.format("Starting execution of method - setParser"));
		logger.finest(String.format("Input parameter parser type: %s; value: %s", parser.getClass(), parser));
		
        this.parser = parser;
        
        logger.finest(String.format("Ending execution of method - setParser"));
    }
    
    public ReportHtmlParser getParser() {
    	logger.finest(String.format("Starting execution of method - getParser"));
    	logger.finest(String.format("Returning value: %s", this.parser));
    	
        return this.parser;
    }
    
    public QAR getReportTool() {
    	logger.finest(String.format("Starting execution of method - getQar"));
    	logger.finest(String.format("Returning value: %s", this.reportTool));
    	
        return this.reportTool;
    }
    
    public PRQAanalyzer getAnalysisTool() {
        return this.reportTool.getAnalysisTool();
    }

    public void setReportTool(QAR reportTool) {
    	logger.finest(String.format("Starting execution of method - setQar"));
		logger.finest(String.format("Input parameter qar type: %s; value: %s", reportTool.getClass(), reportTool));
		
        this.reportTool = reportTool;
        
        logger.finest(String.format("Ending execution of method - setQar"));
    }
    
    public CmdResult getCmdResult() {
    	logger.finest(String.format("Starting execution of method - getCmdResult"));
    	logger.finest(String.format("Returning value: %s", this.cmdResult));
    	
        return this.cmdResult;
    }
    
    public void setCmdResult(CmdResult res) {
    	logger.finest(String.format("Starting execution of method - setCmdResult"));
		logger.finest(String.format("Input parameter res type: %s; value: %s", res.getClass(), res));
		
        this.cmdResult = res;
        
        logger.finest(String.format("Ending execution of method - setCmdResult"));
    }
    
    /**
     * QAR Reports seem to follow the naming convention of this kind.
     * @return A string representing the actual filename generated by the QAR reporting tool. 
     */
    public String getNamingTemplate() {
    	logger.finest(String.format("Starting execution of method - getNamingTemplate()"));
    	
    	String result = reportTool.getType() + " " + PRQAReport.XHTML_REPORT_EXTENSION;
    	
    	logger.finest(String.format("Returning value: %s", result));
    	
        return result;
    }
   
    
    /**
     * Provides an alternative extension to the default XHTML extension
     * 
     *Options include:
     * 
     * PRQAReport.XML_REPORT_EXTENSION
     * PRQAReport.HTML_REPORT_EXTENSION
     * PRQAReport.XHTML_REPORT_EXTENSION
     * 
     * @param extension
     * @return 
     */
    public String getNamingTemplate(String extension) {
    	logger.finest(String.format("Starting execution of method - getNamingTemplate(String extension)"));
    	logger.finest(String.format("Input parameter extension type: %s; value: %s", extension.getClass(), extension));
    	
    	String result = reportTool.getType() + " " + extension;
    	
    	logger.finest(String.format("Returning value: %s", result));
    	
        return result;
    }
    
    /**
     * Knowing the naming convention. This will give us a complete path to the report. This should always be the Workspace directory, followed by 
     * the template name for the report.
     * @return A string representing the full path to the generated report.
     */
    public String getFullReportPath() {
    	logger.finest(String.format("Starting execution of method - getFullReportPath"));
    	
    	String result = reportTool.getReportOutputPath() + File.separator + getNamingTemplate();
    	
    	logger.finest(String.format("Returning value: %s", result));
    	
        return result;
    }
    
    public void executeQAR() throws PrqaException {
    	logger.finest(String.format("Starting execution of method - executeQAR"));
        
        /**
         * Throw an exception if the report does NOT exits on the file system.
         */
       
        if(!(new File(getReportTool().getProjectFile()).exists())) {
            throw new PrqaCommandLineException("Error in QAR: ", new FileNotFoundException(String.format("Project file %s not found", getReportTool().getProjectFile())), reportTool);
        }
		
		String fullReportPath = this.getFullReportPath();
		
		logger.finest(String.format("Setting full report path to: %s", fullReportPath));
		
		parser.setFullReportPath(fullReportPath);
		cmdResult = null;
		
		logger.finest(String.format("Attempting to generate report files..."));
		try {
            reportTool.getAnalysisTool().analyze();
			cmdResult = reportTool.report();            
		} catch (AbnormalProcessTerminationException ex) {
			PrqaCommandLineException exception = new PrqaCommandLineException("Failed in report generation",ex,reportTool);
			
			logger.severe(String.format("Exception thrown type: %s; message: %s", exception.getClass(), exception.getMessage()));
			
			throw exception;
		} catch (CommandLineException cle) {
			PrqaCommandLineException exception = new PrqaCommandLineException("Failed in report generation with CLI Exception: ", cle, reportTool);
			
			logger.severe(String.format("Exception thrown type: %s message: %s", exception.getClass(), exception.getMessage()));
			
			throw exception;
		} catch (Exception ex) {
            PrqaCommandLineException prqaclex = new PrqaCommandLineException(String.format("Exception thrown type: %s",ex.getClass()), ex, reportTool);
            throw prqaclex;
        }
		logger.finest(String.format("qar executed successfully!"));
		
		logger.finest(String.format("Ending execution of method - executeQAR"));
    }
    
    /**
     * @return the useCrossModuleAnalysis
     */
    public boolean isUseCrossModuleAnalysis() {
        return useCrossModuleAnalysis;
    }

    /**
     * @param useCrossModuleAnalysis the useCrossModuleAnalysis to set
     */
    public void setUseCrossModuleAnalysis(boolean useCrossModuleAnalysis) {
        this.useCrossModuleAnalysis = useCrossModuleAnalysis;
    }
    
    
    /**
     * The task that is to be executed on the master/slave hosting the job. 
     * @param parameter
     * @return
     * @throws PrqaException 
     */
    public abstract <T> T generateReport() throws PrqaException;        
    
    public abstract String getDisplayName();
    
    /**
     * Factory method used to create the report. Currently only the compliance report is used.
     * @param type
     * @param reportTool
     * @return 
     */
    public static PRQAReport create(PRQAContext.QARReportType type, QAR reportTool) {
        logger.finest(String.format("In create(PRQAContext.QARReportType type, QAR reportTool) with args type = %s and reportTool = %s", type, reportTool));
        PRQAReport report = null;
        switch(type) {
            case Compliance:
                report = new PRQAComplianceReport();
                report.reportTool = reportTool;
                return report;
            default:
                throw new IllegalArgumentException("No valid report type given!");
        }   
    }

    /**
     * @return the enableDependencyMode
     */
    public boolean isEnableDependencyMode() {
        return enableDependencyMode;
    }

    /**
     * @param enableDependencyMode the enableDependencyMode to set
     */
    public void setEnableDependencyMode(boolean enableDependencyMode) {
        this.enableDependencyMode = enableDependencyMode;
    }

    /**
     * @return the chosenReports
     */
    public EnumSet<PRQAContext.QARReportType> getChosenReports() {
        return chosenReports;
    }

    /**
     * @param chosenReports the chosenReports to set
     */
    public void setChosenReports(EnumSet<PRQAContext.QARReportType> chosenReports) {
        this.chosenReports = chosenReports;
    }

    /**
     * @return the enableDataFlowAnalysis
     */
    public boolean isEnableDataFlowAnalysis() {
        return enableDataFlowAnalysis;
    }

    /**
     * @param enableDataFlowAnalysis the enableDataFlowAnalysis to set
     */
    public void setEnableDataFlowAnalysis(boolean enableDataFlowAnalysis) {
        this.enableDataFlowAnalysis = enableDataFlowAnalysis;
    }

    /**
     * @return the environment
     */
    public HashMap<String, String> getEnvironment() {
        return environment;
    }

    /**
     * @param environment the environment to set
     */
    public void setEnvironment(HashMap<String, String> environment) {
        this.environment = environment;
    }
}

