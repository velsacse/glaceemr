package com.glenwood.glaceemr.server.utils;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
* @author Anupama
* class to handle all kinds of Exceptions
*
* throw the exception in controller class in a try/catch block or give default throws declaration
* Exception can be Default Exception or custom exception. eg.UserNotFoundException or GenericException
* Example handler is given for default Exceptions
* Commented code contains a similar Exception handler declaration to handle custom Exceptions
*
* Add handler for your Exceptions(Custom / Default) with a custom message
*/

/**
* @author Anupama
*
*/
@ControllerAdvice
public class ExceptionController {

     @Resource
     EMRResponseBean emrResponseBean;

     /**
      *
      * @return emrResponseBean object with custom values set
      * by default success value is expected to be false in case of exceptions
      * and data as null
      *
      * you can change data by adding any custom message in case of exceptions and
      * modify your client side code as required
      */

     @ExceptionHandler(Exception.class)
     public @ResponseBody EMRResponseBean handleCustomException(Exception ex) {
         ex.printStackTrace();
         emrResponseBean.setSuccess(false);
         emrResponseBean.setData(null);
         return emrResponseBean;
     }
}
