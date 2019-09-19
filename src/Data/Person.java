/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

/**
 *<pre>
 *  Class           Person
 *  File            Person.java
 *  Description     This class keeps track of the people associated with each
 *                  IMDbPage. They can be the director or actor.
 *  @author         Illarion Eremenko
 *  Environment     PC, Windows 7, NetBeans IDE 8.2, jdk 1.8.0_172
 *  </pre>
 */
public class Person {
    private String url;
    private String name;
    
    /**
     * Default constructor, only allowing to set url
     */
    public Person(){
        this.url = null;
        this.name = null;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return "Person{" + "url=" + url + "\n name=" + name + '}';
    }
    
}
