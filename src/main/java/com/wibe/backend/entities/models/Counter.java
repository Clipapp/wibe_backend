package com.wibe.backend.entities.models;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
public class Counter {

        @GraphId Long id;
        
        @Property(name = "counter")
        private String counter;

        @Property(name = "seq")
        private Long seq;
        
        public void setCounter(String counter){
        	this.counter = counter;
        }
        
        public String getCounter(){
        	return this.counter;
        }
        
        public void setSeq(Long seq){
        	this.seq = seq;
        }
        
        public Long getSeq(){
        	return this.seq;
        }

}
