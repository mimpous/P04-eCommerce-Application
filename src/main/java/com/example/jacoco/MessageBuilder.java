package com.example.jacoco;


/**
 * 
 * @author michailbousios
 *
 */
public class MessageBuilder {
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public String getMessage(String name) {

        StringBuilder result = new StringBuilder();

        if (name == null || name.trim().length() == 0) {

            result.append("Please provide a name!");

        } else {

            result.append("Hello " + name);

        }
        return result.toString();
    }
}
