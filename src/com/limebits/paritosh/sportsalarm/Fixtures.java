package com.limebits.paritosh.sportsalarm;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

public class Fixtures {
	public List<String> fixtures;
	private String feedUrl = "http://synd.cricbuzz.com/ashwl/scores-multi.xml";
	
    protected InputStream getInputStream() {
        try {
            return new URL(feedUrl).openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public String[] toArray() {
    	String[] result = new String[fixtures.size()];
    	Object[] f = fixtures.toArray();
    	for (int i=0; i<fixtures.size(); i++) {
    		result[i] = f[i].toString();
    	}
    	
    	return result;
    }

	public Fixtures() {
		fixtures = new ArrayList<String>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(this.getInputStream());
            Element root = dom.getDocumentElement();
            NodeList matches = root.getElementsByTagName("match");
            for (int i=0; i<matches.getLength(); i++){
                Node match = matches.item(i);
                NodeList properties = match.getChildNodes();
                
                String team1 = "", team2 = "", state = "";
             
                for (int j=0; j<properties.getLength(); j++){
                    Node property = properties.item(j);
                    String name = property.getNodeName();
                    if (name.equalsIgnoreCase("team1")){
                        team1 = property.getFirstChild().getNodeValue();
                    } else if (name.equalsIgnoreCase("team2")){
                        team2 = property.getFirstChild().getNodeValue();
                    } else if (name.equalsIgnoreCase("state")) {
                    	state = property.getFirstChild().getNodeValue();
                    }
                }
                
                if (state.equalsIgnoreCase("preview")) {
                	fixtures.add(team1 + " vs. " + team2);	
                }
                
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
	}
}
