package com.inn.counselling.utils;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class JasperCompileUitil.
 *
 */
public class JasperCompileUitil  {
	
	/** The logger. */
	private static Logger logger=LoggerFactory.getLogger(JasperCompileUitil.class);
    
    /** The parameters. */
    private Map parameters = null;
    
    /** The conn. */
   private Connection conn = null;
    
    /** The file name. */
   private String fileName = null;
    
    /** The folder context path. */
   private String folderContextPath = null;
    
    /** The folder name. */
   private String folderName = null;
   
    /** The data source. */
   private JRBeanCollectionDataSource dataSource;
    
    /**
     * Instantiates a new jasper compile uitil.
     */
    public JasperCompileUitil(){};
    
    /**
     * Instantiates a new jasper compile uitil.
     *
     * @param parameters the parameters
     * @param fileName the file name
     * @param folderContextPath the folder context path
     */
    public  JasperCompileUitil(Map parameters, String fileName,
              String folderContextPath) {
    	logger.info("inside @class JasperCompileUitil @method JasperCompileUitil @param parameters"+parameters+"fileName"+fileName+"folderContextPath"+folderContextPath);
     this.parameters = parameters;
     this.fileName = fileName;
     this.folderName = folderName;
     this.conn = null;
     this.folderContextPath = folderContextPath;
    try {
    
           File reportFile = new File(folderContextPath + fileName + ".jasper");
           JasperRunManager.runReportToPdfFile(reportFile.getPath(),parameters);
           
      } catch(Exception e) {
    	  
    	  logger.error("inside @class JasperCompileUitil @method JasperCompileUitil"+e);
            }finally{
    			 logger.info("Inside @class:JasperCompileUitil Constructor with Finally Block 1");
    		  }

      }  

      /**
       * Down load jasper.
       *
       * @param folderPath the folder path
       * @param fileName the file name
       * @return the response
       */
      public static Response downLoadJasper(String folderPath,String fileName, String fileExtentions) {
    	  logger.info("inside @class JasperCompileUitil @method downLoadJasper @param folderPath"+folderPath+"fileName"+fileName+"fileExtentions"+fileExtentions);
          try {
        	  File sourceFile = new File(folderPath +fileName + fileExtentions);
        	  ResponseBuilder restResponse= Response.ok(sourceFile, "application/octet-stream");
        	  restResponse.header("Content-Disposition",
                      "attachment; filename="+fileName+fileExtentions);
        	  
  	          return  restResponse.build();
  	       
  	        
              
          } catch (Exception e) {

        	  logger.error("inside @class JasperCompileUitil @method downLoadJasper"+e);
               return null;
          }finally{
 			 logger.info("Inside @class:JasperCompileUitil @method:downLoadJasper with Finally Block 1");
  		  }
      }
      
      
      /**
       * Down load jasper.
       *
       * @param response the response
       * @param downLoadFileName the down load file name
       */
      public void downLoadJasper(HttpServletResponse response,String downLoadFileName) {
          try {
              File sourceFile = new File(folderContextPath + "/jasper/" +
              folderName + "/" + downLoadFileName + ".pdf");
              FileInputStream fin = new FileInputStream(sourceFile);
              ServletOutputStream outStream = response.getOutputStream();
              response.setContentType("application/pdf");
              response.setHeader("Content-Disposition", "attachment;filename=" +
              downLoadFileName + ".pdf");
              byte[] buffer = new byte[1024];
              int n = 0;
              while ((n = fin.read(buffer)) != -1) {
                  outStream.write(buffer, 0, n);
              }
              outStream.close();
              fin.close();
              response.flushBuffer();
          } catch (Exception e) { 
              logger.debug(""+e.getMessage());
          }finally{
 			 logger.info("Inside @class:JasperCompileUitil @method:downLoadJasper with Finally Block 1");
  		  }
                                                             
       }              
}
